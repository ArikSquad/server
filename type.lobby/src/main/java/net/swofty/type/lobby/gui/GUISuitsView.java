package net.swofty.type.lobby.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.cosmetic.PlayerCosmeticData;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointCosmeticData;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.PaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.cosmetics.LobbySuitData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUISuitsView extends PaginatedView<LobbySuitData, GUISuitsView.SuitsState> {

    private static final int[] PAGINATED_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<SuitsState> configuration() {
        return ViewConfiguration.withString(
            (state, _) -> "Suits - Page " + (state.page() + 1),
            InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(LobbySuitData suit, int index, HypixelPlayer player) {
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        boolean owned = data.owns("SUITS", suit.name());
        boolean active = data.isActive("SUITS", suit.name());
        List<String> lore = new ArrayList<>();

        lore.add("§7Full Set Ability: §a" + suit.getFullSetAbility());
        lore.add("");

        if (owned) {
            if (active) {
                lore.add("§aCurrently equipped!");
                lore.add("§eClick to unequip!");
            } else {
                lore.add("§eClick to equip!");
            }
        } else {
            lore.add("§7" + suit.getCostDisplay());
        }

        Material mat = owned ? suit.getDisplayMaterial() : Material.GRAY_DYE;
        return ItemStackCreator.getStack(
            suit.getRarity().getColorCode() + suit.getDisplayName(),
            mat, 1,
            lore.toArray(new String[0])
        );
    }

    @Override
    protected void onItemClick(ClickContext<SuitsState> click, ViewContext ctx, LobbySuitData suit, int index) {
        HypixelPlayer player = ctx.player();
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        if (!data.owns("SUITS", suit.name())) {
            return;
        }

        if (data.isActive("SUITS", suit.name())) {
            data.deactivate("SUITS");
            player.sendMessage(Component.text("§cSuit unequipped!"));
        } else {
            data.activate("SUITS", suit.name());
            player.sendMessage(Component.text("§aEquipped " + suit.getRarity().getColorCode() + suit.getDisplayName() + "§a!"));
        }
        player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);
        ctx.session(Object.class).refresh();
    }

    @Override
    protected boolean shouldFilterFromSearch(SuitsState state, LobbySuitData item) {
        if (state.filter.isEmpty()) return false;
        return !item.getDisplayName().toLowerCase().contains(state.filter.toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<SuitsState> layout, SuitsState state, ViewContext ctx) {
        Components.close(layout, 49);
        if (!Components.back(layout, 48, ctx)) {
            layout.slot(48, FILLER);
        }
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    public static SuitsState createInitialState() {
        return new SuitsState(Arrays.asList(LobbySuitData.values()), 0, "");
    }

    public record SuitsState(List<LobbySuitData> items, int page,
                             String filter) implements PaginatedState<LobbySuitData> {
        @Override
        public PaginatedState<LobbySuitData> withPage(int page) {
            return new SuitsState(items, page, filter);
        }

        @Override
        public PaginatedState<LobbySuitData> withItems(List<LobbySuitData> items) {
            return new SuitsState(items, page, filter);
        }
    }
}
