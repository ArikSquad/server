package net.swofty.anticheat.flag;

import net.swofty.anticheat.loader.SwoftyAnticheat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlagManager {
    private static final double MIN_CERTAINTY = 0.0;
    private static final double MAX_CERTAINTY = 1.0;
    private static final double PUNISHMENT_THRESHOLD = 0.9;
    private static final long FLAG_EXPIRATION_TIME = 1000;

    private final UUID uuid;
    private final Map<FlagType, List<Flag>> flags;

    public FlagManager(UUID uuid, Map<FlagType, List<Flag>> flags) {
        this.uuid = uuid;
        this.flags = flags;
    }

    public void addFlag(FlagType flagType, double certainty) {
        Flag flag = flagType.getFlagSupplier().get();
        flag.setCertainty(clampCertainty(certainty));

        flags.computeIfAbsent(flagType, _ -> new ArrayList<>()).add(flag);
        calculateOverallCertainty(flagType);
    }

    private void calculateOverallCertainty(FlagType flagType) {
        List<Flag> flagList = flags.get(flagType);
        long currentTime = System.currentTimeMillis();

        // Remove expired flags
        flagList.removeIf(flag -> currentTime - flag.getTimestamp() > FLAG_EXPIRATION_TIME);

        if (flagList.isEmpty()) return;

        // Calculate overall certainty
        double overallCertainty = flagList.stream()
                .mapToDouble(Flag::getCertainty)
                .reduce(1, (a, b) -> a * (1 - b));
        overallCertainty = 1 - overallCertainty;

        if (overallCertainty > PUNISHMENT_THRESHOLD) {
            SwoftyAnticheat.getPunishmentHandler().onFlag(uuid, flagType);
        }
    }

    private double clampCertainty(double certainty) {
        if (Double.isNaN(certainty) || Double.isInfinite(certainty)) {
            return MIN_CERTAINTY;
        }

        return Math.max(MIN_CERTAINTY, Math.min(MAX_CERTAINTY, certainty));
    }
}
