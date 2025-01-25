package net.swofty.commons.item.attribute.attributes;

import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeUniqueTrackedID extends ItemAttribute<String> {
    @Override
    public String getKey() {
        return "unique-tracked-id";
    }

    @Override
    public String getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return "none";
    }

    @Override
    public String loadFromString(String string) {
        return string;
    }

    @Override
    public String saveIntoString() {
        return this.value;
    }
}
