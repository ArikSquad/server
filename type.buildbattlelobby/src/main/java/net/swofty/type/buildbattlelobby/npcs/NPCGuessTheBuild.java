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

public class NPCGuessTheBuild extends HypixelNPC {

    public NPCGuessTheBuild() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                int amountOnline = GameCountCache.getPlayerCount(
                        ServerType.BUILD_BATTLE_GAME,
                        BuildBattleGameType.GUESS_THE_BUILD.name()
                );

                String commaified = StringUtility.commaify(amountOnline);
                return new String[]{
                        "§e§lCLICK TO PLAY",
                        "§bGuess The Build",
                        "§e§l" + commaified + " Players"
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "JFrZ+h2ouFyhLhFWgb//TQU6aLL+IKCR9flLyar7Iy7gyGdHYQSji6A8Uthy2uab9w3lHm49GNYQwcdsmSf0183MysW0AhoAxWWyC5SKSVWSs71SIiduKkz6X4T0veEH6xtFTScWR3jI9s5nkV1en/gc1WBajW/ZRSJ5OvE0Xmap6uHenSB6nzvSvK8vFAFM4d6SOVYHcVh/tAaLvPkm+AzD1JeRTqJO50fXeOUeM+HjZC62/Eb7T4Nha7PlvlyvjPfhKIGKx8IXZ6MEdS/EfowkkoNXyOKe0u9X+E1TezF3XexssAiNbP20+24O29pGefvnySxBF6vrHrO3uvooYBzZ6mMueJIvf+v4w+Uu9ACywVRJE2ERoT59JQ+WsGYecVJaWQiOwlb6mLTjgO6ekfx7ujbsozppvcLcDI53HrS61VRIX4bJZiPwmStBlhHcIl9f6B1vkQpOKTyPbxpgzpSlt0Tl33Lnl0U5MieI1L/fR6JfJU0TfkSC8DkD46ApZaWv1J0mwj+7FyosWf8mcGHUSeHZ9E6Xn3rLjwHYf+yB92upt4Knsu7fIN2XX/6BXRt+IISOa0SDOYj9lIkCn/F+jSI9gx/dwWpQd3EdgR8ycJ9b+cUqxZBJv56viEkf6MbdQ5k6G7jyUC+e5NZJJmLsqqmLvkjR8e/JlYz96Ms=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1MDAwMDQ2MDIxNDYsInByb2ZpbGVJZCI6IjkzYzdmMmUxMTg2MzQ5NzU4OGE2ZWI0YzUwYjRhZGZiIiwicHJvZmlsZU5hbWUiOiJUYWN0ZnVsIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hNGY3ZDBkMDgyYjZmMTFiZjgyMmUwNWJlNTdiMzQ2MDE3MmZmZWQwODIwNDJhNmFjMGY4MmY3ZWJlNWQyNyJ9fX0===";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(26, 68, 4.5, 95, 0);
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
