package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.math.Vel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedFlag extends Flag {
    private static final double BLINK_DISTANCE = 3.25;
    private static final double MAX_GROUND_SPEED = 0.82;
    private static final double MAX_AIR_SPEED = 0.95;
    private static final double LONG_JUMP_SPEED = 1.05;
    private static final Map<UUID, Integer> violationBuffer = new HashMap<>();

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();
        UUID uuid = player.getUuid();

        if (player.shouldBypassMovementChecks() || player.hasMovementGrace()) {
            violationBuffer.remove(uuid);
            return;
        }

        if (event.getPreviousTick() == null) {
            return;
        }

        Vel currentVel = event.getCurrentTick().getVel();
        Vel previousVel = event.getPreviousTick().getVel();

        double horizontalSpeed = Math.hypot(currentVel.x(), currentVel.z());
        double previousHorizontalSpeed = Math.hypot(previousVel.x(), previousVel.z());
        double dx = event.getCurrentTick().getPos().x() - event.getPreviousTick().getPos().x();
        double dy = event.getCurrentTick().getPos().y() - event.getPreviousTick().getPos().y();
        double dz = event.getCurrentTick().getPos().z() - event.getPreviousTick().getPos().z();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > BLINK_DISTANCE) {
            player.flag(FlagType.SPEED, 0.99);
            return;
        }

        int violations = Math.max(0, violationBuffer.getOrDefault(uuid, 0) - 1);

        if (event.getCurrentTick().isOnGround()) {
            if (horizontalSpeed > MAX_GROUND_SPEED) {
                violations += horizontalSpeed > MAX_GROUND_SPEED + 0.2 ? 3 : 2;
            }
        } else {
            if (horizontalSpeed > MAX_AIR_SPEED) {
                violations += horizontalSpeed > MAX_AIR_SPEED + 0.2 ? 3 : 2;
            }

            if (horizontalSpeed > LONG_JUMP_SPEED && dy > 0.18) {
                violations += 2;
            }
        }

        if (horizontalSpeed - previousHorizontalSpeed > 0.35 && horizontalSpeed > 0.9) {
            violations += 2;
        }

        if (violations >= 4) {
            double certainty = Math.min(0.98, 0.6 + (horizontalSpeed - MAX_GROUND_SPEED) * 0.35 + violations * 0.03);
            player.flag(FlagType.SPEED, certainty);
            violations = Math.max(0, violations - 2);
        }

        violationBuffer.put(uuid, violations);
    }
}
