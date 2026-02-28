package net.swofty.type.lobby.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointPetData;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.PaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.pet.PlayerPetData;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.lobby.cosmetics.LobbyPetData;
import net.swofty.type.lobby.cosmetics.LobbyPetManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIPetsView extends PaginatedView<LobbyPetData, GUIPetsView.PetsState> {

    private static final int[] PAGINATED_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<PetsState> configuration() {
        return ViewConfiguration.withString(
            (state, _) -> "Pets - Page " + (state.page() + 1),
            InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(LobbyPetData pet, int index, HypixelPlayer player) {
        PlayerPetData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class).getValue();

        boolean owned = data.ownsPet(pet.name());
        List<String> lore = new ArrayList<>();

        if (owned) {
            PlayerPetData.PetInstance inst = data.getPetInstance(pet.name());
            String name = inst.getCustomName() != null ? inst.getCustomName() : "(Use /pet to set name)";
            lore.add("§8Lv§7" + String.format("%03d", inst.getLevel()) + " §a" + name);
            lore.add("§7EXP: §b" + inst.getExp() + "§f/" + inst.getExpForNextLevel());
            lore.add("§7Type: §9" + pet.getCategory());
            lore.add("");
            lore.add("§7Status: " + inst.getStatusColor() + inst.getStatusName());
            lore.add("");
            lore.add("§7Hunger: §e" + inst.getHunger() + "§f/100");
            lore.add("§7Thirst: §e" + inst.getThirst() + "§f/100");
            lore.add("§7Exercise: §e" + inst.getExercise() + "§f/100");
            lore.add("");
            lore.add("§7Requires §aVIP §7to spawn or rename.");
            lore.add("");

            boolean isActive = pet.name().equals(data.getActivePetId());
            if (isActive) {
                lore.add("§7Already summoned!");
            } else {
                lore.add("§6Left-Click §7to summon this pet.");
            }
            lore.add("§bRight-Click §7to provide care");
        } else {
            lore.add("§7" + pet.getCostDisplay());
            lore.add("");
            lore.add("§7Requires §aVIP §7to spawn or rename.");
            if (pet.getDustCost() > 0) {
                lore.add("");
                lore.add("§eClick to craft for §b" + pet.getDustCost() + " §eMystery Dust!");
            }
        }

        Material mat = owned ? pet.getDisplayMaterial() : Material.GRAY_DYE;
        return ItemStackCreator.getStack(
            pet.getRarity().getColorCode() + pet.getDisplayName(),
            mat, 1,
            lore.toArray(new String[0])
        );
    }

    @Override
    protected void onItemClick(ClickContext<PetsState> click, ViewContext ctx, LobbyPetData pet, int index) {
        HypixelPlayer player = ctx.player();
        PlayerPetData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class).getValue();

        if (!data.ownsPet(pet.name())) {
            if (pet.getDustCost() > 0) {
                data.addPet(pet.name());
                player.getDataHandler().get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class)
                    .setValue(data);
                player.sendMessage(Component.text("§aYou unlocked " + pet.getRarity().getColorCode() + pet.getDisplayName() + "§a!"));
                ctx.session(Object.class).refresh();
            }
            return;
        }

        if (click.click() instanceof net.minestom.server.inventory.click.Click.Right) {
            player.openView(new GUIPetCareView(), new GUIPetCareView.PetCareState(
                GUIPetCareView.getConsumablesFor(pet), 1, "", pet
            ));
            return;
        }

        Rank rank = player.getDataHandler()
            .get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue();
        if (!rank.isEqualOrHigherThan(Rank.VIP)) {
            player.sendMessage(Component.text("§cYou need VIP or higher to spawn pets!"));
            return;
        }

        if (pet.name().equals(data.getActivePetId())) {
            LobbyPetManager.despawnPet(player);
            data.despawnPet();
            player.getDataHandler().get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class)
                .setValue(data);
            player.sendMessage(Component.text("§cPet despawned!"));
        } else {
            LobbyPetManager.spawnPet(player, pet, player.getInstance());
            player.sendMessage(Component.text("§aSpawned " + pet.getRarity().getColorCode() + pet.getDisplayName() + "§a!"));
        }
        ctx.session(Object.class).refresh();
    }

    @Override
    protected boolean shouldFilterFromSearch(PetsState state, LobbyPetData item) {
        if (state.filter.isEmpty()) return false;
        return !item.getDisplayName().toLowerCase().contains(state.filter.toLowerCase())
            && !item.getCategory().toLowerCase().contains(state.filter.toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<PetsState> layout, PetsState state, ViewContext ctx) {
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

    public static PetsState createInitialState() {
        return new PetsState(Arrays.asList(LobbyPetData.values()), 0, "");
    }

    public record PetsState(List<LobbyPetData> items, int page,
                            String filter) implements PaginatedState<LobbyPetData> {
        @Override
        public PaginatedState<LobbyPetData> withPage(int page) {
            return new PetsState(items, page, filter);
        }

        @Override
        public PaginatedState<LobbyPetData> withItems(List<LobbyPetData> items) {
            return new PetsState(items, page, filter);
        }
    }
}
