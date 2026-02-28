package net.swofty.type.lobby.cosmetics;

import net.minestom.server.instance.Instance;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointPetData;
import net.swofty.type.generic.pet.PlayerPetData;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyPetManager {
    private static final Map<UUID, LobbyPetEntity> activePets = new ConcurrentHashMap<>();

    public static void spawnPet(HypixelPlayer player, LobbyPetData petData, Instance instance) {
        despawnPet(player);

        PlayerPetData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class).getValue();
        PlayerPetData.PetInstance petInstance = data.getPetInstance(petData.name());

        String displayName = petData.getDisplayName();
        if (petInstance != null && petInstance.getCustomName() != null) {
            displayName = petInstance.getCustomName();
        }

        LobbyPetEntity entity = new LobbyPetEntity(petData, player.getUuid(), displayName);
        entity.spawnForOwner(instance, player.getPosition());
        activePets.put(player.getUuid(), entity);

        data.summonPet(petData.name());
        player.getDataHandler().get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class)
            .setValue(data);
    }

    public static void despawnPet(HypixelPlayer player) {
        LobbyPetEntity existing = activePets.remove(player.getUuid());
        if (existing != null && !existing.isRemoved()) {
            existing.remove();
        }
    }

    public static LobbyPetEntity getActivePet(UUID playerUuid) {
        return activePets.get(playerUuid);
    }

    public static boolean hasActivePet(UUID playerUuid) {
        return activePets.containsKey(playerUuid);
    }

    public static void trySpawnOnJoin(HypixelPlayer player, Instance instance) {
        PlayerPetData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class).getValue();
        if (data.getActivePetId() == null) return;

        try {
            LobbyPetData petData = LobbyPetData.valueOf(data.getActivePetId());
            if (data.ownsPet(petData.name())) {
                LobbyPetEntity entity = new LobbyPetEntity(
                    petData, player.getUuid(),
                    resolveDisplayName(data, petData)
                );
                entity.spawnForOwner(instance, player.getPosition());
                activePets.put(player.getUuid(), entity);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    private static String resolveDisplayName(PlayerPetData data, LobbyPetData petData) {
        PlayerPetData.PetInstance inst = data.getPetInstance(petData.name());
        if (inst != null && inst.getCustomName() != null) {
            return inst.getCustomName();
        }
        return petData.getDisplayName();
    }

    public static void cleanupAll() {
        activePets.values().forEach(entity -> {
            if (!entity.isRemoved()) entity.remove();
        });
        activePets.clear();
    }
}
