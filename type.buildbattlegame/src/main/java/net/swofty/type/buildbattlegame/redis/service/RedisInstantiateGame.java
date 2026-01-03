package net.swofty.type.buildbattlegame.redis.service;

import net.swofty.commons.buildbattle.BuildBattleGameType;
import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.buildbattlegame.TypeBuildBattleGameLoader;
import net.swofty.type.buildbattlegame.game.Game;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig;
import org.json.JSONObject;

public class RedisInstantiateGame implements ServiceToClient {
    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.INSTANTIATE_GAME;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        try {
            String gameTypeStr = message.getString("gameType");

            BuildBattleGameType gameType = BuildBattleGameType.valueOf(gameTypeStr.toUpperCase());
            Game game = TypeBuildBattleGameLoader.createGame(gameType);
            if (game == null) {
                return new JSONObject()
                        .put("success", false)
                        .put("error", "Server at capacity, cannot create new game");
            }

            return new JSONObject()
                    .put("success", true)
                    .put("gameId", game.getGameId())
                    .put("gameType", gameType.toString());

        } catch (Exception e) {
            return new JSONObject()
                    .put("success", false)
                    .put("error", "Failed to instantiate game: " + e.getMessage());
        }
    }
}
