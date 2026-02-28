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
import net.swofty.type.lobby.cosmetics.LobbyCompanionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUICompanionsView extends PaginatedView<LobbyCompanionData, GUICompanionsView.CompanionsState> {

    private static final int[] PAGINATED_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<CompanionsState> configuration() {
        return ViewConfiguration.withString(
            (state, _) -> "Companions - Page " + (state.page() + 1),
            InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(LobbyCompanionData companion, int index, HypixelPlayer player) {
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        boolean owned = data.owns("COMPANIONS", companion.name());
        boolean active = data.isActive("COMPANIONS", companion.name());
        List<String> lore = new ArrayList<>();

        lore.add("§7" + companion.getDescription());
        lore.add("");

        if (owned) {
            if (active) {
                lore.add("§aCurrently selected!");
                lore.add("§eClick to deselect!");
            } else {
                lore.add("§eClick to select!");
            }
        } else {
            lore.add("§7" + companion.getCostDisplay());
            if (companion.isPurchasableWithDust()) {
                lore.add("");
                lore.add("§eClick to craft for §b" + companion.getDustCost() + " §eMystery Dust!");
            }
        }

        Material mat = owned ? companion.getDisplayMaterial() : Material.GRAY_DYE;
        return ItemStackCreator.getStack(
            companion.getRarity().getColorCode() + companion.getDisplayName(),
            mat, 1,
            lore.toArray(new String[0])
        );
    }

    @Override
    protected void onItemClick(ClickContext<CompanionsState> click, ViewContext ctx, LobbyCompanionData companion, int index) {
        HypixelPlayer player = ctx.player();
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        if (!data.owns("COMPANIONS", companion.name())) {
            if (companion.isPurchasableWithDust()) {
                data.unlock("COMPANIONS", companion.name());
                player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);
                player.sendMessage(Component.text("§aYou unlocked " + companion.getRarity().getColorCode() + companion.getDisplayName() + "§a!"));
                ctx.session(Object.class).refresh();
            }
            return;
        }

        if (data.isActive("COMPANIONS", companion.name())) {
            data.deactivate("COMPANIONS");
        } else {
            data.activate("COMPANIONS", companion.name());
        }
        player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);
        ctx.session(Object.class).refresh();
    }

    @Override
    protected boolean shouldFilterFromSearch(CompanionsState state, LobbyCompanionData item) {
        if (state.filter.isEmpty()) return false;
        return !item.getDisplayName().toLowerCase().contains(state.filter.toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<CompanionsState> layout, CompanionsState state, ViewContext ctx) {
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

    public static CompanionsState createInitialState() {
        return new CompanionsState(Arrays.asList(LobbyCompanionData.values()), 0, "");
    }

    public record CompanionsState(List<LobbyCompanionData> items, int page,
                                  String filter) implements PaginatedState<LobbyCompanionData> {
        @Override
        public PaginatedState<LobbyCompanionData> withPage(int page) {
            return new CompanionsState(items, page, filter);
        }

        @Override
        public PaginatedState<LobbyCompanionData> withItems(List<LobbyCompanionData> items) {
            return new CompanionsState(items, page, filter);
        }
    }
}
