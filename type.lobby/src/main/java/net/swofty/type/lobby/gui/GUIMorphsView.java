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
import net.swofty.type.lobby.cosmetics.LobbyMorphData;
import net.swofty.type.lobby.cosmetics.MorphManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIMorphsView extends PaginatedView<LobbyMorphData, GUIMorphsView.MorphsState> {

    private static final int[] PAGINATED_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<MorphsState> configuration() {
        return ViewConfiguration.withString(
            (state, _) -> "Morphs - Page " + (state.page() + 1),
            InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(LobbyMorphData morph, int index, HypixelPlayer player) {
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        boolean owned = data.owns("MORPHS", morph.name());
        List<String> lore = new ArrayList<>();

        lore.add("§7Transform into a " + morph.getDisplayName() + " for §a5");
        lore.add("§7minutes.");
        lore.add("");

        if (morph.getAbility() != LobbyMorphData.MorphAbility.NONE) {
            lore.add("§7Ability: §a" + morph.getAbility().getDisplayName());
            lore.add("§7" + morph.getAbility().getDescription());
            lore.add("");
        }

        if (owned) {
            boolean active = MorphManager.hasMorph(player) &&
                data.isActive("MORPHS", morph.name());
            if (active) {
                lore.add("§aCurrently active!");
                lore.add("§eClick to deactivate!");
            } else {
                lore.add("§eClick to select!");
            }
        } else {
            lore.add("§7" + morph.getCostDisplay());
            if (morph.isPurchasableWithDust()) {
                lore.add("");
                lore.add("§eClick to craft for §b" + morph.getDustCost() + " §eMystery Dust!");
            }
        }

        Material mat = owned ? morph.getDisplayMaterial() : Material.GRAY_DYE;
        return ItemStackCreator.getStack(
            morph.getRarity().getColorCode() + morph.getDisplayName() + " Morph",
            mat, 1,
            lore.toArray(new String[0])
        );
    }

    @Override
    protected void onItemClick(ClickContext<MorphsState> click, ViewContext ctx, LobbyMorphData morph, int index) {
        HypixelPlayer player = ctx.player();
        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        if (!data.owns("MORPHS", morph.name())) {
            if (morph.isPurchasableWithDust()) {
                data.unlock("MORPHS", morph.name());
                player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);
                player.sendMessage(Component.text("§aYou unlocked " + morph.getRarity().getColorCode() + morph.getDisplayName() + " Morph§a!"));
                ctx.session(Object.class).refresh();
            }
            return;
        }

        if (MorphManager.hasMorph(player) && data.isActive("MORPHS", morph.name())) {
            MorphManager.deactivateMorph(player);
        } else {
            MorphManager.activateMorph(player, morph);
        }
        ctx.session(Object.class).refresh();
    }

    @Override
    protected boolean shouldFilterFromSearch(MorphsState state, LobbyMorphData item) {
        if (state.filter.isEmpty()) return false;
        return !item.getDisplayName().toLowerCase().contains(state.filter.toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<MorphsState> layout, MorphsState state, ViewContext ctx) {
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

    public static MorphsState createInitialState() {
        return new MorphsState(Arrays.asList(LobbyMorphData.values()), 0, "");
    }

    public record MorphsState(List<LobbyMorphData> items, int page,
                              String filter) implements PaginatedState<LobbyMorphData> {
        @Override
        public PaginatedState<LobbyMorphData> withPage(int page) {
            return new MorphsState(items, page, filter);
        }

        @Override
        public PaginatedState<LobbyMorphData> withItems(List<LobbyMorphData> items) {
            return new MorphsState(items, page, filter);
        }
    }
}
