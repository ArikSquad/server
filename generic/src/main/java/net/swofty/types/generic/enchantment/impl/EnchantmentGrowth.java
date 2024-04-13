package net.swofty.types.generic.enchantment.impl;

import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.enchantment.abstr.EnchFromTable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentGrowth implements Ench, EnchFromTable {
    public static final int[] increases = new int[]{15, 30, 45, 60, 75, 90, 105};

    @Override
    public String getDescription(int level) {
        return "Grants §a+" + increases[level - 1] + ItemStatistic.HEALTH.getColour() + ItemStatistic.HEALTH.getSymbol() + " " + ItemStatistic.HEALTH.getDisplayName();
    }

    @Override
    public ApplyLevels getLevelsToApply(@NotNull SkyBlockPlayer player) {
        return new ApplyLevels(new HashMap<>(Map.of(
                4, 36,
                5, 45,
                6, 91,
                7, 179
        )));
    }

    @Override
    public List<EnchantItemGroups> getGroups() {
        return List.of(EnchantItemGroups.ARMOR);
    }

    @Override
    public ItemStatistics getStatistics(int level) {
        double increase = increases[level - 1];
        return ItemStatistics.builder().with(ItemStatistic.HEALTH, increase).build();
    }

    @Override
    public TableLevels getLevelsFromTableToApply(@NotNull SkyBlockPlayer player) {
        return new TableLevels(new HashMap<>(Map.of(
                1, 10,
                2, 20,
                3, 30,
                4, 40,
                5, 50
        )));
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 8;
    }
}
