package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.swofty.commons.Configuration;
import net.swofty.commons.ServerType;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.redis.RedisMessage;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public record TransferHandler(Player player) {
    public static final Map<Player, ServerType> playersGoalServerType = new HashMap<>();
    private static final Map<Player, RegisteredServer> playersOriginServer = new HashMap<>();
    private static final List<Player> disregard = new ArrayList<>();

    public boolean isInLimbo() {
        return playersGoalServerType.containsKey(player);
    }

    public void addToDisregard() {
        disregard.add(player);
    }

    public void removeFromDisregard() {
        disregard.remove(player);
    }

    public void standardTransferTo(RegisteredServer currentServer, ServerType type) {
        new Thread(() -> {
            RegisteredServer limboServer = SkyBlockVelocity.getLimboServer();

            player.createConnectionRequest(limboServer).connectWithIndication();
            playersGoalServerType.put(player, type);
            playersOriginServer.put(player, currentServer);
        }).start();
    }

    public CompletableFuture<Boolean> sendToLimbo() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        new Thread(() -> {
            if (player.getCurrentServer().isPresent()) {
                RegisteredServer previousServer = player.getCurrentServer().get().getServer();
                playersOriginServer.put(player, previousServer);
            }

            RegisteredServer limboServer = SkyBlockVelocity.getLimboServer();
            player.createConnectionRequest(limboServer).connectWithIndication();
            future.complete(true);
        }).start();

        return future;
    }

    public void previousServerIsFinished(RegisteredServer manualPick) {
        new Thread(() -> {
            if (disregard.contains(player)) return;

            RegisteredServer originServer = playersOriginServer.get(player);
            ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

            UUID serverUUID = UUID.fromString(manualPick.getServerInfo().getName());
            UUID originServerUUID = UUID.fromString(originServer.getServerInfo().getName());

            RedisMessage.sendMessageToServer(serverUUID,
                    FromProxyChannels.GIVE_PLAYERS_ORIGIN_TYPE,
                    new JSONObject().put("uuid", player.getUniqueId().toString())
                            .put("origin-type", originServerType.name())
            );

            playersGoalServerType.remove(player);
            playersOriginServer.remove(player);

            player.createConnectionRequest(manualPick).connectWithIndication();

            RedisMessage.sendMessageToServer(originServerUUID,
                    FromProxyChannels.PLAYER_HAS_SWITCHED_FROM_HERE,
                    new JSONObject().put("uuid", player.getUniqueId().toString()));
        }).start();
    }

    public void previousServerIsFinished() {
        new Thread(() -> {
            if (disregard.contains(player)) return;

            ServerType type = playersGoalServerType.get(player);
            GameManager.GameServer server = BalanceConfigurations.getServerFor(player, type);
            RegisteredServer originServer = playersOriginServer.get(player);
            UUID originServerUUID = UUID.fromString(originServer.getServerInfo().getName());
            UUID sendingToServerUUID = server.internalID();
            ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

            RedisMessage.sendMessageToServer(sendingToServerUUID,
                    FromProxyChannels.GIVE_PLAYERS_ORIGIN_TYPE,
                    new JSONObject().put("uuid", player.getUniqueId().toString())
                            .put("origin-type", originServerType.name())
            );

            playersOriginServer.remove(player);
            playersGoalServerType.remove(player);

            try {
                Thread.sleep(Long.parseLong(Configuration.get("transfer-timeout")));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            player.createConnectionRequest(server.registeredServer()).connectWithIndication();

            RedisMessage.sendMessageToServer(originServerUUID,
                    FromProxyChannels.PLAYER_HAS_SWITCHED_FROM_HERE,
                    new JSONObject().put("uuid", player.getUniqueId().toString()));
        }).start();
    }

    public void noLimboTransferTo(ServerType type) {
        new Thread(() -> {
            RegisteredServer originServer = playersOriginServer.get(player);
            ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

            playersGoalServerType.remove(player);
            playersOriginServer.remove(player);

            GameManager.GameServer server = BalanceConfigurations.getServerFor(player, type);

            if (originServer != null && originServerType != null) {
                RedisMessage.sendMessageToServer(server.internalID(),
                        FromProxyChannels.GIVE_PLAYERS_ORIGIN_TYPE,
                        new JSONObject().put("uuid", player.getUniqueId().toString())
                                .put("origin-type", originServerType.name())
                );
            }

            player.createConnectionRequest(server.registeredServer()).connectWithIndication();
        }).start();
    }

    public void forceRemoveFromLimbo() {
        playersGoalServerType.remove(player);
        playersOriginServer.remove(player);
    }

    public void noLimboTransferTo(RegisteredServer toTransferTo) {
        new Thread(() -> {
            RegisteredServer originServer = playersOriginServer.get(player);
            ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

            playersGoalServerType.remove(player);
            playersOriginServer.remove(player);

            ServerType type = GameManager.getTypeFromRegisteredServer(toTransferTo);
            UUID serverUUID = UUID.fromString(toTransferTo.getServerInfo().getName());

            if (originServer != null && originServerType != null) {
                RedisMessage.sendMessageToServer(serverUUID,
                        FromProxyChannels.GIVE_PLAYERS_ORIGIN_TYPE,
                        new JSONObject().put("uuid", player.getUniqueId().toString())
                                .put("origin-type", originServerType.name())
                );
            }

            player.createConnectionRequest(toTransferTo).connectWithIndication();
        }).start();
    }
}
