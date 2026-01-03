package net.swofty.type.buildbattlegame.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.buildbattlegame.TypeBuildBattleGameLoader;
import net.swofty.type.buildbattlegame.game.Game;
import net.swofty.type.buildbattlegame.user.BuildBattlePlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPlayerDisconnect implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerDisconnectEvent event) {
        BuildBattlePlayer player = (BuildBattlePlayer) event.getPlayer();

        Game game = TypeBuildBattleGameLoader.getPlayerGame(player);
        if (game != null) {
            game.disconnect(player);
        }
    }
}
