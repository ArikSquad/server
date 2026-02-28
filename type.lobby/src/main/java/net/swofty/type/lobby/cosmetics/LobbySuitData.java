package net.swofty.type.lobby.cosmetics;

import lombok.Getter;
import net.minestom.server.item.Material;

@Getter
public enum LobbySuitData {
    FROG("Frog Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Super High Jump.", Material.SLIME_BALL),
    NINJA("Ninja Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Click to throw a Ninja Shuriken!", Material.IRON_SWORD),
    SPEEDSTER("Speedster Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Sprint to run extremely fast.", Material.SUGAR),
    GHOSTLY_SKELETON("Ghostly Skeleton Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Makes the wearer invisible under the suit!", Material.BONE),
    DISCO("Disco Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Changes Colors when equipped!", Material.JUKEBOX),
    MERMAID("Mermaid Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Transform into a beautiful squid when under water.", Material.PRISMARINE_SHARD),
    SPOODERMAN("Spooderman Suit", CosmeticRarity.LEGENDARY, 0, "Mystery Box or Mystery Dust",
        "Click to swing around lobbies!", Material.STRING),
    WARRIOR("Warrior Suit", CosmeticRarity.LEGENDARY, 0, "Mystery Box or Mystery Dust",
        "Left click and look down to Ground Slam, or look ahead to Seismic Wave.", Material.IRON_AXE),
    NECROMANCER("Necromancer Suit", CosmeticRarity.LEGENDARY, 0, "Mystery Box or Mystery Dust",
        "Click to raise the dead.", Material.WITHER_SKELETON_SKULL),
    THOR("Thor Suit", CosmeticRarity.LEGENDARY, 0, "Mystery Box or Mystery Dust",
        "Click to strike lightning.", Material.LIGHTNING_ROD),
    DEATH_ANGEL("Death Angel Suit", CosmeticRarity.LEGENDARY, 0, "Mystery Box or Mystery Dust",
        "Click any player to lock on, chasing them around the lobby.", Material.PHANTOM_MEMBRANE),
    BAKER("Baker Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Click to deliver baked goods around the lobby.", Material.BREAD),
    BUMBLEBEE("Bumblebee Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "This suit will play a classical song when you fly or left-click!", Material.HONEYCOMB),
    FIREFIGHTER("Firefighter Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Click to spray water.", Material.WATER_BUCKET),
    PLUMBER("Plumber Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Click to throw fireballs.", Material.FIRE_CHARGE),
    ICE_WALKER("Ice Walker Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Absorb heat and chill the world around you by walking.", Material.ICE),
    VAMPIRE("Vampire Suit", CosmeticRarity.EPIC, 0, "Halloween Mystery Boxes",
        "Transform into a bat when you fly.", Material.REDSTONE),
    DRAGON_BREATH("Dragon Breath Suit", CosmeticRarity.LEGENDARY, 0, "Summer Mystery Boxes",
        "Click to breathe fire on your foes!", Material.DRAGON_BREATH),
    PIRATE("Pirate Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Click to sail around in a boat.", Material.OAK_BOAT),
    TNT("TNT Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Click to throw a bomb!", Material.TNT),
    WOLF_PRINCESS("Wolf Princess Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Click to summon your wolf pack!", Material.WOLF_SPAWN_EGG),
    SOLAR_POWER("Solar Power Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Suit drains as you move around. Click to recharge!", Material.SUNFLOWER),
    SOCCER("Soccer Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Click to spawn a soccer ball, which you can kick around!", Material.SLIME_BALL),
    SANTA("Santa Suit", CosmeticRarity.LEGENDARY, 0, "Holiday Mystery Boxes",
        "Click to deliver presents!", Material.RED_WOOL),
    TOY_SOLDIER("Toy Soldier Suit", CosmeticRarity.EPIC, 0, "Mystery Box or Mystery Dust",
        "Click to play the drums!", Material.STICK),
    SURPRISE_GIFT("Surprise Gift Suit", CosmeticRarity.LEGENDARY, 0, "50 Gift Milestone",
        "Click to disguise yourself as a present that other players can unwrap.", Material.CHEST),
    TREASURE_HUNTER("Treasure Hunter Suit", CosmeticRarity.LEGENDARY, 0, "Reward Cards",
        "Search lobbies to collect ancient golden treasures.", Material.GOLD_INGOT),
    COSTUME("Costume Suit", CosmeticRarity.EPIC, 0, "Halloween Mystery Boxes",
        "The suit rapidly changes to different spooky outfits.", Material.CARVED_PUMPKIN),
    NEW_YEARS_CELEBRATION("New Year's Celebration Suit", CosmeticRarity.EPIC, 0, "Holiday Mystery Boxes",
        "Punch to set off a firework.", Material.FIREWORK_ROCKET),
    GRINCH("Grinch Suit", CosmeticRarity.LEGENDARY, 0, "Holiday Mystery Boxes",
        "Punch to spawn presents that turn into coal.", Material.GREEN_DYE),
    ;

    private final String displayName;
    private final CosmeticRarity rarity;
    private final int dustCost;
    private final String specialSource;
    private final String fullSetAbility;
    private final Material displayMaterial;

    LobbySuitData(String displayName, CosmeticRarity rarity, int dustCost, String specialSource,
                  String fullSetAbility, Material displayMaterial) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.dustCost = dustCost;
        this.specialSource = specialSource;
        this.fullSetAbility = fullSetAbility;
        this.displayMaterial = displayMaterial;
    }

    public String getCostDisplay() {
        if (dustCost > 0) return dustCost + " Mystery Dust";
        if (specialSource != null) return specialSource;
        return "Currently Unavailable";
    }
}
