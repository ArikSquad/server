package net.swofty.type.generic.gui.inventory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class HypixelPaginatedGUI<T> extends HypixelInventoryGUI {

    protected PaginationList<T> latestPaged;

    protected HypixelPaginatedGUI(InventoryType size) {
        super("", size);
    }

    protected abstract int[] getPaginatedSlots();

    protected abstract PaginationList<T> fillPaged(HypixelPlayer player, PaginationList<T> paged);

    protected abstract boolean shouldFilterFromSearch(String query, T item);

    protected abstract void performSearch(HypixelPlayer player, String query, int page, int maxPage);

    protected abstract String getTitle(HypixelPlayer player, String query, int page, PaginationList<T> paged);

    protected abstract GUIClickableItem createItemFor(T item, int slot, HypixelPlayer player);

    @Override
    public void open(HypixelPlayer player) {
        open(player, "", 1);
    }

    public void open(HypixelPlayer player, String query, int page) {
        Thread.startVirtualThread(() -> {
            PaginationList<T> paged = fillPaged(player, new PaginationList<>(getPaginatedSlots().length));

            if (!query.isEmpty()) {
                paged.removeIf(type -> shouldFilterFromSearch(query, type));
            }

            this.title = getTitle(player, query, page, paged);
            latestPaged = paged;

            try {
                updatePagedItems(paged, page, player);
            } catch (IllegalStateException ex) {}
            performSearch(player, query, page, paged.getPageCount());
            super.open(player);
        });
    }

    protected void updatePagedItems(PaginationList<?> paged, int page, HypixelPlayer player) {
        List<?> thisPage = paged.getPage(page);
        if (thisPage == null) throw new IllegalStateException();
        Arrays.stream(getPaginatedSlots()).forEach(slot -> set(new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStack.builder(Material.AIR);
            }
        }));

        for (int i = 0; i < thisPage.size(); i++) {
            set(createItemFor((T) thisPage.get(i), getPaginatedSlots()[i], player));
        }
    }

    public static GUIQueryItem createSearchItem(HypixelPaginatedGUI<?> gui, int slot, String search) {
        return new GUIQueryItem(slot) {

            @Override
            public HypixelInventoryGUI onQueryFinish(String query, HypixelPlayer player) {
                gui.items.clear();
                gui.performSearch(player, query, 1, gui.latestPaged.getPageCount());
                MinecraftServer.getSchedulerManager().scheduleTask(() -> gui.open(player, query, 1), TaskSchedule.tick(1), TaskSchedule.stop());
                return null;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aSearch", Material.BIRCH_SIGN, 1, "§7Query: §e" +
                        (Objects.equals(search, "") ? "None" : search));
            }
        };
    }

    public static GUIClickableItem createNavigationButton(HypixelPaginatedGUI<?> gui, int slot, String search, int page, boolean forward) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                int newPage = forward ? page + 1 : page - 1;
                gui.open(player, search, newPage);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String name = forward ? "§aNext Page" : "§aPrevious Page";
                String lore = forward ? "§ePage " + (page + 1) : "§ePage " + (page - 1);
                return ItemStackCreator.getStack(name, Material.ARROW, 1, lore);
            }
        };
    }

}
