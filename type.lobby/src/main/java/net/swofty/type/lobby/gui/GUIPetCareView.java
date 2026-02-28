package net.swofty.type.lobby.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointPetData;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.PaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.pet.PlayerPetData;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.cosmetics.LobbyPetData;
import net.swofty.type.lobby.cosmetics.PetConsumable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIPetCareView extends PaginatedView<PetConsumable, GUIPetCareView.PetCareState> {

    private static final int[] PAGINATED_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34
    };

    @Override
    public ViewConfiguration<PetCareState> configuration() {
        return ViewConfiguration.withString(
            (state, _) -> "Pet Care - " + state.pet().getDisplayName(),
            InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(PetConsumable consumable, int index, HypixelPlayer player) {
        PlayerPetData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class).getValue();

        String typeLabel = switch (consumable.getType()) {
            case FOOD -> "§7Increases pet's Hunger rating.";
            case DRINK -> "§7Increases pet's Thirst rating.";
            case EXERCISE -> "§7Increases pet's Exercise rating.";
        };

        List<String> lore = new ArrayList<>();
        lore.add(typeLabel);
        lore.add("");
        lore.add("§eClick to give to pet!");

        return ItemStackCreator.getStack(
            "§b" + consumable.getDisplayName(),
            consumable.getMaterial(), 1,
            lore.toArray(new String[0])
        );
    }

    @Override
    protected void onItemClick(ClickContext<PetCareState> click, ViewContext ctx, PetConsumable consumable, int index) {
        HypixelPlayer player = ctx.player();
        PetCareState state = click.state();
        LobbyPetData pet = state.pet();
        PlayerPetData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class).getValue();

        if (!data.ownsPet(pet.name())) return;

        PlayerPetData.PetInstance inst = data.getPetInstance(pet.name());
        boolean isFavorite = switch (consumable.getType()) {
            case FOOD -> consumable == pet.getFavoriteFood();
            case DRINK -> consumable == pet.getFavoriteDrink();
            case EXERCISE -> consumable == pet.getFavoriteExercise();
        };

        switch (consumable.getType()) {
            case FOOD -> inst.feed(isFavorite);
            case DRINK -> inst.giveWater(isFavorite);
            case EXERCISE -> inst.giveExercise(isFavorite);
        }

        player.getDataHandler().get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class)
            .setValue(data);

        String bonus = isFavorite ? " §6(Favorite!)" : "";
        player.sendMessage(Component.text("§aGave " + consumable.getDisplayName() + " to your pet!" + bonus));
        ctx.session(Object.class).refresh();
    }

    @Override
    protected boolean shouldFilterFromSearch(PetCareState state, PetConsumable item) {
        return false;
    }

    @Override
    protected void layoutCustom(ViewLayout<PetCareState> layout, PetCareState state, ViewContext ctx) {
        HypixelPlayer player = ctx.player();
        PlayerPetData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class).getValue();

        LobbyPetData pet = state.pet();
        if (data.ownsPet(pet.name())) {
            PlayerPetData.PetInstance inst = data.getPetInstance(pet.name());
            layout.slot(4, (s, c) -> ItemStackCreator.getStack(
                pet.getRarity().getColorCode() + pet.getDisplayName(),
                pet.getDisplayMaterial(), 1,
                "§8Lv§7" + String.format("%03d", inst.getLevel()),
                "§7Status: " + inst.getStatusColor() + inst.getStatusName(),
                "",
                "§7Hunger: §e" + inst.getHunger() + "§f/100",
                "§7Thirst: §e" + inst.getThirst() + "§f/100",
                "§7Exercise: §e" + inst.getExercise() + "§f/100"
            ));

            boolean canMission = inst.canDoMission();
            if (canMission) {
                layout.slot(40, (s, c) -> ItemStackCreator.getStack(
                    "§aSend on Mission",
                    Material.COMPASS, 1,
                    "§7Send your pet on a mission to earn EXP!",
                    "§7Status bonus: §e" + inst.getStatusMultiplier() + "x",
                    "",
                    "§eClick to send!"
                ), (clk, vctx) -> {
                    PlayerPetData d = player.getDataHandler()
                        .get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class).getValue();
                    PlayerPetData.PetInstance pi = d.getPetInstance(pet.name());
                    if (pi != null && pi.canDoMission()) {
                        pi.completeMission(pi.getStatusMultiplier());
                        player.getDataHandler().get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class)
                            .setValue(d);
                        player.sendMessage(Component.text("§aPet completed a mission! Gained EXP."));
                        vctx.session(Object.class).refresh();
                    }
                });
            } else {
                long remaining = inst.getMissionCooldownRemaining();
                int minutes = (int) (remaining / 60000);
                layout.slot(40, (s, c) -> ItemStackCreator.getStack(
                    "§cMission Cooldown",
                    Material.CLOCK, 1,
                    "§7Your pet needs to rest before another mission.",
                    "§7Time remaining: §e" + minutes + " minutes"
                ));
            }
        }

        Components.backAlways(layout, 49);
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    public static List<PetConsumable> getConsumablesFor(LobbyPetData pet) {
        return Arrays.stream(PetConsumable.values())
            .filter(c -> c != PetConsumable.NONE)
            .toList();
    }

    public record PetCareState(List<PetConsumable> items, int page, String filter,
                               LobbyPetData pet) implements PaginatedState<PetConsumable> {
        @Override
        public PaginatedState<PetConsumable> withPage(int page) {
            return new PetCareState(items, page, filter, pet);
        }

        @Override
        public PaginatedState<PetConsumable> withItems(List<PetConsumable> items) {
            return new PetCareState(items, page, filter, pet);
        }
    }
}
