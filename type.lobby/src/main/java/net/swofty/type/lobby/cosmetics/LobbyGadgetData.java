package net.swofty.type.lobby.cosmetics;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.type.generic.user.categories.Rank;

@Getter
public enum LobbyGadgetData {
    HOT_POTATO("Hot Potato", CosmeticRarity.RANKED, 0, null, "Fishing or Admin Gift",
        Material.BAKED_POTATO, "Hot, hot, hot!"),
    MAGIC_9_BALL("Magic 9 Ball", CosmeticRarity.RARE, 0, Rank.MVP, null,
        Material.COAL_BLOCK, "Learn the answers to your problems through some of the world's greatest leaders."),
    ROLL_OVER("Roll Over", CosmeticRarity.EPIC, 0, Rank.MVP_PLUS, null,
        Material.BONE, "Causes your pet to roll over, allowing you to rub their belly."),
    FORTUNE_COOKIE("Fortune Cookie", CosmeticRarity.COMMON, 15, null, null,
        Material.COOKIE, "What's better than a cookie? A cookie that gives solid life advice!"),
    TENNIS_BALL("Tennis Ball", CosmeticRarity.EPIC, 85, null, null,
        Material.SLIME_BALL, "A Tennis Ball gadget. It's a ball, usually used for tennis."),
    TETHERBALL("Tetherball", CosmeticRarity.RARE, 0, null, "Currently Unavailable",
        Material.OAK_FENCE, "Spawns a pole, complete with a tether ball - perfect for a quick game!"),
    FOUR_IN_A_ROW("Four in a Row", CosmeticRarity.LEGENDARY, 0, null, "Tournament Hall (250 Tributes)",
        Material.RED_STAINED_GLASS_PANE, "Challenge your friends to a game of Four in a Row!"),
    ROCK_PAPER_SHEARS("Rock Paper Shears", CosmeticRarity.LEGENDARY, 500, null, null,
        Material.SHEARS, "Challenge your friends to a Rock Paper Shears game!"),
    ;

    private final String displayName;
    private final CosmeticRarity rarity;
    private final int dustCost;
    private final Rank requiredRank;
    private final String specialSource;
    private final Material displayMaterial;
    private final String description;

    LobbyGadgetData(String displayName, CosmeticRarity rarity, int dustCost, Rank requiredRank,
                    String specialSource, Material displayMaterial, String description) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.dustCost = dustCost;
        this.requiredRank = requiredRank;
        this.specialSource = specialSource;
        this.displayMaterial = displayMaterial;
        this.description = description;
    }

    public String getCostDisplay() {
        if (requiredRank != null) return "Requires " + requiredRank.name().replace("_", " ");
        if (dustCost > 0) return dustCost + " Mystery Dust";
        if (specialSource != null) return specialSource;
        return "Currently Unavailable";
    }
}
