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
import net.swofty.type.lobby.cosmetics.LobbyGadgetData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIGadgetsView extends PaginatedView<LobbyGadgetData, GUIGadgetsView.GadgetsState> {

    private static final int[] PAGINATED_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<GadgetsState> configuration() {
        return ViewConfiguration.withString(
            (state, _) -> "Gadgets - Page " + (state.page() + 1),
            InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(LobbyGadgetData gadget, int index, HypixelPlayer player) {
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        boolean owned = data.owns("GADGETS", gadget.name());
        boolean active = data.isActive("GADGETS", gadget.name());
        List<String> lore = new ArrayList<>();

        lore.add("§7" + gadget.getDescription());
        lore.add("");

        if (owned) {
            if (active) {
                lore.add("§aCurrently selected!");
                lore.add("§eClick to deselect!");
            } else {
                lore.add("§eClick to select!");
            }
        } else {
            lore.add("§7" + gadget.getCostDisplay());
            if (gadget.getDustCost() > 0) {
                lore.add("");
                lore.add("§eClick to craft for §b" + gadget.getDustCost() + " §eMystery Dust!");
            }
        }

        Material mat = owned ? gadget.getDisplayMaterial() : Material.GRAY_DYE;
        return ItemStackCreator.getStack(
            gadget.getRarity().getColorCode() + gadget.getDisplayName(),
            mat, 1,
            lore.toArray(new String[0])
        );
    }

    @Override
    protected void onItemClick(ClickContext<GadgetsState> click, ViewContext ctx, LobbyGadgetData gadget, int index) {
        HypixelPlayer player = ctx.player();
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        if (!data.owns("GADGETS", gadget.name())) {
            if (gadget.getDustCost() > 0) {
                data.unlock("GADGETS", gadget.name());
                player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);
                player.sendMessage(Component.text("§aYou unlocked " + gadget.getRarity().getColorCode() + gadget.getDisplayName() + "§a!"));
                ctx.session(Object.class).refresh();
            }
            return;
        }

        if (data.isActive("GADGETS", gadget.name())) {
            data.deactivate("GADGETS");
            player.sendMessage(Component.text("§cGadget deselected!"));
        } else {
            data.activate("GADGETS", gadget.name());
            player.sendMessage(Component.text("§aSelected " + gadget.getRarity().getColorCode() + gadget.getDisplayName() + "§a!"));
        }
        player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);
        ctx.session(Object.class).refresh();
    }

    @Override
    protected boolean shouldFilterFromSearch(GadgetsState state, LobbyGadgetData item) {
        if (state.filter.isEmpty()) return false;
        return !item.getDisplayName().toLowerCase().contains(state.filter.toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<GadgetsState> layout, GadgetsState state, ViewContext ctx) {
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

    public static GadgetsState createInitialState() {
        return new GadgetsState(Arrays.asList(LobbyGadgetData.values()), 0, "");
    }

    public record GadgetsState(List<LobbyGadgetData> items, int page,
                               String filter) implements PaginatedState<LobbyGadgetData> {
        @Override
        public PaginatedState<LobbyGadgetData> withPage(int page) {
            return new GadgetsState(items, page, filter);
        }

        @Override
        public PaginatedState<LobbyGadgetData> withItems(List<LobbyGadgetData> items) {
            return new GadgetsState(items, page, filter);
        }
    }
}
