package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.cosmetic.PlayerCosmeticData;
import net.swofty.type.generic.data.Datapoint;

public class DatapointCosmeticData extends Datapoint<PlayerCosmeticData> {
    private static final JacksonSerializer<PlayerCosmeticData> serializer = new JacksonSerializer<>(PlayerCosmeticData.class);

    public DatapointCosmeticData(String key, PlayerCosmeticData value) {
        super(key, value != null ? value : new PlayerCosmeticData(), serializer);
    }

    public DatapointCosmeticData(String key) {
        super(key, new PlayerCosmeticData(), serializer);
    }
}
