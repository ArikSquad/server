package net.swofty.type.generic.entity.npc.runtime;

import net.swofty.type.generic.entity.npc.HypixelNPC;

public final class NPCRuntimeService {

    private NPCRuntimeService() {
    }

    public static void tick() {
        for (HypixelNPC npc : HypixelNPC.getRegisteredNPCs()) {
            if (npc.getParameters().supportsRuntimeBehavior()) {
                npc.controller().tick();
            }
        }
    }
}
