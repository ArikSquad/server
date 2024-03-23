package net.swofty.types.generic.item.items.jerrysworkshop;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.bloom.CuckooFilterCommands;

import java.util.ArrayList;
import java.util.Arrays;

public class GlacialFragment implements CustomSkyBlockItem, SkullHead, Sellable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 200;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f5667b183d824be6f8249318472a727c89f1146926c4695dd05f9ea9984ced45";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7A small piece of a giant glacier.",
                "§7Doesn\u0027t ever melt."));
    }
}
