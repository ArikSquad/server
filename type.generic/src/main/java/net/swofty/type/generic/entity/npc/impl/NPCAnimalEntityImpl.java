package net.swofty.type.generic.entity.npc.impl;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.AnimalConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

public final class NPCAnimalEntityImpl extends AbstractNPCEntityImpl<AnimalConfiguration> {

    public NPCAnimalEntityImpl(
            @NotNull HypixelNPC npc,
            @NotNull HypixelPlayer viewer,
            @NotNull Pos pos,
            @NotNull String bottomDisplay,
            @NotNull EntityType entityType,
            @NotNull AnimalConfiguration config,
            String[] holograms,
            boolean overflowing
    ) {
        super(npc, viewer, entityType, pos, bottomDisplay, config, holograms, true, config.hologramYOffset());
    }

    @Override
    protected float additionalHologramOffset() {
        return getConfig().hologramYOffset();
    }
}
