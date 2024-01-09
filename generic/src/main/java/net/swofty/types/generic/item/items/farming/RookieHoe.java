package net.swofty.types.generic.item.items.farming;

import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;

import java.util.List;

public class RookieHoe implements CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7Crops broken with this hoe have", "§7a §a50%§7 chance to drop a seed!");
    }
}
