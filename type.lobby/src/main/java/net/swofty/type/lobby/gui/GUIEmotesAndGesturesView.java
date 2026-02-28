package net.swofty.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.cosmetic.PlayerCosmeticData;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointCosmeticData;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.lobby.cosmetics.LobbyEmoteData;
import net.swofty.type.lobby.cosmetics.LobbyGestureData;

public class GUIEmotesAndGesturesView extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Emotes & Gestures", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.filler(Components.FILLER);

        PlayerCosmeticData data = ctx.player().getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();

        int ownedEmotes = data.getOwnedCount("EMOTES");
        int totalEmotes = LobbyEmoteData.values().length;
        int emotePercent = totalEmotes > 0 ? (ownedEmotes * 100 / totalEmotes) : 0;

        int ownedGestures = data.getOwnedCount("GESTURES");
        int totalGestures = LobbyGestureData.values().length;
        int gesturePercent = totalGestures > 0 ? (ownedGestures * 100 / totalGestures) : 0;

        layout.slot(21, (s, c) -> ItemStackCreator.getStackHead(
            "§aEmotes",
            "d17e8ba17459d20fd73672bcb8a9e2a8a44cf0a5ff154122d96b5dbbd9171a",
            1,
            "§7Show detailed facial expressions to",
            "§7all that will watch!",
            "",
            "§7Unlocked: §e" + ownedEmotes + "/" + totalEmotes + " §8(" + emotePercent + "%)",
            "",
            "§eClick to browse!"
        ), (click, vctx) -> {
            ctx.player().openView(new GUIEmotesView(), GUIEmotesView.createInitialState());
        });

        layout.slot(23, (s, c) -> ItemStackCreator.getStackHead(
            "§aGestures",
            "26e6a37aa55cb6fa92d5a3af86c64d3912e5835c3b2fc2da5224c975ffaf2af",
            1,
            "§7Show off your moves, wave at your",
            "§7friends or more with these fancy",
            "§7gestures!",
            "",
            "§7Unlocked: §e" + ownedGestures + "/" + totalGestures + " §8(" + gesturePercent + "%)",
            "",
            "§eClick to browse!"
        ), (click, vctx) -> {
            ctx.player().openView(new GUIGesturesView(), GUIGesturesView.createInitialState());
        });

        Components.backOrClose(layout, 48, ctx);

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

        Components.close(layout, 40);
    }
}
