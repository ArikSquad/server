package net.swofty.type.lobby.cosmetics;

import lombok.Getter;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;

@Getter
public enum LobbyMorphData {
    PIG("Pig", CosmeticRarity.RARE, 36, null, EntityType.PIG, Material.PORKCHOP, MorphAbility.POOP),
    COW("Cow", CosmeticRarity.RARE, 30, null, EntityType.COW, Material.LEATHER, MorphAbility.POOP),
    ENDERMAN("Enderman", CosmeticRarity.EPIC, 90, null, EntityType.ENDERMAN, Material.ENDER_PEARL, MorphAbility.TELEPORT),
    CHICKEN("Chicken", CosmeticRarity.EPIC, 50, null, EntityType.CHICKEN, Material.EGG, MorphAbility.LAY_EGG),
    SPIDER("Spider", CosmeticRarity.EPIC, 80, null, EntityType.SPIDER, Material.STRING, MorphAbility.SHOOT_WEB),
    SHEEP("Sheep", CosmeticRarity.EPIC, 65, null, EntityType.SHEEP, Material.WHITE_WOOL, MorphAbility.CYCLE_COLORS),
    CREEPER("Creeper", CosmeticRarity.LEGENDARY, 180, null, EntityType.CREEPER, Material.GUNPOWDER, MorphAbility.EXPLOSION),
    BLAZE("Blaze", CosmeticRarity.LEGENDARY, 200, null, EntityType.BLAZE, Material.BLAZE_ROD, MorphAbility.FIREBALL),
    ZOMBIE("Zombie", CosmeticRarity.LEGENDARY, 160, null, EntityType.ZOMBIE, Material.ROTTEN_FLESH, MorphAbility.CONVERT_ZOMBIES),
    IRON_GOLEM("Iron Golem", CosmeticRarity.LEGENDARY, 250, null, EntityType.IRON_GOLEM, Material.IRON_INGOT, MorphAbility.FLING_PLAYERS),
    WITHER_SKELETON("Wither Skeleton", CosmeticRarity.RARE, 0, "Halloween Event Shop", EntityType.WITHER_SKELETON, Material.WITHER_SKELETON_SKULL, MorphAbility.NONE),
    CAVE_SPIDER("Cave Spider", CosmeticRarity.RARE, 0, "Currently Unavailable", EntityType.CAVE_SPIDER, Material.SPIDER_EYE, MorphAbility.LAUNCH_FORWARD),
    WITCH("Witch", CosmeticRarity.LEGENDARY, 0, "Currently Unavailable", EntityType.WITCH, Material.GLASS_BOTTLE, MorphAbility.THROW_POTION),
    SKELETON("Skeleton", CosmeticRarity.EPIC, 0, "Currently Unavailable", EntityType.SKELETON, Material.BOW, MorphAbility.SHOOT_ARROW),
    SNOWMAN("Snowman", CosmeticRarity.LEGENDARY, 0, "Holiday Event Shop", EntityType.SNOW_GOLEM, Material.SNOWBALL, MorphAbility.NONE),
    GRINCH("Grinch", CosmeticRarity.EPIC, 0, "Currently Unavailable", EntityType.ZOMBIE, Material.GREEN_DYE, MorphAbility.NONE),
    GUARDIAN("Guardian", CosmeticRarity.LEGENDARY, 0, "Currently Unavailable", EntityType.GUARDIAN, Material.PRISMARINE_SHARD, MorphAbility.SHOOT_LASER),
    RABBIT("Rabbit", CosmeticRarity.EPIC, 0, "Easter Event Shop", EntityType.RABBIT, Material.RABBIT_FOOT, MorphAbility.FLING_UPWARD),
    ;

    private final String displayName;
    private final CosmeticRarity rarity;
    private final int dustCost;
    private final String specialSource;
    private final EntityType entityType;
    private final Material displayMaterial;
    private final MorphAbility ability;

    LobbyMorphData(String displayName, CosmeticRarity rarity, int dustCost, String specialSource,
                   EntityType entityType, Material displayMaterial, MorphAbility ability) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.dustCost = dustCost;
        this.specialSource = specialSource;
        this.entityType = entityType;
        this.displayMaterial = displayMaterial;
        this.ability = ability;
    }

    public boolean isPurchasableWithDust() {
        return dustCost > 0;
    }

    public String getCostDisplay() {
        if (dustCost > 0) return dustCost + " Mystery Dust";
        if (specialSource != null) return specialSource;
        return "Currently Unavailable";
    }

    public enum MorphAbility {
        NONE("None", "No special ability."),
        POOP("Poop", "Drop items behind you as you walk."),
        TELEPORT("Teleport", "Teleport to a random nearby location."),
        LAY_EGG("Lay Egg", "Lay an egg on the ground."),
        SHOOT_WEB("Shoot Web", "Shoot web items everywhere."),
        CYCLE_COLORS("Cycle Colors", "Cycle through random wool colors."),
        EXPLOSION("Mini Explosion", "Fling nearby players away."),
        FIREBALL("Fireball", "Shoot five fireballs forward."),
        CONVERT_ZOMBIES("Convert Zombies", "Convert nearby players into zombies."),
        FLING_PLAYERS("Fling Players", "Fling players in front of you into the air."),
        LAUNCH_FORWARD("Launch Forward", "Launch yourself forwards."),
        THROW_POTION("Throw Potion", "Throw a potion converting the splashed player."),
        SHOOT_ARROW("Shoot Arrow", "Shoot an arrow forwards."),
        SHOOT_LASER("Shoot Laser", "Shoot a laser beam forwards."),
        FLING_UPWARD("Fling Upward", "Get flung upwards into the air.");

        @Getter
        private final String displayName;
        @Getter
        private final String description;

        MorphAbility(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
    }
}
