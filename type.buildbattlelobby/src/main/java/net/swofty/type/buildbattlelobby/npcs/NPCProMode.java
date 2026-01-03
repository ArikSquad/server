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

public class NPCProMode extends HypixelNPC {

    public NPCProMode() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                int amountOnline = GameCountCache.getPlayerCount(
                        ServerType.BUILD_BATTLE_GAME,
                        BuildBattleGameType.PRO_MODE.name()
                );

                String commaified = StringUtility.commaify(amountOnline);
                return new String[]{
                        "§e§lCLICK TO PLAY",
                        "§bPro Mode",
                        "§e§l" + commaified + " Players"
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "x4ELWKGA1vmw4xLaBORu99Z1H5Eultg5IEfvzzz0LwaQUeh4St4GUg9oZrrbdRHtl+1X26gx3FXXL/tCE++DWuvgClw1yDsx/A1K8EY02Q+x0XA0hf7a5fOHMrBlAG56xdpSJHTZqhyd+VDxDwgrlaHXAkDwWz18xb9kMD6855Upks/Vr7Y8k4gKLtSB1QUGiH3w6tbTelPzGBVDOAMa//UJ6gBoAWKOwM4dadpX/YW/ApRIiFsD+FlV3Qhg3CMsGtl9eqkw6jl8z5tgyve3FZXSbzMRsc+QjqgeTmtUqtfBkZpblBuLHODjOLVpVIhfCwTbxHXn2SLtW4V96/B3kVpFBaYKNOt3Bi99LE41nzUX/HfhOJCKE4yqqsCqOOA0AAkENwMI8g7M92wHV8RMwx+4+KEERElYg3JlWlxd6+9ziW9zN5j8p+9Pvng5J8ZVTZOjpc1sU1QOOlSKxS+m4T0gy9MiTfCzjlWtkA6nc0oxPEUUnpQveo2tCF0v+6biFzH+8mN0CuUo2bRs2jfAWRygf945kuehwmTk9RdNDNkI32Wv9ePvd4IdzYpxc/+0FWSO2BBps/qvIUJ9mFe3nOgDzmFgK2Gjqo0L2i/jSCCxbrim7FzXHdD1yBfPXzwG7k8g1H44TJBpUmM4uiyrEW/KtKH60y+Ik9VyAkWnXZU=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1MDAwMDQ5MTcxOTAsInByb2ZpbGVJZCI6IjkzYzdmMmUxMTg2MzQ5NzU4OGE2ZWI0YzUwYjRhZGZiIiwicHJvZmlsZU5hbWUiOiJUYWN0ZnVsIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMzUxOTlkZjZkNWViMTZiZGQ5Mjg5YzBiYTUxM2Y5MjdmNjhjNmFiYTJiMjA1NmEyYTYyZDY2YjkzZDdjNmUifX19===";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(25.5, 68, -7.5, 75, 0);
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
