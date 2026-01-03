package net.swofty.type.buildbattlegame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.hollowcube.polar.PolarLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.buildbattle.BuildBattleGameType;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.orchestrator.GameHeartbeatProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.buildbattlegame.game.Game;
import net.swofty.type.buildbattlegame.item.SimpleInteractableItem;
import net.swofty.type.buildbattlegame.item.SimpleInteractableItemHandler;
import net.swofty.type.buildbattlegame.user.BuildBattlePlayer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.handlers.BuildBattleDataHandler;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.HypixelTypeLoader;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.redis.RedisOriginServer;
import net.swofty.type.generic.tab.EmptyTabModule;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static net.swofty.type.generic.HypixelGenericLoader.getLoadedPlayers;

public class TypeBuildBattleGameLoader implements HypixelTypeLoader {

    public static final int MAX_GAMES = 8;

    @Getter
    public static final List<Game> games = new ArrayList<>();

    @Getter
    public static final SimpleInteractableItemHandler itemHandler = new SimpleInteractableItemHandler();

    private static InstanceManager instanceManager;
    private static RegistryKey<@NotNull DimensionType> fullbrightDimension;

    public static Game getGameById(@NotNull String gameId) {
        return games.stream()
                .filter(game -> game.getGameId().equals(gameId))
                .findFirst()
                .orElse(null);
    }

    public static Game getPlayerGame(@NotNull Player player) {
        String gameId = player.getTag(Tag.String("gameId"));
        if (gameId == null) return null;
        return getGameById(gameId);
    }

    @SneakyThrows
    public static Game createGame(BuildBattleGameType gameType) {
        if (games.size() >= MAX_GAMES) {
            return null;
        }
        InstanceContainer mapInstance = instanceManager.createInstanceContainer(fullbrightDimension);
        mapInstance.setChunkLoader(new PolarLoader(new File("./configuration/buildbattle/1.polar").toPath()));
        Game game = new Game(mapInstance, gameType);
        games.add(game);
        return game;
    }

    private static Component header() {
        return MiniMessage.miniMessage().deserialize("<aqua>You are playing on <bold><yellow>MC.HYPIXEL.NET</yellow></bold>");
    }

    private static Component footer(HypixelPlayer player) {
        return Component.empty()
                .append(Component.text("§aRanks, Boosters & MORE! §c§lSTORE.HYPIXEL.NET"));
    }

    @Override
    public ServerType getType() {
        return ServerType.BUILD_BATTLE_GAME;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        instanceManager = MinecraftServer.getInstanceManager();
        fullbrightDimension = MinecraftServer.getDimensionTypeRegistry().register("fullbright", DimensionType.builder().ambientLight(0.9f).build());

        MinecraftServer.getConnectionManager().setPlayerProvider((playerConnection, gameProfile) -> {
            BuildBattlePlayer player = new BuildBattlePlayer(playerConnection, gameProfile);

            UUID uuid = gameProfile.uuid();
            String username = gameProfile.name();

            if (RedisOriginServer.origin.containsKey(uuid)) {
                player.setOriginServer(RedisOriginServer.origin.get(uuid));
                RedisOriginServer.origin.remove(uuid);
            }

            Logger.info("Received new player: " + username + " (" + uuid + ")");

            return player;
        });
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.buildbattlegame.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });
        BuildBattleGameScoreboard.start();
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.buildbattlegame.item.impl", SimpleInteractableItem.class).forEach(itemHandler::add);

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            UUID uuid = HypixelConst.getServerUUID();
            String shortName = HypixelConst.getShortenedServerName();
            int maxPlayers = HypixelConst.getMaxPlayers();
            int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().size();

            List<net.swofty.commons.game.Game> commonsGames = new ArrayList<>();
            for (Game internalGame : TypeBuildBattleGameLoader.getGames()) {
                net.swofty.commons.game.Game commonsGame = new net.swofty.commons.game.Game();
                commonsGame.setGameId(UUID.fromString(internalGame.getGameId()));
                commonsGame.setType(ServerType.BUILD_BATTLE_GAME);
                commonsGame.setGameTypeName(internalGame.getGameType().name());

                List<UUID> playerUuids = new ArrayList<>();
                for (BuildBattlePlayer player : internalGame.getPlayers()) {
                    playerUuids.add(player.getUuid());
                }
                commonsGame.setInvolvedPlayers(playerUuids);
                commonsGames.add(commonsGame);
            }

            var heartbeat = new GameHeartbeatProtocolObject.HeartbeatMessage(
                    uuid,
                    shortName,
                    getType(),
                    maxPlayers,
                    onlinePlayers,
                    commonsGames
            );
            new ProxyService(ServiceType.ORCHESTRATOR).handleRequest(heartbeat);
        }).delay(TaskSchedule.seconds(5)).repeat(TaskSchedule.seconds(1)).schedule();

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            Collection<HypixelPlayer> players = getLoadedPlayers();
            if (players.isEmpty())
                return;
            for (HypixelPlayer player : players) {
                player.sendPlayerListHeaderAndFooter(header(), footer(player));
            }
        }).repeat(10, TimeUnit.SERVER_TICK).schedule();
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.ORCHESTRATOR);
    }

    @Override
    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return List.of(
                        new EmptyTabModule(),
                        new EmptyTabModule(),
                        new EmptyTabModule(),
                        new EmptyTabModule()
                );
            }
        };
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (_) -> new Pos(0, 72, 0, 0, 0),
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.buildbattlegame.events",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.buildbattlegame.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.buildbattlegame.npcs",
                HypixelNPC.class
        ).toList();
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.buildbattlegame.redis.service",
                ServiceToClient.class
        ).toList();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.buildbattlegame.redis",
                ProxyToClient.class
        ).toList();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(BuildBattleDataHandler.class);
    }
}
