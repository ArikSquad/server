package net.swofty.type.buildbattlelobby.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.buildbattle.BuildBattleGameType;
import net.swofty.proxyapi.ProxyInformation;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.utility.GameCountCache;

public class NPCSoloMode extends HypixelNPC {

    public NPCSoloMode() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                int amountOnline = GameCountCache.getPlayerCount(
                        ServerType.BUILD_BATTLE_GAME,
                        BuildBattleGameType.SOLO_MODE.name()
                );

                String commaified = StringUtility.commaify(amountOnline);
                return new String[]{
                        "§e§lCLICK TO PLAY",
                        "§bSolo Mode",
                        "§e§l" + commaified + " Players"
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "q+SigjPrEtKesr/szWqXWuA0Scs0NgEhDCtp1VRXB0a5uxOkdo4YgFRsP9wLPyaeQPXWI5FQOxbtOy6lxHLpTArbQgIzzdoyZ72mhyuXH+w924+x8+zUWhKiPkA+zTCxn8T/SYulQ0ptigs+vxTFkWyNp+I2QkzgB6fnmh2/IHRKP42PyIiLfPsLh9cTBm7ekXSrCUcSXlbX0BRNPmkpWv8KLx3/iK0diZB+oz0eBO13JHLKITO/unjLMRKt9LC3q1L3WSPFFjOJzN5cJmD6EwO5ANWhERHruwF+UlrZDm1tcOLXGoqyYo1p+gmy2Ims13H1kPJG3807jtnrmXOXrgkQTfddBpCm2gi593hnpsvBoNfAfmjE+CkdhnE7xlCwD5I6SWUrYGZ6kFgGoxTuv31zAVUKEGCMryKgch7AEX8OYCDfvmncpnqkHCkrxlVNE9fQ4/iI20R77Muw+iOTcQTQOU4KMI48w6O5tlfpZjm2vtJlaW2vaN2zmba1R2v/ncMi1p2LidZcwfEe5EV15WAOkHSo3nEAvMprV6uiLbRTXOK1RZmNQPFVIJ7Gb+5q9X0Nv9S4OH/U/SiN83VKy9KCUhZ7IemL5epT/pP1pjbhTMuxVmQlfHp7qwUxLMbRwP/P8UWjPq3nhnB3cnQ763Dr+2+OOpYoTbmAqtLmNqM=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1MDAwMDQ3OTM5MjMsInByb2ZpbGVJZCI6IjkzYzdmMmUxMTg2MzQ5NzU4OGE2ZWI0YzUwYjRhZGZiIiwicHJvZmlsZU5hbWUiOiJUYWN0ZnVsIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80OTQ1ZDA2NDM4ZjdmZmYyOTFjY2QxNWIxNTZlM2ZiODcyYmMyNWNkOGUyMmE1Zjc2NWM3MDNlYjMzOWY4In19fQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(25.5, 68, 8.5, 110, 0);
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
