package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.pet.PlayerPetData;

public class DatapointPetData extends Datapoint<PlayerPetData> {
    private static final JacksonSerializer<PlayerPetData> serializer = new JacksonSerializer<>(PlayerPetData.class);

    public DatapointPetData(String key, PlayerPetData value) {
        super(key, value != null ? value : new PlayerPetData(), serializer);
    }

    public DatapointPetData(String key) {
        super(key, new PlayerPetData(), serializer);
    }
}
