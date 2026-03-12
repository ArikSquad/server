package net.swofty.type.generic.entity.npc.impl;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.avatar.MannequinMeta;
import net.minestom.server.network.player.ResolvableProfile;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class NPCEntityImpl extends AbstractNPCEntityImpl<HumanConfiguration> {
    private String skinTexture;
    private String skinSignature;

    public NPCEntityImpl(
            @NotNull HypixelNPC npc,
            @NotNull HypixelPlayer viewer,
            @NotNull Pos pos,
            @NotNull String bottomDisplay,
            @NotNull String skinTexture,
            @NotNull String skinSignature,
            @NotNull String[] holograms,
            HumanConfiguration config,
            boolean overflowing
    ) {
        super(npc, viewer, EntityType.MANNEQUIN, pos, bottomDisplay, config, holograms, false, false, 1.9f);
        this.skinTexture = skinTexture;
        this.skinSignature = skinSignature;

        editEntityMeta(MannequinMeta.class, meta -> {
            meta.setImmovable(true);
            meta.setProfile(new ResolvableProfile(new PlayerSkin(skinTexture, skinSignature)));
        });
    }

    @Override
    protected boolean shouldSplitBottomLine() {
        return false;
    }

    @Override
    protected float additionalHologramOffset() {
        return 0.1f;
    }

    @Override
    protected void onPresentationSync() {
        String actualSkinTexture = getConfig().texture(getViewer());
        String actualSkinSignature = getConfig().signature(getViewer());
        if (!Objects.equals(skinTexture, actualSkinTexture) || !Objects.equals(skinSignature, actualSkinSignature)) {
            this.skinTexture = actualSkinTexture;
            this.skinSignature = actualSkinSignature;
            editEntityMeta(MannequinMeta.class, meta ->
                    meta.setProfile(new ResolvableProfile(new PlayerSkin(actualSkinTexture, actualSkinSignature))));
        }
    }
}
