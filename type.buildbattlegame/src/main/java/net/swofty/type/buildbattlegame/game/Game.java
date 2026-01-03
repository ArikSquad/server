package net.swofty.type.buildbattlegame.game;

import lombok.Getter;
import lombok.Setter;
import net.hollowcube.polar.PolarLoader;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.commons.buildbattle.BuildBattleGameType;
import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.murdermystery.MurderMysteryLeaderboardMode;
import net.swofty.type.generic.data.datapoints.DatapointMurderMysteryModeStats;
import net.swofty.type.generic.data.handlers.MurderMysteryDataHandler;
import net.swofty.type.buildbattlegame.TypeBuildBattleGameLoader;
import net.swofty.type.buildbattlegame.user.BuildBattlePlayer;

import java.io.File;
import java.util.*;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

@Getter
public class Game {
    private final InstanceContainer instanceContainer;
    private final BuildBattleGameType gameType;
    private final String gameId = UUID.randomUUID().toString();

    private final List<BuildBattlePlayer> players = new ArrayList<>();
    private final GameCountdown countdown;

    @Setter
    private GameStatus gameStatus;
    private long gameStartTime = 0;

    public Game(InstanceContainer instanceContainer,
                BuildBattleGameType gameType) {
        this.instanceContainer = instanceContainer;
        this.gameType = gameType;
        this.countdown = new GameCountdown(this);

        this.gameStatus = GameStatus.WAITING;
    }

    public void join(BuildBattlePlayer player) {
        if (gameStatus != GameStatus.WAITING) {
            player.sendMessage(Component.text("Game already in progress!", NamedTextColor.RED));
            player.sendTo(ServerType.BUILD_BATTLE_LOBBY);
            return;
        }

        if (players.size() >= gameType.getMaxPlayers()) {
            player.sendMessage(Component.text("Game is full!", NamedTextColor.RED));
            player.sendTo(ServerType.BUILD_BATTLE_LOBBY);
            return;
        }

        setupPlayerForWaiting(player);
        players.add(player);
        player.setTag(Tag.String("gameId"), gameId);

        broadcastMessage(Component.empty()
                .append(Component.text(player.getFullDisplayName()))
                .append(Component.text(" has joined ", NamedTextColor.YELLOW))
                .append(Component.text("(", NamedTextColor.YELLOW))
                .append(Component.text(players.size(), NamedTextColor.AQUA))
                .append(Component.text("/", NamedTextColor.YELLOW))
                .append(Component.text(gameType.getMaxPlayers(), NamedTextColor.AQUA))
                .append(Component.text(")!", NamedTextColor.YELLOW)));

        if (hasMinimumPlayers() && !countdown.isActive()) {
            countdown.startCountdown();
        }
    }

    public void leave(BuildBattlePlayer player) {
        players.remove(player);
        player.removeTag(Tag.String("gameId"));
        player.sendTo(ServerType.BUILD_BATTLE_LOBBY);

        countdown.checkCountdownConditions();
    }

    public void disconnect(BuildBattlePlayer player) {
        players.remove(player);

        if (gameStatus == GameStatus.IN_PROGRESS) {
            // TODO: skip player
        }
    }

    public void startGame() {
        gameStatus = GameStatus.IN_PROGRESS;
        gameStartTime = System.currentTimeMillis();

        startOutsideOfArea();
    }

    private void startOutsideOfArea() {

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (gameStatus != GameStatus.IN_PROGRESS) return;

            List<BuildBattlePlayer> playersToCheck = new ArrayList<>(players);
            for (BuildBattlePlayer player : playersToCheck) {

            }
        }).repeat(TaskSchedule.tick(5)).schedule();
    }

    private void setupPlayerForWaiting(BuildBattlePlayer player) {
        Pos waitingPos = getWaitingPosition();

        if (player.getInstance() == null || !player.getInstance().getUuid().equals(instanceContainer.getUuid())) {
            player.setInstance(instanceContainer, waitingPos);
        } else {
            player.teleport(waitingPos);
        }

        player.getInventory().clear();
        player.getInventory().setItemStack(8,
                TypeBuildBattleGameLoader.getItemHandler().getItem("leave_game").getItemStack());
        player.setFlying(false);
        player.setGameMode(GameMode.ADVENTURE);
    }

    private Pos getWaitingPosition() {
        return new Pos(2216.5, 5, 18.5);
    }

    private void setupPlayerForSpectator(BuildBattlePlayer player) {
        player.getInventory().clear();
        player.getInventory().setItemStack(7,
                TypeBuildBattleGameLoader.getItemHandler().getItem("play_again").getItemStack());
        player.getInventory().setItemStack(8,
                TypeBuildBattleGameLoader.getItemHandler().getItem("leave_game").getItemStack());
        for (BuildBattlePlayer otherPlayer : players) {
            if (!otherPlayer.equals(player)) {
                player.removeViewer(otherPlayer);
                player.updateOldViewer(otherPlayer);
            }
        }

        // Allow flying
        player.setAllowFlying(true);
        player.setFlying(true);
    }

    private void endGame() {
        gameStatus = GameStatus.ENDING;
    }

    private boolean hasMinimumPlayers() {
        return players.size() >= gameType.getMinPlayers();
    }

    public void forceStart() {
        forceStart(5);
    }

    public void forceStart(int seconds) {
        if (gameStatus != GameStatus.WAITING) return;
        countdown.forceStart(seconds);
    }

    public Audience getPlayersAsAudience() {
        return Audience.audience(players);
    }

    private void broadcastMessage(Component message) {
        getPlayersAsAudience().sendMessage(message);
    }

    @lombok.SneakyThrows
    private void resetInstance() {
        for (Entity entity : instanceContainer.getEntities()) {
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        }

        PolarLoader loader = new PolarLoader(new File("./configuration/buildbattle/1.polar").toPath());
        instanceContainer.setChunkLoader(loader);
        instanceContainer.getChunks().forEach(instanceContainer::unloadChunk);
    }

    public boolean canAcceptNewPlayers() {
        return gameStatus == GameStatus.WAITING;
    }

    public int getAvailableSlots() {
        return Math.max(0, gameType.getMaxPlayers() - players.size());
    }

    public String canAcceptPartyWarp() {
        if (gameStatus == GameStatus.IN_PROGRESS) {
            return "Cannot warp - game has already started";
        }
        if (gameStatus == GameStatus.ENDING) {
            return "Cannot warp - game is ending";
        }
        return null; // Warp is allowed
    }
}
