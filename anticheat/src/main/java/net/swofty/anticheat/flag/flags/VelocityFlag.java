package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerKnockbackEvent;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Vel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VelocityFlag extends Flag {
    private static final int MAX_CHECK_TICKS = 4;
    private static final int MIN_TICKS_BEFORE_EVALUATION = 2;
    private static final double MIN_EXPECTED_HORIZONTAL = 0.05;
    private static final double REQUIRED_HORIZONTAL_RATIO = 0.55;
    private static final double REQUIRED_VERTICAL_RATIO = 0.5;

    private static final Map<UUID, KnockbackData> expectedKnockback = new HashMap<>();

    private static class KnockbackData {
        private final Vel expectedVelocity;
        private int ticksObserved;

        KnockbackData(Vel velocity) {
            this.expectedVelocity = velocity;
        }
    }

    @ListenerMethod
    public void onPlayerKnockback(PlayerKnockbackEvent event) {
        UUID uuid = event.getPlayer().getUuid();
        expectedKnockback.put(uuid, new KnockbackData(event.getKnockbackVelocity()));
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();
        UUID uuid = player.getUuid();

        if (!expectedKnockback.containsKey(uuid)) {
            return;
        }

        KnockbackData data = expectedKnockback.get(uuid);

        if (player.shouldBypassMovementChecks()) {
            expectedKnockback.remove(uuid);
            return;
        }

        data.ticksObserved++;
        if (data.ticksObserved < MIN_TICKS_BEFORE_EVALUATION) {
            return;
        }
        if (data.ticksObserved > MAX_CHECK_TICKS) {
            expectedKnockback.remove(uuid);
            return;
        }

        Vel actualVelocity = event.getCurrentTick().getVel();
        Vel expectedVel = data.expectedVelocity;

        double expectedHorizontal = Math.hypot(expectedVel.x(), expectedVel.z());
        double actualHorizontal = Math.hypot(actualVelocity.x(), actualVelocity.z());

        if (expectedHorizontal >= MIN_EXPECTED_HORIZONTAL) {
            double velocityRatio = actualHorizontal / expectedHorizontal;

            if (velocityRatio < REQUIRED_HORIZONTAL_RATIO) {
                double reduction = 1.0 - velocityRatio;
                double certainty = Math.min(0.95, 0.45 + reduction * 0.6);

                player.flag(net.swofty.anticheat.flag.FlagType.VELOCITY, certainty);
                expectedKnockback.remove(uuid);
                return;
            }
        }

        if (Math.abs(expectedVel.y()) > 0.1) {
            double verticalRatio = actualVelocity.y() / expectedVel.y();

            if (verticalRatio < REQUIRED_VERTICAL_RATIO) {
                double certainty = Math.min(0.9, 0.5 + (1.0 - verticalRatio) * 0.5);
                player.flag(net.swofty.anticheat.flag.FlagType.VELOCITY, certainty);
                expectedKnockback.remove(uuid);
            }
        }
    }
}
