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
import net.swofty.type.lobby.cosmetics.LobbyClickEffectData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIClickEffectsView extends PaginatedView<LobbyClickEffectData, GUIClickEffectsView.ClickEffectsState> {

    private static final int[] PAGINATED_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<ClickEffectsState> configuration() {
        return ViewConfiguration.withString(
            (state, _) -> "Click Effects - Page " + (state.page() + 1),
            InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(LobbyClickEffectData effect, int index, HypixelPlayer player) {
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        boolean owned = data.owns("CLICK_EFFECTS", effect.name());
        boolean active = data.isActive("CLICK_EFFECTS", effect.name());
        List<String> lore = new ArrayList<>();

        lore.add("§7" + effect.getDescription());
        lore.add("");

        if (owned) {
            if (active) {
                lore.add("§aCurrently selected!");
                lore.add("§eClick to deselect!");
            } else {
                lore.add("§eClick to select!");
            }
        } else {
            lore.add("§7" + effect.getCostDisplay());
        }

        Material mat = owned ? effect.getDisplayMaterial() : Material.GRAY_DYE;
        return ItemStackCreator.getStack(
            effect.getRarity().getColorCode() + effect.getDisplayName(),
            mat, 1,
            lore.toArray(new String[0])
        );
    }

    @Override
    protected void onItemClick(ClickContext<ClickEffectsState> click, ViewContext ctx, LobbyClickEffectData effect, int index) {
        HypixelPlayer player = ctx.player();
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        if (!data.owns("CLICK_EFFECTS", effect.name())) {
            return;
        }

        if (data.isActive("CLICK_EFFECTS", effect.name())) {
            data.deactivate("CLICK_EFFECTS");
            player.sendMessage(Component.text("§cClick effect deselected!"));
        } else {
            data.activate("CLICK_EFFECTS", effect.name());
            player.sendMessage(Component.text("§aSelected " + effect.getRarity().getColorCode() + effect.getDisplayName() + "§a!"));
        }
        player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);
        ctx.session(Object.class).refresh();
    }

    @Override
    protected boolean shouldFilterFromSearch(ClickEffectsState state, LobbyClickEffectData item) {
        if (state.filter.isEmpty()) return false;
        return !item.getDisplayName().toLowerCase().contains(state.filter.toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<ClickEffectsState> layout, ClickEffectsState state, ViewContext ctx) {
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

    public static ClickEffectsState createInitialState() {
        return new ClickEffectsState(Arrays.asList(LobbyClickEffectData.values()), 0, "");
    }

    public record ClickEffectsState(List<LobbyClickEffectData> items, int page,
                                    String filter) implements PaginatedState<LobbyClickEffectData> {
        @Override
        public PaginatedState<LobbyClickEffectData> withPage(int page) {
            return new ClickEffectsState(items, page, filter);
        }

        @Override
        public PaginatedState<LobbyClickEffectData> withItems(List<LobbyClickEffectData> items) {
            return new ClickEffectsState(items, page, filter);
        }
    }
}
