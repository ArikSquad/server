package net.swofty.type.lobby.cosmetics;

import lombok.Getter;
import net.minestom.server.item.Material;

@Getter
public enum CosmeticCategory {
    PETS("Pets", Material.BONE),
    COMPANIONS("Companions", Material.PLAYER_HEAD),
    PARTICLE_PACK("Particle Pack", Material.NETHER_STAR),
    HATS("Hats", Material.DIAMOND_HELMET),
    CLICK_EFFECTS("Click Effects", Material.PUFFERFISH),
    SUITS("Suits", Material.GOLDEN_LEGGINGS),
    GADGETS("Gadgets", Material.PISTON),
    MORPHS("Morphs", Material.ZOMBIE_HEAD),
    CLOAKS("Cloaks", Material.LEATHER_CHESTPLATE),
    SPRAYS("Sprays & Graffiti", Material.PAINTING),
    EMOTES_AND_GESTURES("Emotes & Gestures", Material.NOTE_BLOCK),
    EVENT_COSMETICS("Event Cosmetics", Material.ENDER_CHEST);

    private final String displayName;
    private final Material displayMaterial;

    CosmeticCategory(String displayName, Material displayMaterial) {
        this.displayName = displayName;
        this.displayMaterial = displayMaterial;
    }
}
