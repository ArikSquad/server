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
import net.swofty.type.lobby.cosmetics.LobbyCloakData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUICloaksView extends PaginatedView<LobbyCloakData, GUICloaksView.CloaksState> {

    private static final int[] PAGINATED_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<CloaksState> configuration() {
        return ViewConfiguration.withString(
            (state, _) -> "Cloaks - Page " + (state.page() + 1),
            InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(LobbyCloakData cloak, int index, HypixelPlayer player) {
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        boolean owned = data.owns("CLOAKS", cloak.name());
        boolean active = data.isActive("CLOAKS", cloak.name());
        List<String> lore = new ArrayList<>();

        lore.add("§7" + cloak.getDescription());
        lore.add("");

        if (owned) {
            if (active) {
                lore.add("§aCurrently equipped!");
                lore.add("§eClick to unequip!");
            } else {
                lore.add("§eClick to equip!");
            }
        } else {
            lore.add("§7" + cloak.getCostDisplay());
        }

        Material mat = owned ? cloak.getDisplayMaterial() : Material.GRAY_DYE;
        return ItemStackCreator.getStack(
            cloak.getRarity().getColorCode() + cloak.getDisplayName(),
            mat, 1,
            lore.toArray(new String[0])
        );
    }

    @Override
    protected void onItemClick(ClickContext<CloaksState> click, ViewContext ctx, LobbyCloakData cloak, int index) {
        HypixelPlayer player = ctx.player();
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        if (!data.owns("CLOAKS", cloak.name())) {
            return;
        }

        if (data.isActive("CLOAKS", cloak.name())) {
            data.deactivate("CLOAKS");
            player.sendMessage(Component.text("§cCloak unequipped!"));
        } else {
            data.activate("CLOAKS", cloak.name());
            player.sendMessage(Component.text("§aEquipped " + cloak.getRarity().getColorCode() + cloak.getDisplayName() + "§a!"));
        }
        player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);
        ctx.session(Object.class).refresh();
    }

    @Override
    protected boolean shouldFilterFromSearch(CloaksState state, LobbyCloakData item) {
        if (state.filter.isEmpty()) return false;
        return !item.getDisplayName().toLowerCase().contains(state.filter.toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<CloaksState> layout, CloaksState state, ViewContext ctx) {
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

    public static CloaksState createInitialState() {
        return new CloaksState(Arrays.asList(LobbyCloakData.values()), 0, "");
    }

    public record CloaksState(List<LobbyCloakData> items, int page,
                              String filter) implements PaginatedState<LobbyCloakData> {
        @Override
        public PaginatedState<LobbyCloakData> withPage(int page) {
            return new CloaksState(items, page, filter);
        }

        @Override
        public PaginatedState<LobbyCloakData> withItems(List<LobbyCloakData> items) {
            return new CloaksState(items, page, filter);
        }
    }
}
