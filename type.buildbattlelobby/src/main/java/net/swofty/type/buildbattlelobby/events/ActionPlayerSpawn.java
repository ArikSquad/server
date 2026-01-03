package net.swofty.type.buildbattlelobby.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.buildbattlelobby.util.BuildBattleLobbyMap;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        BuildBattleLobbyMap buildBattleLobbyMap = new BuildBattleLobbyMap();
        buildBattleLobbyMap.sendMapData(player);
    }
}
