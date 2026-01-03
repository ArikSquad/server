package net.swofty.type.buildbattlegame.events;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.type.buildbattlegame.TypeBuildBattleGameLoader;
import net.swofty.type.buildbattlegame.game.Game;
import net.swofty.type.buildbattlegame.user.BuildBattlePlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPreventBlockModification implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onBlockBreak(PlayerBlockBreakEvent event) {
        if (!(event.getPlayer() instanceof BuildBattlePlayer player)) return;

        // Allow in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Game game = TypeBuildBattleGameLoader.getPlayerGame(player);
        if (game != null) {
            event.setCancelled(true);
        }
    }

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onBlockPlace(PlayerBlockPlaceEvent event) {
        if (!(event.getPlayer() instanceof BuildBattlePlayer player)) return;

        // Allow in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Game game = TypeBuildBattleGameLoader.getPlayerGame(player);
        if (game != null) {
            event.setCancelled(true);
        }
    }
}
