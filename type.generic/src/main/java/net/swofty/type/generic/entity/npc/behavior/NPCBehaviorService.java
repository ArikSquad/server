package net.swofty.type.generic.entity.npc.behavior;

import net.swofty.type.generic.entity.npc.HypixelNPC;

public final class NPCBehaviorService {

    private NPCBehaviorService() {
    }

    public static void tick() {
        for (HypixelNPC npc : HypixelNPC.getRegisteredNPCs()) {
            if (npc.getParameters().supportsBehavior()) {
                npc.behaviorController().tick();
            }
        }
    }
}
