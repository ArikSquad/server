package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.math.Vel;

public class SpeedFlag extends Flag {
    private static final double IMPOSSIBLE_HORIZONTAL_SPEED = 1.5;

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();

        // Skip checks for players with flight/creative abilities
        if (player.shouldBypassMovementChecks()) {
            return;
        }

        Vel currentVel = event.getCurrentTick().getVel();

        // Calculate horizontal speed (ignoring vertical movement)
        double horizontalSpeed = Math.hypot(currentVel.x(), currentVel.z());

        // Only flag for mathematically impossible speeds
        // This is very generous to avoid false positives from speed effects, knockback, etc.
        if (horizontalSpeed > IMPOSSIBLE_HORIZONTAL_SPEED) {
            player.flag(FlagType.SPEED, 0.95);
        }
    }
}
