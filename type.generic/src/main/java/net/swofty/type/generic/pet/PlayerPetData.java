package net.swofty.type.generic.pet;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PlayerPetData {
    private Map<String, PetInstance> ownedPets = new HashMap<>();
    private String activePetId = null;

    public PlayerPetData() {
    }

    public boolean ownsPet(String petId) {
        return ownedPets.containsKey(petId);
    }

    public PetInstance getPetInstance(String petId) {
        return ownedPets.get(petId);
    }

    public PetInstance getActivePetInstance() {
        if (activePetId == null) return null;
        return ownedPets.get(activePetId);
    }

    public void addPet(String petId) {
        if (!ownedPets.containsKey(petId)) {
            ownedPets.put(petId, new PetInstance());
        }
    }

    public void summonPet(String petId) {
        if (ownedPets.containsKey(petId)) {
            this.activePetId = petId;
        }
    }

    public void despawnPet() {
        this.activePetId = null;
    }

    @Getter
    @Setter
    public static class PetInstance {
        private int level = 1;
        private int exp = 0;
        private int hunger = 0;
        private int thirst = 0;
        private int exercise = 0;
        private String customName = null;
        private long lastMissionTime = 0;

        public PetInstance() {
        }

        public boolean canDoMission() {
            int average = (hunger + thirst + exercise) / 3;
            if (average < 30) return false;
            return System.currentTimeMillis() - lastMissionTime >= 3600000;
        }

        public long getMissionCooldownRemaining() {
            long elapsed = System.currentTimeMillis() - lastMissionTime;
            return Math.max(0, 3600000 - elapsed);
        }

        public void completeMission(double statusMultiplier) {
            int baseExp = 50 + (level * 10);
            int earned = (int) (baseExp * statusMultiplier);
            addExp(earned);
            lastMissionTime = System.currentTimeMillis();
        }

        public void addExp(int amount) {
            exp += amount;
            while (exp >= getExpForNextLevel()) {
                exp -= getExpForNextLevel();
                level++;
            }
        }

        public int getExpForNextLevel() {
            return 100 + (level * 50);
        }

        public void feed(boolean isFavorite) {
            hunger = Math.min(100, hunger + (isFavorite ? 15 : 8));
        }

        public void giveWater(boolean isFavorite) {
            thirst = Math.min(100, thirst + (isFavorite ? 15 : 8));
        }

        public void giveExercise(boolean isFavorite) {
            exercise = Math.min(100, exercise + (isFavorite ? 15 : 8));
        }

        public String getStatusName() {
            int average = (hunger + thirst + exercise) / 3;
            if (average >= 80) return "Super Happy";
            if (average >= 55) return "Very Happy";
            if (average >= 30) return "Happy";
            return "Okay";
        }

        public String getStatusColor() {
            int average = (hunger + thirst + exercise) / 3;
            if (average >= 80) return "§6";
            if (average >= 55) return "§2";
            if (average >= 30) return "§a";
            return "§e";
        }

        public double getStatusMultiplier() {
            int average = (hunger + thirst + exercise) / 3;
            if (average >= 80) return 2.0;
            if (average >= 55) return 1.5;
            if (average >= 30) return 1.25;
            return 1.0;
        }
    }
}
