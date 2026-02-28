package net.swofty.type.lobby.cosmetics;

import lombok.Getter;

@Getter
public enum CosmeticRarity {
    COMMON("§a", "Common"),
    RARE("§9", "Rare"),
    EPIC("§5", "Epic"),
    LEGENDARY("§6", "Legendary"),
    RANKED("§e", "Ranked");

    private final String colorCode;
    private final String displayName;

    CosmeticRarity(String colorCode, String displayName) {
        this.colorCode = colorCode;
        this.displayName = displayName;
    }

    public String format(String name) {
        return colorCode + name;
    }
}
