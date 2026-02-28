package net.swofty.type.lobby.cosmetics;

import lombok.Getter;

@Getter
public enum PetStatus {
    OKAY("Okay", "§e", 1.0),
    HAPPY("Happy", "§a", 1.25),
    VERY_HAPPY("Very Happy", "§2", 1.5),
    SUPER_HAPPY("Super Happy", "§6", 2.0);

    private final String displayName;
    private final String colorCode;
    private final double missionMultiplier;

    PetStatus(String displayName, String colorCode, double missionMultiplier) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.missionMultiplier = missionMultiplier;
    }

    public static PetStatus fromRatings(int hunger, int thirst, int exercise) {
        int average = (hunger + thirst + exercise) / 3;
        if (average >= 80) return SUPER_HAPPY;
        if (average >= 55) return VERY_HAPPY;
        if (average >= 30) return HAPPY;
        return OKAY;
    }
}
