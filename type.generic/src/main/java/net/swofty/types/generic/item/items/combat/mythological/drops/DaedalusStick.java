package net.swofty.types.generic.item.items.combat.mythological.drops;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class DaedalusStick implements CustomSkyBlockItem, Enchanted, PetItem, Sellable, RightClickRecipe {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Drops rare off of Minotaurs from",
                "§7Diana's Mythological Ritual."));
    }

    @Override
    public double getSellValue() {
        return 250000;
    }

    @Override
    public ItemTypeLinker getRecipeItem() {
        return ItemTypeLinker.DAEDALUS_AXE;
    }
}
