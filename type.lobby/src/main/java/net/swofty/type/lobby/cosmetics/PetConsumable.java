package net.swofty.type.lobby.cosmetics;

import lombok.Getter;
import net.minestom.server.item.Material;

@Getter
public enum PetConsumable {
    APPLE("Apple", Material.APPLE, ConsumableType.FOOD),
    MELON("Melon", Material.MELON_SLICE, ConsumableType.FOOD),
    PUMPKIN_PIE("Pumpkin Pie", Material.PUMPKIN_PIE, ConsumableType.FOOD),
    CARROT("Carrot", Material.CARROT, ConsumableType.FOOD),
    BAKED_POTATO("Baked Potato", Material.BAKED_POTATO, ConsumableType.FOOD),
    MUSHROOM_SOUP("Mushroom Soup", Material.MUSHROOM_STEW, ConsumableType.FOOD),
    FLOWER("Flower", Material.POPPY, ConsumableType.FOOD),
    HAY("Hay", Material.HAY_BLOCK, ConsumableType.FOOD),
    WHEAT("Wheat", Material.WHEAT, ConsumableType.FOOD),
    BREAD("Bread", Material.BREAD, ConsumableType.FOOD),
    COOKIE("Cookie", Material.COOKIE, ConsumableType.FOOD),
    CAKE("Cake", Material.CAKE, ConsumableType.FOOD),
    RAW_FISH("Raw Fish", Material.COD, ConsumableType.FOOD),
    RAW_PORKCHOP("Raw Porkchop", Material.PORKCHOP, ConsumableType.FOOD),
    ANGUS_STEAK("Angus Steak", Material.COOKED_BEEF, ConsumableType.FOOD),
    BONE("Bone", Material.BONE, ConsumableType.FOOD),
    ROTTEN_FLESH("Rotten Flesh", Material.ROTTEN_FLESH, ConsumableType.FOOD),
    MAGMA_CREAM("Magma Cream", Material.MAGMA_CREAM, ConsumableType.FOOD),

    WATER("Water", Material.WATER_BUCKET, ConsumableType.DRINK),
    MILK("Milk", Material.MILK_BUCKET, ConsumableType.DRINK),
    LAVA("Lava", Material.LAVA_BUCKET, ConsumableType.DRINK),

    STICK("Stick", Material.STICK, ConsumableType.EXERCISE),
    BALL("Ball", Material.SLIME_BALL, ConsumableType.EXERCISE),
    LEASH("Leash", Material.LEAD, ConsumableType.EXERCISE),
    SPARRING_SWORD("Sparring Sword", Material.WOODEN_SWORD, ConsumableType.EXERCISE),
    FRISBEE("Frisbee", Material.PAPER, ConsumableType.EXERCISE),
    FEATHER("Feather", Material.FEATHER, ConsumableType.EXERCISE),

    NONE("None", Material.BARRIER, ConsumableType.FOOD);

    private final String displayName;
    private final Material material;
    private final ConsumableType type;

    PetConsumable(String displayName, Material material, ConsumableType type) {
        this.displayName = displayName;
        this.material = material;
        this.type = type;
    }

    public enum ConsumableType {
        FOOD, DRINK, EXERCISE
    }
}
