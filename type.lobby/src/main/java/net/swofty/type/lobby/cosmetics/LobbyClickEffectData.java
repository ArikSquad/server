package net.swofty.type.lobby.cosmetics;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.type.generic.user.categories.Rank;

@Getter
public enum LobbyClickEffectData {
    CRITS("Crits", CosmeticRarity.COMMON, 0, Rank.VIP, null, Material.DIAMOND_SWORD,
        "Just like jump-critting a player, but way more stylish."),
    COILED("Coiled", CosmeticRarity.COMMON, 0, Rank.VIP_PLUS, null, Material.LEAD,
        "A sweet spiral circles you from toe to head."),
    HEARTS("Hearts", CosmeticRarity.RARE, 0, Rank.MVP, null, Material.RED_DYE,
        "Someone likes you! Little hearts will chase them in return."),
    ZEUS("Zeus", CosmeticRarity.RARE, 0, Rank.MVP_PLUS, null, Material.GOLD_INGOT,
        "Harness the power of gods and shock anyone who dares click you."),
    SHOWOFF("Showoff", CosmeticRarity.EPIC, 0, Rank.MVP_PLUS, null, Material.NETHER_STAR,
        "Show off your favorite color! (Based on your + color!)."),
    HEADPUNCH("Headpunch", CosmeticRarity.EPIC, 0, Rank.MVP_PLUS, null, Material.PISTON,
        "Send your head to space! Maybe someone will need it there?"),
    EGGY("Eggy", CosmeticRarity.RARE, 0, null, "Currently Unavailable", Material.EGG,
        "Which came first: the chicken or the egg? THE CLICK."),
    GHOSTLY("Ghostly", CosmeticRarity.EPIC, 0, null, "Currently Unavailable", Material.GLASS,
        "Turn into a spooky ghost!"),
    SKULL("Skull", CosmeticRarity.RARE, 0, null, "Halloween Event Shop", Material.SKELETON_SKULL,
        "Display a flurry of skulls!"),
    PRESENT("Present", CosmeticRarity.RARE, 0, null, "Currently Unavailable", Material.CHEST,
        "Who says a present can't be a person?"),
    HOLIDAY_FIREWORK("Holiday Firework", CosmeticRarity.RARE, 0, null, "Holiday Event Shop", Material.FIREWORK_ROCKET,
        "Fireworks are appropriate for any time of year!"),
    DAMAGE_INDICATORS("Damage Indicators", CosmeticRarity.EPIC, 0, null, "Currently Unavailable", Material.IRON_SWORD,
        "Is this SkyBlock?"),
    EASTER_EGG("Easter Egg", CosmeticRarity.RARE, 0, null, "Easter Event Shop", Material.TURTLE_EGG,
        "A bunch of Easter Eggs shoot out of your body."),
    HYPIXEL("Hypixel", CosmeticRarity.LEGENDARY, 0, null, "Currently Unavailable", Material.NETHER_STAR,
        "Celebrate the Hypixel Network with this neat effect that displays the Hypixel logo!"),
    BLIZZARD("Blizzard", CosmeticRarity.RARE, 0, null, "Holidays Event Shop", Material.PACKED_ICE,
        "Bring the storm whenever someone clicks you."),
    SPOUT("Spout", CosmeticRarity.EPIC, 0, null, "20 Ranks Gifted Reward", Material.WATER_BUCKET,
        "Water will spout out of your head!"),
    SPIRIT("Spirit", CosmeticRarity.LEGENDARY, 0, null, "Currently Unavailable", Material.GHAST_TEAR,
        "Knock the soul right out of them!"),
    SNOW("Snow", CosmeticRarity.RARE, 0, null, "Holidays Bingo Cards", Material.SNOWBALL,
        "Swirls of snow appear around you."),
    ;

    private final String displayName;
    private final CosmeticRarity rarity;
    private final int dustCost;
    private final Rank requiredRank;
    private final String specialSource;
    private final Material displayMaterial;
    private final String description;

    LobbyClickEffectData(String displayName, CosmeticRarity rarity, int dustCost, Rank requiredRank,
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
