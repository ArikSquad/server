package net.swofty.type.generic.entity.npc.impl;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.VillagerProfession;
import net.minestom.server.entity.VillagerType;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

public final class NPCVillagerEntityImpl extends AbstractNPCEntityImpl<VillagerConfiguration> {

    public NPCVillagerEntityImpl(
            @NotNull HypixelNPC npc,
            @NotNull HypixelPlayer viewer,
            Pos pos,
            @NotNull String bottomDisplay,
            VillagerProfession profession,
            VillagerConfiguration config,
            String[] holograms,
            boolean overflowing
    ) {
        super(npc, viewer, EntityType.VILLAGER, pos, bottomDisplay, config, holograms, true, 0.5f);
        VillagerMeta meta = (VillagerMeta) this.entityMeta;
        meta.setVillagerData(new VillagerMeta.VillagerData(VillagerType.PLAINS, profession, VillagerMeta.Level.EXPERT));
    }
}
