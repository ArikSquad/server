package net.swofty.type.lobby.cosmetics;

import lombok.Getter;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;

@Getter
public enum LobbyCompanionData {
    PENGUIN("Penguin", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "He's migrating all the way from the North Pole to your friends list!",
        EntityType.CHICKEN, Material.FEATHER),
    TURTLE("Turtle", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "Need someone to help you come out of your shell? Slow and steady always wins the race!",
        EntityType.TURTLE, Material.TURTLE_SCUTE),
    GIFTERINO("Gifterino", CosmeticRarity.LEGENDARY, 0, "100 Gift Milestone Reward",
        "These little guys tend to follow the most generous person ever, and that person is you!",
        EntityType.ARMOR_STAND, Material.CHEST),
    BLACK_PUG("Black Pug", CosmeticRarity.LEGENDARY, 250, null,
        "Yelping, whimpering, howling away in the middle of night... Take this Black Pug companion for a walk today.",
        EntityType.WOLF, Material.BLACK_DYE),
    DUCK("Duck", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "Looking for an Egg-cellent companion? This quacking little friend can't be beak!",
        EntityType.CHICKEN, Material.EGG),
    FROG("Frog", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "He was once a beautiful human prince, transformed by an old witch's curse...",
        EntityType.FROG, Material.SLIME_BALL),
    SLOTH("Sloth", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "Neeeeeed aaaaaa friennnnnnnnd? Hypixel's favourite animal arrives as this cute Sloth companion!",
        EntityType.OCELOT, Material.COCOA_BEANS),
    HP8("HP8", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "BLEEP-BLOOP. BLOOP-BLEEP. Bwwwoooooooo! That's robot for 'I love you'.",
        EntityType.ARMOR_STAND, Material.IRON_BLOCK),
    WHITE_PUG("White Pug", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "If dog is a man's best friend, then this little guy is man's BEST best friend!",
        EntityType.WOLF, Material.WHITE_DYE),
    SHIBE("Shibe", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "Such pet. Many fluffy. Much doge. Wow.",
        EntityType.WOLF, Material.ORANGE_DYE),
    CHIMP("Chimp", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "Monkey see, monkey do, monkey gonna follow you.",
        EntityType.OCELOT, Material.COCOA_BEANS),
    GORILLA("Gorilla", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "DON'T ask him where he got those glasses! Meet the real king of the jungle.",
        EntityType.IRON_GOLEM, Material.LEATHER),
    PANDA("Panda", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "He may not know Kung Fu, but give this little guy a bamboo stick and he'll be your friend for life.",
        EntityType.PANDA, Material.BAMBOO),
    ELEPHANT("Elephant", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "They say an elephant never forgets... to follow you around the Hypixel Lobby!",
        EntityType.PIG, Material.GRAY_DYE),
    MAGIC_DOG("Magic Dog", CosmeticRarity.RANKED, 0, "Hypixel Store",
        "What time is it? Time to stretch your legs and go for a walk with this adventurous Magic Dog companion!",
        EntityType.WOLF, Material.YELLOW_DYE),
    ACHIEVEMENTS_TOTEM("Achievements Totem", CosmeticRarity.LEGENDARY, 0, "3000 Achievement Points",
        "Make your way to the stars with this special achievements totem!",
        EntityType.ARMOR_STAND, Material.TOTEM_OF_UNDYING),
    HP9_B("HP9-B", CosmeticRarity.RANKED, 0, "No Longer Available",
        "Equipped with an infra-red eye and a stealthy coat of black paint.",
        EntityType.ARMOR_STAND, Material.COAL_BLOCK),
    LITTLE_YOU("Little You", CosmeticRarity.LEGENDARY, 0, "Currently Unavailable",
        "We all have the little voice inside our head. This companion looks just like you!",
        EntityType.ARMOR_STAND, Material.PLAYER_HEAD),
    SPOOKY_GHOST("Spooky Ghost", CosmeticRarity.LEGENDARY, 0, "Currently Unavailable",
        "Ever wanted a spooky companion to scare off other players?",
        EntityType.VEX, Material.WHITE_STAINED_GLASS),
    BUGGY("Buggy", CosmeticRarity.LEGENDARY, 0, "Currently Unavailable",
        "This little buggy friend will buzz around you in lobbies!",
        EntityType.BEE, Material.HONEYCOMB),
    JACK_O_LANTERN("Jack o' Lantern", CosmeticRarity.LEGENDARY, 0, "Currently Unavailable",
        "Bring the light show wherever you go, this spooky Jack o' Lantern will light up as it follows you.",
        EntityType.ARMOR_STAND, Material.JACK_O_LANTERN),
    PRESENT("Present", CosmeticRarity.LEGENDARY, 0, "Currently Unavailable",
        "Customize this floating present to your liking and have it follow you around in the lobby!",
        EntityType.ARMOR_STAND, Material.CHEST),
    ;

    private final String displayName;
    private final CosmeticRarity rarity;
    private final int dustCost;
    private final String specialSource;
    private final String description;
    private final EntityType entityType;
    private final Material displayMaterial;

    LobbyCompanionData(String displayName, CosmeticRarity rarity, int dustCost, String specialSource,
                       String description, EntityType entityType, Material displayMaterial) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.dustCost = dustCost;
        this.specialSource = specialSource;
        this.description = description;
        this.entityType = entityType;
        this.displayMaterial = displayMaterial;
    }

    public boolean isPurchasableWithDust() {
        return dustCost > 0;
    }

    public String getCostDisplay() {
        if (dustCost > 0) return dustCost + " Mystery Dust";
        if (specialSource != null) return specialSource;
        return "Currently Unavailable";
    }
}
