package net.swofty.type.skyblockgeneric.skill;

import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkills;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.skill.skills.RunecraftingSkill;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.List;

public abstract class SkillCategory {
    public abstract Material getDisplayIcon();

    public abstract String getName();

    public abstract List<String> getDescription();

    public abstract SkillReward[] getRewards();

    public int getHighestLevel() {
        return getRewards().length;
    }

    public SkillReward getReward(int level) {
        return getRewards()[level - 1];
    }

    public int getLevel(double rawExperience) {
        int level = 0;
        double cumulative = 0;

        for (SkillReward reward : getRewards()) {
            cumulative += reward.requirement;
            if (rawExperience >= cumulative) {
                level++;
            } else {
                break;
            }
        }

        return level;
    }

    public record SkillReward(int level, int requirement, Reward... unlocks) {
        public List<String> getDisplay(List<String> lore) {
            lore.add("§7Level " + StringUtility.getAsRomanNumeral(level) + " Rewards:");

            Arrays.stream(unlocks).forEach(unlock -> {
                switch (unlock.type()) {
                    case XP -> lore.add("§7  §8+§b" + ((XPReward) unlock).getXP() + " SkyBlock XP");
                    case COINS -> lore.add("§7  §8+§6" + ((CoinReward) unlock).getCoins() + " §7Coins");
                    case REGION_ACCESS -> lore.add("§7  §8+§aAccess to " + ((RegionReward) unlock).getRegion());
                    case STATS_BASE -> {
                        ItemStatistic statistic = ((BaseStatisticReward) unlock).getStatistic();
                        lore.add("§7  §8+§a" + StringUtility.decimalify(((BaseStatisticReward) unlock).amountAdded(), 1) +
                                statistic.getSuffix() + " " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                statistic.getDisplayName());
                    }
                    case STATS_ADDITIVE_PERCENTAGE -> {
                        ItemStatistic statistic = ((AdditivePercentageStatisticReward) unlock).getStatistic();
                        lore.add("§7  §8+§a" + StringUtility.decimalify(((AdditivePercentageStatisticReward) unlock).amountAdded() * 100, 1) +
                                "% " + statistic.getDisplayColor() + statistic.getSymbol() + " " + statistic.getDisplayName());
                    }
                    case RUNE -> lore.add("§7  Access to Level §d" + ((RunecraftingSkill.RuneReward) unlock).getRuneLevel() + " §7Runes");
                }
            });

            return lore;
        }
    }

    public abstract static class Reward {
        public abstract UnlockType type();

        public abstract void onUnlock(SkyBlockPlayer player);

        public enum UnlockType {
            REGION_ACCESS,
            COINS,
            XP,
            STATS_BASE,
            STATS_ADDITIVE_PERCENTAGE,
            RUNE
        }
    }

    public abstract static class CoinReward extends Reward {
        @Override
        public UnlockType type() {
            return UnlockType.COINS;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {
            DatapointDouble coins = player.getSkyBlockData().get(SkyBlockDataHandler.Data.COINS, DatapointDouble.class);
            coins.setValue(coins.getValue() + getCoins());
        }

        public abstract int getCoins();
    }

    public abstract static class XPReward extends Reward {
        @Override
        public UnlockType type() {
            return UnlockType.XP;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {
            // TODO
        }

        public abstract int getXP();
    }

    public abstract static class BaseStatisticReward extends Reward {
        @Override
        public UnlockType type() {
            return UnlockType.STATS_BASE;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {
            DatapointSkills.PlayerSkills skills = player.getSkills();
            ItemStatistics statistics = ItemStatistics.builder()
                    .withBase(getStatistic(), amountAdded())
                    .build();
            skills.setStatistics(ItemStatistics.add(skills.getSkillStatistics(), statistics));
        }

        public abstract ItemStatistic getStatistic();

        public abstract Double amountAdded();
    }

    public abstract static class AdditivePercentageStatisticReward extends Reward {
        @Override
        public UnlockType type() {
            return UnlockType.STATS_ADDITIVE_PERCENTAGE;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {
            DatapointSkills.PlayerSkills skills = player.getSkills();
            ItemStatistics statistics = ItemStatistics.builder()
                    .withMultiplicativePercentage(getStatistic(), amountAdded())
                    .build();
            skills.setStatistics(ItemStatistics.add(skills.getSkillStatistics(), statistics));
        }

        public abstract ItemStatistic getStatistic();

        public abstract Double amountAdded();
    }

    public abstract static class RegionReward extends Reward {
        @Override
        public UnlockType type() {
            return UnlockType.REGION_ACCESS;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {
            // Handled by the region system
        }

        public abstract RegionType getRegion();
    }
}
