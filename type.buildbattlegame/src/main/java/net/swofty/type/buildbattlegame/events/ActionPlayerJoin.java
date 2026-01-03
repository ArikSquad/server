package net.swofty.type.buildbattlegame.events;

import lombok.SneakyThrows;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.buildbattlegame.TypeBuildBattleGameLoader;
import net.swofty.type.buildbattlegame.game.Game;
import net.swofty.type.buildbattlegame.user.BuildBattlePlayer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.redis.service.RedisGameMessage;
import net.swofty.type.generic.utility.MathUtility;
import org.tinylog.Logger;

public class ActionPlayerJoin implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        final BuildBattlePlayer player = (BuildBattlePlayer) event.getPlayer();
        Logger.info("Player " + player.getUsername() + " joined the server from origin server " + player.getOriginServer());
        event.setSpawningInstance(HypixelConst.getEmptyInstance());
        player.setRespawnPoint(new Pos(0, 72, 0));

        MathUtility.delay(
                () -> {
                    String preferredGameId;
                    if (RedisGameMessage.game.containsKey(player.getUuid())) {
                        preferredGameId = RedisGameMessage.game.get(player.getUuid());
                        RedisGameMessage.game.remove(player.getUuid());
                    } else {
                        Logger.error("Failed to find game for player " + player.getUsername());
                        return;
                    }

                    Game preferred = TypeBuildBattleGameLoader.getGameById(preferredGameId);
                    if (preferred != null) {
                        MathUtility.delay(
                                () -> {
                                    // Check if this is a rejoin (player was disconnected from this game)
                                    if (preferred.hasDisconnectedPlayer(player.getUuid())) {
                                        preferred.rejoin(player);
                                    } else {
                                        preferred.join(player);
                                    }
                                },
                                15
                        );
                    }
                }, 15);
    }
}
