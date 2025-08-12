package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;

import java.util.Arrays;
import java.util.List;

@Getter
public enum BestiaryCategories {
    YOUR_ISLAND("§aYour island", BestiaryCategory.PRIVATE_ISLAND.values()),
    HUB("§aHub", BestiaryCategory.HUB.values())
    ;

    private final String displayName;
    private final BestiaryEntry[] entries;

    BestiaryCategories(String displayName, BestiaryEntry[] entries) {
        this.displayName = displayName;
        this.entries = entries;
    }

    public List<BestiaryMob> getAllMobs() {
        return Arrays.stream(entries)
                .flatMap(entry -> entry.getMobs().stream())
                .toList();
    }
}
