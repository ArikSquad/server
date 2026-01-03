package net.swofty.type.buildbattlegame.item.impl;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.buildbattlegame.TypeBuildBattleGameLoader;
import net.swofty.type.buildbattlegame.game.Game;
import net.swofty.type.buildbattlegame.user.BuildBattlePlayer;
import net.swofty.type.buildbattlegame.item.SimpleInteractableItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class LeaveGameBed extends SimpleInteractableItem {

    public LeaveGameBed() {
        super("leave_game");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStackCreator.getStack("§c§lReturn to Lobby §7(Right Click)", Material.RED_BED, 1, "§7Right-click to leave to the lobby!").build();
    }

    @Override
    public void onItemUse(PlayerUseItemEvent event) {
        Game game = TypeBuildBattleGameLoader.getPlayerGame(event.getPlayer());
        if (game != null) {
            game.leave((BuildBattlePlayer) event.getPlayer());
        }
    }
}
