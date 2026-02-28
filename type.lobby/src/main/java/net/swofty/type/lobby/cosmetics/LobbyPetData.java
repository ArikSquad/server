package net.swofty.type.lobby.cosmetics;

import lombok.Getter;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;

import java.util.Arrays;
import java.util.List;

@Getter
public enum LobbyPetData {
    // Silverfish
    SILVERFISH("Silverfish", "Silverfish", CosmeticRarity.COMMON, 18, null,
        EntityType.SILVERFISH, false, Material.CLAY_BALL,
        PetConsumable.CAKE, PetConsumable.WATER, PetConsumable.STICK),

    // Cat
    CAT_BLACK("Cat", "Cat: Black", CosmeticRarity.COMMON, 18, null,
        EntityType.CAT, false, Material.COD,
        PetConsumable.RAW_FISH, PetConsumable.MILK, PetConsumable.BALL),
    CAT_RED("Cat", "Cat: Red", CosmeticRarity.COMMON, 18, null,
        EntityType.CAT, false, Material.COD,
        PetConsumable.RAW_FISH, PetConsumable.MILK, PetConsumable.BALL),
    CAT_SIAMESE("Cat", "Cat: Siamese", CosmeticRarity.COMMON, 18, null,
        EntityType.CAT, false, Material.COD,
        PetConsumable.RAW_FISH, PetConsumable.MILK, PetConsumable.BALL),
    CAT_BLACK_BABY("Cat", "Cat: Black (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.CAT, true, Material.COD,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.LEASH),
    CAT_RED_BABY("Cat", "Cat: Red (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.CAT, true, Material.COD,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.LEASH),
    CAT_SIAMESE_BABY("Cat", "Cat: Siamese (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.CAT, true, Material.COD,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.LEASH),
    WILD_OCELOT("Cat", "Wild Ocelot", CosmeticRarity.RARE, -1, "VIP+",
        EntityType.OCELOT, false, Material.COD,
        PetConsumable.RAW_FISH, PetConsumable.MILK, PetConsumable.BALL),
    WILD_OCELOT_BABY("Cat", "Wild Ocelot (Baby)", CosmeticRarity.RARE, -1, "VIP+",
        EntityType.OCELOT, true, Material.COD,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.LEASH),

    // Wolf
    WOLF("Wolf", "Wolf", CosmeticRarity.COMMON, 15, null,
        EntityType.WOLF, false, Material.BONE,
        PetConsumable.BONE, PetConsumable.WATER, PetConsumable.LEASH),
    WOLF_BABY("Wolf", "Wolf (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.WOLF, true, Material.BONE,
        PetConsumable.COOKIE, PetConsumable.MILK, PetConsumable.BALL),

    // Bat
    BAT("Bat", "Bat", CosmeticRarity.EPIC, 55, null,
        EntityType.BAT, false, Material.COAL,
        PetConsumable.MELON, PetConsumable.WATER, PetConsumable.STICK),

    // Villager
    VILLAGER_BLACKSMITH("Villager", "Villager: Blacksmith", CosmeticRarity.RARE, 32, null,
        EntityType.VILLAGER, false, Material.EMERALD,
        PetConsumable.ANGUS_STEAK, PetConsumable.LAVA, PetConsumable.SPARRING_SWORD),
    VILLAGER_BLACKSMITH_BABY("Villager", "Villager: Blacksmith (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.VILLAGER, true, Material.EMERALD,
        PetConsumable.PUMPKIN_PIE, PetConsumable.MILK, PetConsumable.STICK),
    VILLAGER_BUTCHER("Villager", "Villager: Butcher", CosmeticRarity.RARE, 32, null,
        EntityType.VILLAGER, false, Material.EMERALD,
        PetConsumable.ANGUS_STEAK, PetConsumable.LAVA, PetConsumable.SPARRING_SWORD),
    VILLAGER_BUTCHER_BABY("Villager", "Villager: Butcher (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.VILLAGER, true, Material.EMERALD,
        PetConsumable.COOKIE, PetConsumable.MILK, PetConsumable.BALL),
    VILLAGER_FARMER("Villager", "Villager: Farmer", CosmeticRarity.RARE, 32, null,
        EntityType.VILLAGER, false, Material.EMERALD,
        PetConsumable.BAKED_POTATO, PetConsumable.MILK, PetConsumable.FEATHER),
    VILLAGER_FARMER_BABY("Villager", "Villager: Farmer (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.VILLAGER, true, Material.EMERALD,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.STICK),
    VILLAGER_LIBRARIAN("Villager", "Villager: Librarian", CosmeticRarity.RARE, 32, null,
        EntityType.VILLAGER, false, Material.EMERALD,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.FRISBEE),
    VILLAGER_LIBRARIAN_BABY("Villager", "Villager: Librarian (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.VILLAGER, true, Material.EMERALD,
        PetConsumable.COOKIE, PetConsumable.MILK, PetConsumable.FEATHER),
    VILLAGER_PRIEST("Villager", "Villager: Priest", CosmeticRarity.RARE, 32, null,
        EntityType.VILLAGER, false, Material.EMERALD,
        PetConsumable.BREAD, PetConsumable.WATER, PetConsumable.FRISBEE),
    VILLAGER_PRIEST_BABY("Villager", "Villager: Priest (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.VILLAGER, true, Material.EMERALD,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.FEATHER),
    VILLAGER_ZOMBIE("Villager", "Villager: Zombie", CosmeticRarity.RARE, 85, null,
        EntityType.ZOMBIE_VILLAGER, false, Material.EMERALD,
        PetConsumable.ROTTEN_FLESH, PetConsumable.LAVA, PetConsumable.STICK),
    VILLAGER_ZOMBIE_BABY("Villager", "Villager: Zombie (Baby)", CosmeticRarity.EPIC, -1, "Halloween Event Shop",
        EntityType.ZOMBIE_VILLAGER, true, Material.EMERALD,
        PetConsumable.ROTTEN_FLESH, PetConsumable.LAVA, PetConsumable.STICK),
    WITCH("Villager", "Witch", CosmeticRarity.EPIC, -1, "Halloween Event Shop",
        EntityType.WITCH, false, Material.POISONOUS_POTATO,
        PetConsumable.PUMPKIN_PIE, PetConsumable.MILK, PetConsumable.FEATHER),

    // Zombie
    ZOMBIE("Zombie", "Zombie", CosmeticRarity.COMMON, 18, null,
        EntityType.ZOMBIE, false, Material.ROTTEN_FLESH,
        PetConsumable.ROTTEN_FLESH, PetConsumable.LAVA, PetConsumable.SPARRING_SWORD),
    ZOMBIE_BABY("Zombie", "Zombie (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.ZOMBIE, true, Material.ROTTEN_FLESH,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.SPARRING_SWORD),
    FROZEN_ZOMBIE("Zombie", "Frozen Zombie", CosmeticRarity.EPIC, -1, "Holidays Event Shop",
        EntityType.ZOMBIE, false, Material.ROTTEN_FLESH,
        PetConsumable.ROTTEN_FLESH, PetConsumable.WATER, PetConsumable.STICK),
    GROWING_ZOMBIE("Zombie", "Growing Zombie", CosmeticRarity.EPIC, -1, "Easter Event Shop",
        EntityType.ZOMBIE, false, Material.ROTTEN_FLESH,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.STICK),
    BURNING_ZOMBIE("Zombie", "Burning Zombie", CosmeticRarity.EPIC, -1, "Halloween Event Shop",
        EntityType.ZOMBIE, false, Material.ROTTEN_FLESH,
        PetConsumable.ROTTEN_FLESH, PetConsumable.NONE, PetConsumable.STICK),

    // Little Helper
    RED_LITTLE_HELPER("Little Helper", "Red Little Helper", CosmeticRarity.EPIC, -1, "Holidays Event Shop",
        EntityType.ZOMBIE_VILLAGER, false, Material.COOKIE,
        PetConsumable.COOKIE, PetConsumable.MILK, PetConsumable.FRISBEE),
    GREEN_LITTLE_HELPER("Little Helper", "Green Little Helper", CosmeticRarity.EPIC, -1, "Holidays Event Shop",
        EntityType.ZOMBIE_VILLAGER, false, Material.COOKIE,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.BALL),

    // Standalone Legendaries
    GOLEM("Golem", "Golem", CosmeticRarity.LEGENDARY, 300, null,
        EntityType.IRON_GOLEM, false, Material.IRON_INGOT,
        PetConsumable.FLOWER, PetConsumable.LAVA, PetConsumable.BALL),
    BLAZE("Blaze", "Blaze", CosmeticRarity.LEGENDARY, -1, "Podium position in Tournaments",
        EntityType.BLAZE, false, Material.BLAZE_ROD,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    SNOWMAN("Snowman", "Snowman", CosmeticRarity.LEGENDARY, -1, "Holidays Event Shop",
        EntityType.SNOW_GOLEM, false, Material.SNOWBALL,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),
    HEROBRINE("Herobrine", "Herobrine", CosmeticRarity.LEGENDARY, 250, null,
        EntityType.ZOMBIE, false, Material.PLAYER_HEAD,
        PetConsumable.COOKIE, PetConsumable.MILK, PetConsumable.SPARRING_SWORD),
    ENDERMITE("Endermite", "Endermite", CosmeticRarity.LEGENDARY, 250, null,
        EntityType.ENDERMITE, false, Material.ENDER_EYE,
        PetConsumable.NONE, PetConsumable.LAVA, PetConsumable.SPARRING_SWORD),
    MINI_WITHER("Mini Wither", "Mini Wither", CosmeticRarity.LEGENDARY, -1, "Halloween Event Shop",
        EntityType.WITHER, false, Material.WITHER_SKELETON_SKULL,
        PetConsumable.ROTTEN_FLESH, PetConsumable.LAVA, PetConsumable.NONE),
    CLONE("Clone", "Clone", CosmeticRarity.LEGENDARY, -1, "Tournament Hall (400 Tributes)",
        EntityType.ARMOR_STAND, false, Material.PLAYER_HEAD,
        PetConsumable.CAKE, PetConsumable.WATER, PetConsumable.SPARRING_SWORD),
    MINECART("Minecart", "Minecart", CosmeticRarity.LEGENDARY, -1, "Tournament Hall (400 Tributes)",
        EntityType.MINECART, false, Material.MINECART,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    GRINCH("Grinch", "Grinch", CosmeticRarity.LEGENDARY, -1, "Tournament Hall (100 Tributes)",
        EntityType.ZOMBIE, false, Material.CACTUS,
        PetConsumable.COOKIE, PetConsumable.LAVA, PetConsumable.FRISBEE),
    BEE("Bee", "Bee", CosmeticRarity.LEGENDARY, -1, "Easter Event Shop",
        EntityType.BEE, false, Material.HONEYCOMB,
        PetConsumable.FLOWER, PetConsumable.WATER, PetConsumable.BALL),
    FIREFLY("Firefly", "Firefly", CosmeticRarity.LEGENDARY, -1, "Currently unavailable",
        EntityType.BEE, false, Material.GLOW_BERRIES,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    CYCLOPS("Cyclops", "Cyclops", CosmeticRarity.LEGENDARY, -1, "Currently unavailable",
        EntityType.ZOMBIE, false, Material.FERMENTED_SPIDER_EYE,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    SPECTER("Specter", "Specter", CosmeticRarity.LEGENDARY, -1, "Halloween Event Shop",
        EntityType.VEX, false, Material.GLASS,
        PetConsumable.ROTTEN_FLESH, PetConsumable.LAVA, PetConsumable.STICK),

    // Spider
    SPIDER("Spider", "Spider", CosmeticRarity.EPIC, 62, null,
        EntityType.SPIDER, false, Material.STRING,
        PetConsumable.NONE, PetConsumable.WATER, PetConsumable.LEASH),
    CAVE_SPIDER("Spider", "Cave Spider", CosmeticRarity.RARE, 40, null,
        EntityType.CAVE_SPIDER, false, Material.SPIDER_EYE,
        PetConsumable.ANGUS_STEAK, PetConsumable.WATER, PetConsumable.SPARRING_SWORD),
    BOUNCY_SPIDER("Spider", "Bouncy Spider", CosmeticRarity.LEGENDARY, -1, "Halloween Event Shop",
        EntityType.SPIDER, false, Material.STRING,
        PetConsumable.ANGUS_STEAK, PetConsumable.WATER, PetConsumable.SPARRING_SWORD),
    SPIDER_JOCKEY("Spider", "Spider Jockey", CosmeticRarity.EPIC, -1, "Halloween Event Shop",
        EntityType.SPIDER, false, Material.STRING,
        PetConsumable.BONE, PetConsumable.WATER, PetConsumable.NONE),
    SNOWMAN_JOCKEY("Spider", "Snowman Jockey", CosmeticRarity.LEGENDARY, -1, "Holidays Event Shop",
        EntityType.SNOW_GOLEM, false, Material.SNOWBALL,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),

    // Chicken
    CHICKEN("Chicken", "Chicken", CosmeticRarity.COMMON, 15, null,
        EntityType.CHICKEN, false, Material.EGG,
        PetConsumable.FLOWER, PetConsumable.WATER, PetConsumable.FRISBEE),
    CHICKEN_BABY("Chicken", "Chicken (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.CHICKEN, true, Material.EGG,
        PetConsumable.COOKIE, PetConsumable.MILK, PetConsumable.STICK),
    CHICKEN_JOCKEY("Chicken", "Chicken Jockey", CosmeticRarity.LEGENDARY, -1, "Halloween Event Shop",
        EntityType.CHICKEN, false, Material.EGG,
        PetConsumable.ROTTEN_FLESH, PetConsumable.WATER, PetConsumable.FRISBEE),

    // Cow
    COW("Cow", "Cow", CosmeticRarity.COMMON, 15, null,
        EntityType.COW, false, Material.WHEAT,
        PetConsumable.WHEAT, PetConsumable.MILK, PetConsumable.FEATHER),
    COW_BABY("Cow", "Cow (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.COW, true, Material.WHEAT,
        PetConsumable.PUMPKIN_PIE, PetConsumable.MILK, PetConsumable.BALL),
    MOOSHROOM("Cow", "Mooshroom", CosmeticRarity.EPIC, 80, null,
        EntityType.MOOSHROOM, false, Material.RED_MUSHROOM,
        PetConsumable.MUSHROOM_SOUP, PetConsumable.MILK, PetConsumable.FEATHER),
    MOOSHROOM_BABY("Cow", "Mooshroom (Baby)", CosmeticRarity.LEGENDARY, 150, null,
        EntityType.MOOSHROOM, true, Material.RED_MUSHROOM,
        PetConsumable.COOKIE, PetConsumable.MILK, PetConsumable.FEATHER),

    // Creeper
    CREEPER("Creeper", "Creeper", CosmeticRarity.EPIC, 80, null,
        EntityType.CREEPER, false, Material.GUNPOWDER,
        PetConsumable.CAKE, PetConsumable.LAVA, PetConsumable.LEASH),
    POWERED_CREEPER("Creeper", "Powered Creeper", CosmeticRarity.LEGENDARY, 250, null,
        EntityType.CREEPER, false, Material.GUNPOWDER,
        PetConsumable.CAKE, PetConsumable.LAVA, PetConsumable.SPARRING_SWORD),

    // Horse
    HORSE_BLACK("Horse", "Horse: Black", CosmeticRarity.EPIC, 62, null,
        EntityType.HORSE, false, Material.SADDLE,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.LEASH),
    HORSE_BROWN("Horse", "Horse: Brown", CosmeticRarity.COMMON, 25, null,
        EntityType.HORSE, false, Material.SADDLE,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.LEASH),
    HORSE_CHESTNUT("Horse", "Horse: Chestnut", CosmeticRarity.RARE, 36, null,
        EntityType.HORSE, false, Material.SADDLE,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.LEASH),
    HORSE_CREAMY("Horse", "Horse: Creamy", CosmeticRarity.RARE, 36, null,
        EntityType.HORSE, false, Material.SADDLE,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.LEASH),
    HORSE_DARK_BROWN("Horse", "Horse: Dark Brown", CosmeticRarity.RARE, 36, null,
        EntityType.HORSE, false, Material.SADDLE,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.LEASH),
    HORSE_GRAY("Horse", "Horse: Gray", CosmeticRarity.RARE, 36, null,
        EntityType.HORSE, false, Material.SADDLE,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.LEASH),
    HORSE_WHITE("Horse", "Horse: White", CosmeticRarity.EPIC, 62, null,
        EntityType.HORSE, false, Material.SADDLE,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.LEASH),
    HORSE_BROWN_BABY("Horse", "Horse: Brown (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.HORSE, true, Material.SADDLE,
        PetConsumable.APPLE, PetConsumable.MILK, PetConsumable.STICK),
    HORSE_CHESTNUT_BABY("Horse", "Horse: Chestnut (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.HORSE, true, Material.SADDLE,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.LEASH),
    HORSE_CREAMY_BABY("Horse", "Horse: Creamy (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.HORSE, true, Material.SADDLE,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.LEASH),
    HORSE_DARK_BROWN_BABY("Horse", "Horse: Dark Brown (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.HORSE, true, Material.SADDLE,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.LEASH),
    HORSE_GRAY_BABY("Horse", "Horse: Gray (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.HORSE, true, Material.SADDLE,
        PetConsumable.CAKE, PetConsumable.MILK, PetConsumable.LEASH),
    UNDEAD_HORSE("Horse", "Undead Horse", CosmeticRarity.EPIC, 90, null,
        EntityType.ZOMBIE_HORSE, false, Material.ROTTEN_FLESH,
        PetConsumable.ROTTEN_FLESH, PetConsumable.LAVA, PetConsumable.STICK),
    SKELETON_HORSE("Horse", "Skeleton Horse", CosmeticRarity.LEGENDARY, -1, "Halloween Event Shop",
        EntityType.SKELETON_HORSE, false, Material.BONE,
        PetConsumable.ROTTEN_FLESH, PetConsumable.LAVA, PetConsumable.STICK),
    MULE("Horse", "Mule", CosmeticRarity.RARE, 32, null,
        EntityType.MULE, false, Material.CHEST,
        PetConsumable.HAY, PetConsumable.WATER, PetConsumable.LEASH),
    DONKEY("Horse", "Donkey", CosmeticRarity.RARE, 38, null,
        EntityType.DONKEY, false, Material.WHEAT,
        PetConsumable.HAY, PetConsumable.WATER, PetConsumable.LEASH),
    SKELETON_HORSE_BABY("Horse", "Skeleton Horse (Baby)", CosmeticRarity.LEGENDARY, -1, "Halloween Bingo",
        EntityType.SKELETON_HORSE, true, Material.BONE,
        PetConsumable.ROTTEN_FLESH, PetConsumable.LAVA, PetConsumable.STICK),

    // Pig
    PIG("Pig", "Pig", CosmeticRarity.COMMON, 15, null,
        EntityType.PIG, false, Material.PORKCHOP,
        PetConsumable.APPLE, PetConsumable.WATER, PetConsumable.FRISBEE),
    PIG_BABY("Pig", "Pig (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.PIG, true, Material.PORKCHOP,
        PetConsumable.APPLE, PetConsumable.MILK, PetConsumable.LEASH),
    PIG_ZOMBIE("Pig", "Pig Zombie", CosmeticRarity.EPIC, 80, null,
        EntityType.ZOMBIFIED_PIGLIN, false, Material.GOLD_NUGGET,
        PetConsumable.MELON, PetConsumable.LAVA, PetConsumable.FRISBEE),
    PIG_ZOMBIE_BABY("Pig", "Pig Zombie (Baby)", CosmeticRarity.LEGENDARY, 150, null,
        EntityType.ZOMBIFIED_PIGLIN, true, Material.GOLD_NUGGET,
        PetConsumable.COOKIE, PetConsumable.MILK, PetConsumable.SPARRING_SWORD),

    // Rabbit
    RABBIT_BLACK("Rabbit", "Rabbit: Black", CosmeticRarity.RARE, 32, null,
        EntityType.RABBIT, false, Material.CARROT,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),
    RABBIT_BLACK_WHITE("Rabbit", "Rabbit: Black & White", CosmeticRarity.EPIC, 60, null,
        EntityType.RABBIT, false, Material.CARROT,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.BALL),
    RABBIT_BROWN("Rabbit", "Rabbit: Brown", CosmeticRarity.RARE, 32, null,
        EntityType.RABBIT, false, Material.CARROT,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),
    RABBIT_GOLD("Rabbit", "Rabbit: Gold", CosmeticRarity.EPIC, 60, null,
        EntityType.RABBIT, false, Material.CARROT,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.BALL),
    RABBIT_SALT_PEPPER("Rabbit", "Rabbit: Salt & Pepper", CosmeticRarity.EPIC, 60, null,
        EntityType.RABBIT, false, Material.CARROT,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.BALL),
    RABBIT_WHITE("Rabbit", "Rabbit: White", CosmeticRarity.RARE, 32, null,
        EntityType.RABBIT, false, Material.CARROT,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),
    RABBIT_WHITE_BABY("Rabbit", "Rabbit: White (Baby)", CosmeticRarity.EPIC, -1, "Holidays Bingo",
        EntityType.RABBIT, true, Material.CARROT,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),
    KILLER_RABBIT("Rabbit", "Killer Rabbit", CosmeticRarity.LEGENDARY, -1, "Halloween Event Shop",
        EntityType.RABBIT, false, Material.CARROT,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.SPARRING_SWORD),
    RABBIT_JOCKEY("Rabbit", "Rabbit Jockey", CosmeticRarity.LEGENDARY, -1, "Main Lobby Egg Hunt",
        EntityType.RABBIT, false, Material.CARROT,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),

    // Sheep
    SHEEP_BLACK("Sheep", "Sheep: Black", CosmeticRarity.EPIC, 60, null,
        EntityType.SHEEP, false, Material.BLACK_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_BLACK_BABY("Sheep", "Sheep: Black (Baby)", CosmeticRarity.LEGENDARY, 130, null,
        EntityType.SHEEP, true, Material.BLACK_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_BLUE("Sheep", "Sheep: Blue", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.BLUE_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_BLUE_BABY("Sheep", "Sheep: Blue (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.BLUE_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_BROWN("Sheep", "Sheep: Brown", CosmeticRarity.COMMON, 18, null,
        EntityType.SHEEP, false, Material.BROWN_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_BROWN_BABY("Sheep", "Sheep: Brown (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.SHEEP, true, Material.BROWN_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_CYAN("Sheep", "Sheep: Cyan", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.CYAN_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_CYAN_BABY("Sheep", "Sheep: Cyan (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.CYAN_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_GREY("Sheep", "Sheep: Grey", CosmeticRarity.COMMON, 18, null,
        EntityType.SHEEP, false, Material.GRAY_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_GRAY_BABY("Sheep", "Sheep: Gray (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.SHEEP, true, Material.GRAY_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_GREEN("Sheep", "Sheep: Green", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.GREEN_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_GREEN_BABY("Sheep", "Sheep: Green (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.GREEN_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_LIGHT_BLUE("Sheep", "Sheep: Light Blue", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.LIGHT_BLUE_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_LIGHT_BLUE_BABY("Sheep", "Sheep: Light Blue (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.LIGHT_BLUE_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_LIME("Sheep", "Sheep: Lime", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.LIME_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_LIME_BABY("Sheep", "Sheep: Lime (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.LIME_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_MAGENTA("Sheep", "Sheep: Magenta", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.MAGENTA_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_MAGENTA_BABY("Sheep", "Sheep: Magenta (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.MAGENTA_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_ORANGE("Sheep", "Sheep: Orange", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.ORANGE_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_ORANGE_BABY("Sheep", "Sheep: Orange (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.ORANGE_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_PINK("Sheep", "Sheep: Pink", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, false, Material.PINK_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_PINK_BABY("Sheep", "Sheep: Pink (Baby)", CosmeticRarity.LEGENDARY, 130, null,
        EntityType.SHEEP, true, Material.PINK_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_PURPLE("Sheep", "Sheep: Purple", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.PURPLE_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_PURPLE_BABY("Sheep", "Sheep: Purple (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.PURPLE_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_RED("Sheep", "Sheep: Red", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.RED_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_RED_BABY("Sheep", "Sheep: Red (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.RED_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_SILVER("Sheep", "Sheep: Silver", CosmeticRarity.COMMON, 18, null,
        EntityType.SHEEP, false, Material.LIGHT_GRAY_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_SILVER_BABY("Sheep", "Sheep: Silver (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.SHEEP, true, Material.LIGHT_GRAY_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_WHITE("Sheep", "Sheep: White", CosmeticRarity.COMMON, 18, null,
        EntityType.SHEEP, false, Material.WHITE_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_WHITE_BABY("Sheep", "Sheep: White (Baby)", CosmeticRarity.RARE, 45, null,
        EntityType.SHEEP, true, Material.WHITE_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_YELLOW("Sheep", "Sheep: Yellow", CosmeticRarity.RARE, 30, null,
        EntityType.SHEEP, false, Material.YELLOW_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    SHEEP_YELLOW_BABY("Sheep", "Sheep: Yellow (Baby)", CosmeticRarity.EPIC, 85, null,
        EntityType.SHEEP, true, Material.YELLOW_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),
    SHEEP_RAINBOW("Sheep", "Sheep: Rainbow", CosmeticRarity.LEGENDARY, -1, "Summer Event Shop",
        EntityType.SHEEP, false, Material.WHITE_WOOL,
        PetConsumable.WHEAT, PetConsumable.WATER, PetConsumable.LEASH),
    BOUNCY_SHEEP("Sheep", "Bouncy Sheep", CosmeticRarity.EPIC, -1, "Currently unavailable",
        EntityType.SHEEP, false, Material.WHITE_WOOL,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    MERRY_SHEEP("Sheep", "Merry Sheep", CosmeticRarity.EPIC, -1, "Holidays Event Shop",
        EntityType.SHEEP, false, Material.WHITE_WOOL,
        PetConsumable.MELON, PetConsumable.MILK, PetConsumable.FRISBEE),

    // Slime
    SLIME_BIG("Slime", "Slime (Big)", CosmeticRarity.LEGENDARY, 160, null,
        EntityType.SLIME, false, Material.SLIME_BALL,
        PetConsumable.BONE, PetConsumable.MILK, PetConsumable.BALL),
    SLIME_SMALL("Slime", "Slime (Small)", CosmeticRarity.EPIC, 90, null,
        EntityType.SLIME, false, Material.SLIME_BALL,
        PetConsumable.FLOWER, PetConsumable.MILK, PetConsumable.STICK),
    SLIME_TINY("Slime", "Slime (Tiny)", CosmeticRarity.RARE, 45, null,
        EntityType.SLIME, false, Material.SLIME_BALL,
        PetConsumable.RAW_FISH, PetConsumable.MILK, PetConsumable.FRISBEE),
    MAGMA_CUBE_BIG("Slime", "Magma Cube (Big)", CosmeticRarity.LEGENDARY, 165, null,
        EntityType.MAGMA_CUBE, false, Material.MAGMA_CREAM,
        PetConsumable.MAGMA_CREAM, PetConsumable.LAVA, PetConsumable.FEATHER),
    MAGMA_CUBE_SMALL("Slime", "Magma Cube (Small)", CosmeticRarity.EPIC, 90, null,
        EntityType.MAGMA_CUBE, false, Material.MAGMA_CREAM,
        PetConsumable.MAGMA_CREAM, PetConsumable.LAVA, PetConsumable.BALL),
    MAGMA_CUBE_TINY("Slime", "Magma Cube (Tiny)", CosmeticRarity.EPIC, 45, null,
        EntityType.MAGMA_CUBE, false, Material.MAGMA_CREAM,
        PetConsumable.MAGMA_CREAM, PetConsumable.MILK, PetConsumable.FRISBEE),

    // Skeleton
    SKELETON("Skeleton", "Skeleton", CosmeticRarity.EPIC, 80, null,
        EntityType.SKELETON, false, Material.BONE,
        PetConsumable.ROTTEN_FLESH, PetConsumable.LAVA, PetConsumable.FRISBEE),
    WITHER_SKELETON("Skeleton", "Wither Skeleton", CosmeticRarity.LEGENDARY, -1, "Tournament Hall (400 Tributes)",
        EntityType.WITHER_SKELETON, false, Material.COAL,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    FROZEN_SKELETON("Skeleton", "Frozen Skeleton", CosmeticRarity.EPIC, -1, "Holidays Event Shop",
        EntityType.SKELETON, false, Material.BONE,
        PetConsumable.BONE, PetConsumable.WATER, PetConsumable.BALL),
    SMOLDERING_SKELETON("Skeleton", "Smoldering Skeleton", CosmeticRarity.EPIC, -1, "Halloween Event Shop",
        EntityType.SKELETON, false, Material.BONE,
        PetConsumable.BONE, PetConsumable.NONE, PetConsumable.STICK),

    // Guardian
    GUARDIAN("Guardian", "Guardian", CosmeticRarity.RARE, -1, "Season 1 Battle Pass",
        EntityType.GUARDIAN, false, Material.PRISMARINE_SHARD,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    ELDER_GUARDIAN("Guardian", "Elder Guardian", CosmeticRarity.LEGENDARY, -1, "Season 1 Battle Pass",
        EntityType.ELDER_GUARDIAN, false, Material.PRISMARINE_CRYSTALS,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    FLYING_GUARDIAN("Guardian", "Flying Guardian", CosmeticRarity.LEGENDARY, -1, "Summer Bingo",
        EntityType.GUARDIAN, false, Material.PRISMARINE_SHARD,
        PetConsumable.FLOWER, PetConsumable.WATER, PetConsumable.FRISBEE),

    // Squid
    SQUID("Squid", "Squid", CosmeticRarity.LEGENDARY, -1, "Holiday Contest",
        EntityType.SQUID, false, Material.INK_SAC,
        PetConsumable.RAW_FISH, PetConsumable.WATER, PetConsumable.FRISBEE),
    FLYING_SQUID("Squid", "Flying Squid", CosmeticRarity.LEGENDARY, -1, "Currently unavailable",
        EntityType.SQUID, false, Material.INK_SAC,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),

    // Fish
    FISH("Fish", "Fish", CosmeticRarity.EPIC, -1, "Summer Event Shop",
        EntityType.COD, false, Material.COD,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),
    PUFFERFISH("Fish", "Pufferfish", CosmeticRarity.LEGENDARY, -1, "Main Lobby Fishing",
        EntityType.PUFFERFISH, false, Material.PUFFERFISH,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),
    SALMON("Fish", "Salmon", CosmeticRarity.EPIC, -1, "Halloween Bingo",
        EntityType.SALMON, false, Material.SALMON,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),
    CLOWNFISH("Fish", "Clownfish", CosmeticRarity.EPIC, -1, "Halloween Event Shop",
        EntityType.TROPICAL_FISH, false, Material.TROPICAL_FISH,
        PetConsumable.CARROT, PetConsumable.WATER, PetConsumable.STICK),

    // Blocks
    TNT("Blocks", "TNT", CosmeticRarity.LEGENDARY, -1, "Tournament Hall (400 Tributes)",
        EntityType.TNT, false, Material.TNT,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    PACKED_ICE("Blocks", "Packed Ice", CosmeticRarity.LEGENDARY, -1, "Holiday Bingo",
        EntityType.FALLING_BLOCK, false, Material.PACKED_ICE,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),
    HAY_BALE("Blocks", "Hay Bale", CosmeticRarity.LEGENDARY, -1, "Easter Bingo",
        EntityType.FALLING_BLOCK, false, Material.HAY_BLOCK,
        PetConsumable.NONE, PetConsumable.NONE, PetConsumable.NONE),

    // Enderman
    ENDERMAN("Enderman", "Enderman", CosmeticRarity.LEGENDARY, 225, null,
        EntityType.ENDERMAN, false, Material.ENDER_PEARL,
        PetConsumable.BAKED_POTATO, PetConsumable.MILK, PetConsumable.BALL),
    ENDERMAN_PUMPKIN("Enderman", "Enderman: Pumpkin", CosmeticRarity.LEGENDARY, -1, "Halloween Event Shop",
        EntityType.ENDERMAN, false, Material.ENDER_PEARL,
        PetConsumable.NONE, PetConsumable.LAVA, PetConsumable.NONE);

    private final String category;
    private final String displayName;
    private final CosmeticRarity rarity;
    private final int dustCost;
    private final String specialSource;
    private final EntityType entityType;
    private final boolean baby;
    private final Material displayMaterial;
    private final PetConsumable favoriteFood;
    private final PetConsumable favoriteDrink;
    private final PetConsumable favoriteExercise;

    LobbyPetData(String category, String displayName, CosmeticRarity rarity,
                 int dustCost, String specialSource,
                 EntityType entityType, boolean baby, Material displayMaterial,
                 PetConsumable favoriteFood, PetConsumable favoriteDrink, PetConsumable favoriteExercise) {
        this.category = category;
        this.displayName = displayName;
        this.rarity = rarity;
        this.dustCost = dustCost;
        this.specialSource = specialSource;
        this.entityType = entityType;
        this.baby = baby;
        this.displayMaterial = displayMaterial;
        this.favoriteFood = favoriteFood;
        this.favoriteDrink = favoriteDrink;
        this.favoriteExercise = favoriteExercise;
    }

    public boolean isPurchasableWithDust() {
        return dustCost > 0;
    }

    public String getCostDisplay() {
        if (dustCost > 0) return "§b" + dustCost + " §eMystery Dust";
        if (specialSource != null) return "§c" + specialSource;
        return "§cUnavailable";
    }

    public int getExpForLevel(int level) {
        return 100 + (level * 50);
    }

    public static List<LobbyPetData> getByCategory(String category) {
        return Arrays.stream(values())
            .filter(p -> p.category.equals(category))
            .toList();
    }

    public static List<String> getCategories() {
        return Arrays.stream(values())
            .map(LobbyPetData::getCategory)
            .distinct()
            .toList();
    }
}
