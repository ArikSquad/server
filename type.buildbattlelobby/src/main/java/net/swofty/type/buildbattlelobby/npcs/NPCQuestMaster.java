package net.swofty.type.buildbattlelobby.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.achievement.AchievementCategory;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.gui.GUIGameQuests;

public class NPCQuestMaster extends HypixelNPC {

    public NPCQuestMaster() {
        super(new VillagerConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{
                        "§bQuest Master",
                        "§e§lRIGHT CLICK",
                };
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(9, 67, 5.5, 115, -10);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return false;
            }

            @Override
            public VillagerProfession profession() {
                return VillagerProfession.LIBRARIAN;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        new GUIGameQuests(AchievementCategory.BUILD_BATTLE).open(event.player());
    }
}
