package net.swofty.type.lobby.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.lobby.cosmetics.*;

public class GUICollectiblesView extends StatelessView {

    private static final CosmeticCategory[] TOP_ROW = {
        CosmeticCategory.PETS, CosmeticCategory.COMPANIONS,
        CosmeticCategory.PARTICLE_PACK, CosmeticCategory.HATS,
        CosmeticCategory.CLICK_EFFECTS
    };

    private static final CosmeticCategory[] BOTTOM_ROW = {
        CosmeticCategory.SUITS, CosmeticCategory.GADGETS,
        CosmeticCategory.MORPHS, CosmeticCategory.CLOAKS,
        CosmeticCategory.SPRAYS, CosmeticCategory.EMOTES_AND_GESTURES,
        CosmeticCategory.EVENT_COSMETICS
    };

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Collectibles", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.filler(Components.FILLER);

        int slot = 9;
        for (CosmeticCategory cat : TOP_ROW) {
            final CosmeticCategory category = cat;
            layout.slot(slot, (s, c) -> buildCategoryItem(category), (click, vctx) -> openCategory(ctx, category));
            slot += 2;
        }

        slot = 27;
        for (CosmeticCategory cat : BOTTOM_ROW) {
            final CosmeticCategory category = cat;
            layout.slot(slot, (s, c) -> buildCategoryItem(category), (click, vctx) -> openCategory(ctx, category));
            slot += 2;
        }

        layout.slot(49, (s, c) -> ItemStackCreator.getStack(
            "§aCollectibles",
            Material.TRAPPED_CHEST, 1,
            "§7Collect fun cosmetic items! Unlock new items",
            "§7using §bMystery Dust§7 or hitting milestone",
            "§7rewards.",
            "",
            "§bMystery Dust §7is randomly given after playing",
            "§7games."
        ));

        Components.close(layout, 53);
    }

    private void openCategory(ViewContext ctx, CosmeticCategory category) {
        switch (category) {
            case PETS -> ctx.player().openView(new GUIPetsView(), GUIPetsView.createInitialState());
            case COMPANIONS -> ctx.player().openView(new GUICompanionsView(), GUICompanionsView.createInitialState());
            case CLICK_EFFECTS ->
                ctx.player().openView(new GUIClickEffectsView(), GUIClickEffectsView.createInitialState());
            case SUITS -> ctx.player().openView(new GUISuitsView(), GUISuitsView.createInitialState());
            case GADGETS -> ctx.player().openView(new GUIGadgetsView(), GUIGadgetsView.createInitialState());
            case MORPHS -> ctx.player().openView(new GUIMorphsView(), GUIMorphsView.createInitialState());
            case CLOAKS -> ctx.player().openView(new GUICloaksView(), GUICloaksView.createInitialState());
            case EMOTES_AND_GESTURES -> ctx.player().openView(new GUIEmotesAndGesturesView());
            default -> ctx.player().sendMessage(Component.text("§cThis category is coming soon!"));
        }
    }

    private ItemStack.Builder buildCategoryItem(CosmeticCategory category) {
        int total = switch (category) {
            case PETS -> LobbyPetData.values().length;
            case COMPANIONS -> LobbyCompanionData.values().length;
            case MORPHS -> LobbyMorphData.values().length;
            case CLOAKS -> LobbyCloakData.values().length;
            case CLICK_EFFECTS -> LobbyClickEffectData.values().length;
            case SUITS -> LobbySuitData.values().length;
            case GADGETS -> LobbyGadgetData.values().length;
            case EMOTES_AND_GESTURES -> LobbyEmoteData.values().length + LobbyGestureData.values().length;
            default -> 0;
        };

        return ItemStackCreator.getStack(
            "§a" + category.getDisplayName(),
            category.getDisplayMaterial(), 1,
            "§7Browse all " + category.getDisplayName().toLowerCase() + "!",
            "",
            total > 0 ? "§7Total: §e" + total : "§7Coming soon!",
            "",
            "§eClick to browse!"
        );
    }
}
