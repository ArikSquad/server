package net.swofty.type.skyblockgeneric.item.handlers.lore;

import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

public record LoreConfig(
        @Nullable
        BiFunction<SkyBlockItem, SkyBlockPlayer, List<String>> loreGenerator,
        @Nullable
        BiFunction<SkyBlockItem, SkyBlockPlayer, String> displayNameGenerator
) { }
