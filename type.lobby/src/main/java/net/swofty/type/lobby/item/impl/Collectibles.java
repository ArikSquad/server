package net.swofty.type.lobby.item.impl;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.gui.GUICollectiblesView;
import net.swofty.type.lobby.item.LobbyItem;

import java.util.function.Consumer;

public class Collectibles extends LobbyItem {

    private final Consumer<HypixelPlayer> onInteract;

    public Collectibles(Consumer<HypixelPlayer> onInteract) {
        super("collectibles");
        this.onInteract = onInteract;
    }

    public Collectibles() {
        this(player -> player.openView(new GUICollectiblesView()));
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStackCreator.getStack(
            "§aCollectibles §7(Right Click)",
            Material.CHEST,
            1,
            "§7Mystery Dust: §b0",
            "",
            "§7Collect fun cosmetic items! Unlock new items",
            "§7using §bMurdery Dust §7or hitting milestone",
            "§7rewards.",
            "",
            "§bMystery Dust §7is randomly given after playing",
            "§7games."
        ).build();
    }

    @Override
    public void onItemDrop(ItemDropEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        if (event instanceof CancellableEvent cancellable) {
            cancellable.setCancelled(true);
        }
        onInteract.accept((HypixelPlayer) event.getPlayer());
    }
}
