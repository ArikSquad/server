package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.PlayerTickInformation;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.AnticheatPacketEvent;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.event.packet.AbilitiesPacket;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.math.Vel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlightFlag extends Flag {
    private static final double MAX_VERTICAL_SPEED = 0.42;
    private static final double SUSTAINED_ASCENT_Y = 0.05;
    private static final double HOVER_Y = 0.02;
    private static final Map<UUID, Integer> violationBuffer = new HashMap<>();

    @ListenerMethod
    public void onPacket(AnticheatPacketEvent event) {
        if (event.packet() instanceof AbilitiesPacket abilities) {
            SwoftyPlayer player = SwoftyPlayer.players.get(abilities.getPlayer().getUuid());
            if (player != null) {
                player.updateAbilities(abilities.isFlying(), abilities.isAllowFlight(), abilities.isCreativeMode());
            }
        }
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();
        UUID uuid = player.getUuid();

        if (player.shouldBypassMovementChecks() || player.hasMovementGrace()) {
            violationBuffer.remove(uuid);
            return;
        }

        Vel currentVel = event.getCurrentTick().getVel();
        boolean onGround = event.getCurrentTick().isOnGround();
        double horizontalSpeed = Math.hypot(currentVel.x(), currentVel.z());
        int airTicks = countAirTicks(player.getLastTicks());
        int violations = Math.max(0, violationBuffer.getOrDefault(uuid, 0) - 1);

        if (!onGround && currentVel.y() > MAX_VERTICAL_SPEED * 1.5) {
            if (airTicks > 5) {
                violations += 4;
            }
        }

        if (!onGround && airTicks >= 6 && currentVel.y() > SUSTAINED_ASCENT_Y) {
            violations += 2;
        }

        if (!onGround && airTicks >= 12 && Math.abs(currentVel.y()) <= HOVER_Y && horizontalSpeed < 0.35) {
            violations += 2;
        }

        if (!onGround && airTicks >= 10) {
            double avgYVel = calculateAverageYVelocity(player.getLastTicks(), 6);
            if (avgYVel > -0.01) {
                violations += 2;
            }
        }

        if (violations >= 4) {
            double certainty = Math.min(0.97, 0.62 + violations * 0.06 + Math.max(0.0, currentVel.y()) * 0.25);
            player.flag(FlagType.FLIGHT, certainty);
            violations = Math.max(0, violations - 2);
        }

        violationBuffer.put(uuid, violations);
    }

    private int countAirTicks(java.util.List<PlayerTickInformation> ticks) {
        int count = 0;
        for (int i = ticks.size() - 1; i >= 0 && i >= ticks.size() - 10; i--) {
            if (!ticks.get(i).isOnGround()) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private double calculateAverageYVelocity(java.util.List<PlayerTickInformation> ticks, int count) {
        if (ticks.isEmpty()) return 0;
        double sum = 0;
        int actualCount = 0;
        for (int i = ticks.size() - 1; i >= 0 && actualCount < count; i--) {
            sum += ticks.get(i).getVel().y();
            actualCount++;
        }
        return actualCount > 0 ? sum / actualCount : 0;
    }
}
