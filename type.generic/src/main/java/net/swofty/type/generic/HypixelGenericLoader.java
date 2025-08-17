package net.swofty.type.generic;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.commons.Configuration;
import net.swofty.commons.CustomWorlds;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.mongodb.AttributeDatabase;
import net.swofty.type.generic.data.mongodb.AuthenticationDatabase;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.entity.animalnpc.HypixelAnimalNPC;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.packet.HypixelPacketClientListener;
import net.swofty.type.generic.packet.HypixelPacketServerListener;
import net.swofty.type.generic.redis.RedisOriginServer;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public record HypixelGenericLoader(HypixelTypeLoader loader) {
    public static final AtomicReference<TickMonitor> LAST_TICK = new AtomicReference<>();

    @Getter
    private static MinecraftServer server;

    @SneakyThrows
    public void initialize(MinecraftServer server) {
        HypixelGenericLoader.server = server;
        HypixelConst.setTypeLoader(loader);
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        /**
         * Handle instances
         */
        CustomWorlds mainInstance = loader.getMainInstance();
        if (mainInstance != null) {
            InstanceContainer temporaryInstance = instanceManager.createInstanceContainer();
            temporaryInstance.setChunkLoader(new AnvilLoader(loader.getMainInstance().getFolderName()));

            HypixelConst.setInstanceContainer(instanceManager.createSharedInstance(temporaryInstance));
        }
        HypixelConst.setEmptyInstance(instanceManager.createSharedInstance(instanceManager.createInstanceContainer()));
        HypixelConst.getEmptyInstance().setBlock(0, 99, 0, Block.BEDROCK);
        HypixelConst.setEventHandler(MinecraftServer.getGlobalEventHandler());

        /**
         * Register commands
         */
        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            // Large amount of Clients (such as Lunar) send a `/tip all` when joining
            // due to the scoreboard containing `hypixel.net`
            if (command.startsWith("tip ")) return;
            sender.sendMessage("§fUnknown command. Type \"/help\" for help. ('" + command + "')");
        });
        loopThroughPackage("net.swofty.type.generic.command.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error("Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
                e.printStackTrace();
            }
        });

        /**
         * Register events
         */
        loader.getTraditionalEvents().forEach(HypixelEventHandler::registerEventMethods);
        loader.getCustomEvents().forEach(HypixelEventHandler::registerEventMethods);
        loopThroughPackage("net.swofty.type.generic.event.actions", HypixelEventClass.class).forEach(HypixelEventHandler::registerEventMethods);
        // SkyBlockGenericLoader always runs after the generic loader, so if we are a SkyBlock server,
        // we will let that loader register the events
        if (!loader.getType().isSkyBlock()) {
            HypixelEventHandler.register(HypixelConst.getEventHandler());
        }

        /**
         * Register packet events assuming we are not a SkyBlock server, if we are
         * then we will just cache the events and register them in the SkyBlockGenericLoader
         */
        loopThroughPackage("net.swofty.type.generic.packet.packets.client", HypixelPacketClientListener.class)
                .forEach(HypixelPacketClientListener::cacheListener);
        loopThroughPackage("net.swofty.type.generic.packet.packets.server", HypixelPacketServerListener.class)
                .forEach(HypixelPacketServerListener::cacheListener);
        if (!loader.getType().isSkyBlock()) {
            HypixelPacketClientListener.register(HypixelConst.getEventHandler());
            HypixelPacketServerListener.register(HypixelConst.getEventHandler());
        }

        /**
         * Start generic tablist
         * SkyBlock has its own format so let SkyBlockGenericLoader handle it
         */
        if (!loader.getType().isSkyBlock()) {
            MinecraftServer.getGlobalEventHandler().addListener(ServerTickMonitorEvent.class, event ->
                    LAST_TICK.set(event.getTickMonitor()));
            BenchmarkManager benchmarkManager = MinecraftServer.getBenchmarkManager();
            benchmarkManager.enable(Duration.ofDays(3));
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                Collection<HypixelPlayer> players = getLoadedPlayers();
                if (players.isEmpty())
                    return;

                long ramUsage = benchmarkManager.getUsedMemory();
                ramUsage /= (long) 1e6; // bytes to MB
                TickMonitor tickMonitor = LAST_TICK.get();
                double TPS = 1000 / tickMonitor.getTickTime();

                if (TPS < 20) {
                    HypixelGenericLoader.getLoadedPlayers().forEach(player -> {
                        player.getLogHandler().debug("§cServer TPS is below 20! TPS: " + TPS);
                    });
                    Logger.error("Server TPS is below 20! TPS: " + TPS);
                }

                final Component header = Component.text("§bYou are playing on §e§lMC.HYPIXEL.NET")
                        .append(Component.newline())
                        .append(Component.text("§7RAM USAGE: §8" + ramUsage + " MB"))
                        .append(Component.newline())
                        .append(Component.text("§7TPS: §8" + TPS))
                        .append(Component.newline());
                final Component footer = Component.newline()
                        .append(Component.text("§aRanks, Boosters & MORE! §c§lSTORE.HYPIXEL.NET"));
                Audiences.players().sendPlayerListHeaderAndFooter(header, footer);
            }).repeat(10, TimeUnit.SERVER_TICK).schedule();
        }

        /**
         * Start Tablist loop
         */
        loader.getTablistManager().runScheduler(MinecraftServer.getSchedulerManager());

        /**
         * Register databases
         */
        ConnectionString cs = new ConnectionString(Configuration.get("mongodb"));
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        MongoClient mongoClient = MongoClients.create(settings);

        AuthenticationDatabase.connect(mongoClient);
        ProfilesDatabase.connect(mongoClient);
        AttributeDatabase.connect(mongoClient);
        UserDatabase.connect(mongoClient);

        /**
         * Register Hypixel NPCs
         */
        if (mainInstance != null) {
            loader.getNPCs().forEach(HypixelNPC::register);
            loader.getVillagerNPCs().forEach(HypixelVillagerNPC::register);
            loader.getAnimalNPCs().forEach(HypixelAnimalNPC::register);
        }

        /**
         * Register player provider given we aren't a SkyBlock server
         * If we are a SkyBlock server, we will handle the player provider in the SkyBlockGenericLoader
         */
        if (!loader.getType().isSkyBlock()) {
            /**
             * Handle ConnectionManager
             */
            MinecraftServer.getConnectionManager().setPlayerProvider((playerConnection, gameProfile) -> {
                HypixelPlayer player = new HypixelPlayer(playerConnection, gameProfile);

                UUID uuid = playerConnection.getPlayer().getUuid();
                String username = playerConnection.getPlayer().getUsername();

                if (RedisOriginServer.origin.containsKey(uuid)) {
                    player.setOriginServer(RedisOriginServer.origin.get(uuid));
                    RedisOriginServer.origin.remove(uuid);
                }

                Logger.info("Received new player: " + username + " (" + uuid + ")");
                return player;
            });
        }
    }

    public static List<HypixelPlayer> getLoadedPlayers() {
        List<HypixelPlayer> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .filter(player -> {
                    try {
                        HypixelDataHandler.getUser(player);
                    } catch (Exception e) {
                        return false;
                    }
                    return true;
                })
                .filter(player -> player.getInstance() != null)
                .forEach(player -> players.add((HypixelPlayer) player));
        return players;
    }

    public static @Nullable HypixelPlayer getFromUUID(UUID uuid) {
        return getLoadedPlayers().stream().filter(player -> player.getUuid().toString().equalsIgnoreCase(uuid.toString())).findFirst().orElse(null);
    }

    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        return clazz.cast(subClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull);
    }
}
