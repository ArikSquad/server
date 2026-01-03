package net.swofty.type.buildbattlelobby.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.buildbattle.BuildBattleGameType;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.GameCountCache;

public class NPCSpeedBuilders extends HypixelNPC {

    public NPCSpeedBuilders() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                int amountOnline = GameCountCache.getPlayerCount(
                        ServerType.BUILD_BATTLE_GAME,
                        BuildBattleGameType.SPEED_BUILDERS.name()
                );

                String commaified = StringUtility.commaify(amountOnline);
                return new String[]{
                        "§e§lCLICK TO PLAY",
                        "§bSpeed Builders",
                        "§e§l" + commaified + " Players"
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "upSTrpUN5tZOx1FM3jq4z/0M/OHOQNRivE9eTGckriC4UEZ4NCqwfcHMZrZvxec+Pwo7/2wu+3IyFd6h41sapoQH2ypyxLXO0YlfPcImjSaHTTcygnt6+bQbTbW0Hr4mDccEMnpB1gZAtsLkT3wUtwF35g/u2W2w7dclbpQ4M0vACMjpYbGLpchvRTNipFpRq5NKOixpLlE4PdOkJa13ptFKbWnvV89Mo8+vgKrCy0A+Mxnt+G2C60xnpe6QLKbev0euSfTKtUyNuhPljJcOsoNI06gDk6pHAZHSn4RQyTxAuGllDZnO8FrA71dlCZ5nNTLfQg+WqWGzH1ahJQ07HPxF9bQjQkyMCq79Wt2oE49jK1tiIU4jYQVOBhSQ8KDrNJnJAJXNKYzWCrawcBc4wjL9x7VMjRTZQ4k87hDm4CuLq7GQIsXbjuXL/yoB4dw/1yx+1t44elNoeW9xyBRQvtwBfPC2TocqvR6Kp+CATvkY8u6Vk3VJ7a9LLLd9wyva/Q7NFCtz0DpPmMTxU9iuUsaBDPP9CHYNyiwhUxdDa84ctJ3irbiWjAUXhZT20kRse/tVY/9jNmG4AmJv0B5nUojl3bNzB98mExRLVJabeYGBeVC9eQag/tAmSHaZlLXtTaHBKg6FLobcCQQde0eu1HPOcvLthp9Tb7rIM5h5azs=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTcyMjU2MTAwMzMwNSwKICAicHJvZmlsZUlkIiA6ICI0MmIwOTMyZDUwMWI0MWQ1YTM4YjEwOTcxYTYwYmYxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJBaXJib2x0MDc4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzhkMDMzMTA2ZjE3NjUwZDNkYzRhNjUxMjQ0MmZiZWM1NDA3NzJmNmFhMmEzOTlkODJmZDdkYjI3NDhkNjU5OWQiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(27, 68, 0.5, 90, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return false;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        e.player().sendTo(ServerType.BEDWARS_LOBBY);
    }
}
