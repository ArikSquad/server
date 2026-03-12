package net.swofty.type.generic.entity.npc.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EntityAnimationPacket;
import net.minestom.server.network.packet.server.play.EntityEquipmentPacket;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.behavior.NPCAnimation;
import net.swofty.type.generic.entity.npc.behavior.NPCBehaviorState;
import net.swofty.type.generic.entity.npc.behavior.NPCLoadout;
import net.swofty.type.generic.entity.npc.configuration.NPCConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class AbstractNPCEntityImpl<C extends NPCConfiguration> extends EntityCreature implements NPCViewable {
    private final List<HypixelPlayer> inRangeOf = Collections.synchronizedList(new ArrayList<>());
    private final HypixelNPC npc;
    private final HypixelPlayer viewer;
    private final C config;
    private final PlayerHolograms.ExternalPlayerHologram holo;
    private String[] holograms;
    private NPCLoadout lastLoadout = NPCLoadout.EMPTY;

    protected AbstractNPCEntityImpl(
            @NotNull HypixelNPC npc,
            @NotNull HypixelPlayer viewer,
            @NotNull EntityType entityType,
            @NotNull Pos pos,
            @NotNull String bottomDisplay,
            @NotNull C config,
            @NotNull String[] holograms,
            boolean useEntityCustomName,
            boolean splitBottomLine,
            float baseHologramOffset
    ) {
        super(entityType);
        this.npc = npc;
        this.viewer = viewer;
        this.config = config;
        this.holograms = processedHolograms(holograms, splitBottomLine);

        setAutoViewable(false);
        setNoGravity(true);
        if (useEntityCustomName) {
            set(DataComponents.CUSTOM_NAME, Component.text(bottomDisplay));
            setCustomNameVisible(!splitBottomLine);
        } else {
            setCustomNameVisible(false);
        }

        this.holo = PlayerHolograms.ExternalPlayerHologram.builder()
                .pos(pos.add(0, baseHologramOffset, 0))
                .text(this.holograms)
                .player(viewer)
                .instance(config.instance())
                .build();

        if (config.shouldDisplayHolograms(viewer)) {
            PlayerHolograms.addExternalPlayerHologram(this.holo);
        }

        setInstance(config.instance(), pos);
        addViewer(viewer);
        setPose(config.pose(viewer));
    }

    @Override
    public void remove() {
        super.remove();
        PlayerHolograms.removeExternalPlayerHologram(holo);
    }

    @Override
    public void updateNewViewer(@NotNull Player player) {
        super.updateNewViewer(player);
        syncPresentation();
        syncBehavior(npc.behaviorState());
        player.sendPacket(new EntityEquipmentPacket(getEntityId(), lastLoadout.asEquipmentMap()));
    }

    @Override
    public void tick(long time) {
        Instance instance = getInstance();
        Pos position = getPosition();
        if (instance == null) {
            return;
        }

        if (!instance.isChunkLoaded(position)) {
            instance.loadChunk(position).join();
        }

        try {
            super.tick(time);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void syncPresentation() {
        Pos npcPosition = npc.currentPosition(viewer);
        setPose(config.pose(viewer));
        if (!getPosition().samePoint(npcPosition)) {
            refreshPosition(npcPosition);
        }

        if (config.shouldDisplayHolograms(viewer)) {
            if (!PlayerHolograms.externalPlayerHolograms.containsKey(holo)) {
                PlayerHolograms.addExternalPlayerHologram(holo);
            }
            String[] newHolograms = processedHolograms(config.holograms(viewer), shouldSplitBottomLine());
            if (!Arrays.equals(newHolograms, holograms)) {
                PlayerHolograms.updateExternalPlayerHologramText(holo, newHolograms);
                holograms = newHolograms;
            }
            PlayerHolograms.relocateExternalPlayerHologram(holo, npcPosition.add(0, hologramBaseOffset(config.holograms(viewer)), 0));
        } else {
            PlayerHolograms.removeExternalPlayerHologram(holo);
        }

        onPresentationSync();
    }

    @Override
    public void syncBehavior(NPCBehaviorState state) {
        Pos targetPosition = state != null ? state.renderedPosition() : npc.currentPosition(viewer);
        if (!getPosition().samePoint(targetPosition)) {
            refreshPosition(targetPosition);
        }

        float yawOffset = state == null ? 0 : state.headYawOffset();
        setView(targetPosition.yaw() + yawOffset, targetPosition.pitch());

        NPCLoadout loadout = state != null && !state.loadout().isEmpty()
            ? state.loadout()
                : config.loadout(viewer);
        applyLoadout(loadout);

        if (state != null) {
            for (NPCAnimation animation : state.animations()) {
                EntityAnimationPacket.Animation packetAnimation = switch (animation.type()) {
                    case SWING_MAIN_HAND -> EntityAnimationPacket.Animation.SWING_MAIN_ARM;
                    case SWING_OFF_HAND -> EntityAnimationPacket.Animation.SWING_OFF_HAND;
                };
                viewer.sendPacket(new EntityAnimationPacket(getEntityId(), packetAnimation));
            }
        }
    }

    private void applyLoadout(NPCLoadout loadout) {
        if (loadout.equals(lastLoadout)) {
            return;
        }

        for (var entry : loadout.asEquipmentMap().entrySet()) {
            setEquipment(entry.getKey(), entry.getValue());
        }
        lastLoadout = loadout;
        viewer.sendPacket(new EntityEquipmentPacket(getEntityId(), loadout.asEquipmentMap()));
    }

    private static String[] processedHolograms(String[] source, boolean splitBottomLine) {
        if (source == null || source.length == 0) {
            return new String[0];
        }
        boolean overflowing = source[source.length - 1].length() > 16;
        if (!splitBottomLine || overflowing) {
            return source;
        }
        return Arrays.copyOfRange(source, 0, source.length - 1);
    }

    protected float hologramBaseOffset(String[] sourceHolograms) {
        boolean overflowing = sourceHolograms[sourceHolograms.length - 1].length() > 16;
        return (float) (getEyeHeight() + additionalHologramOffset() + (overflowing ? -0.2f : 0f));
    }

    protected boolean shouldSplitBottomLine() {
        return true;
    }

    protected float additionalHologramOffset() {
        return 0.5f;
    }

    protected void onPresentationSync() {
    }
}
