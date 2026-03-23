package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.AnticheatPacketEvent;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.event.packet.IsOnGroundPacket;
import net.swofty.anticheat.event.packet.PositionAndRotationPacket;
import net.swofty.anticheat.event.packet.PositionPacket;
import net.swofty.anticheat.event.packet.RotationPacket;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.flag.FlagType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerFlag extends Flag {
    private static final Map<UUID, SampleWindow> sampleWindows = new HashMap<>();
    private static final Map<UUID, Integer> violationBuffer = new HashMap<>();
    private static final long EXPECTED_TICK_MS = 50L;
    private static final long MAX_SUSPICIOUS_AVERAGE_MS = 38L;
    private static final long MAX_SUSPICIOUS_INTERVAL_MS = 40L;
    private static final int SAMPLE_SIZE = 30;
    private static final int MIN_SUSPICIOUS_SAMPLES = 24;

    private static final class SampleWindow {
        private final Deque<Long> packetIntervals = new ArrayDeque<>();
        private long lastPacketTime = -1L;

        void record(long now) {
            if (lastPacketTime != -1L) {
                long interval = now - lastPacketTime;
                if (interval > EXPECTED_TICK_MS * 5) {
                    packetIntervals.clear();
                } else {
                    packetIntervals.addLast(interval);
                    if (packetIntervals.size() > SAMPLE_SIZE) {
                        packetIntervals.removeFirst();
                    }
                }
            }
            lastPacketTime = now;
        }

        boolean isFull() {
            return packetIntervals.size() == SAMPLE_SIZE;
        }

        double averageInterval() {
            return packetIntervals.stream().mapToLong(Long::longValue).average().orElse(EXPECTED_TICK_MS);
        }

        long suspiciousSampleCount() {
            return packetIntervals.stream().filter(interval -> interval <= MAX_SUSPICIOUS_INTERVAL_MS).count();
        }

        void reset() {
            packetIntervals.clear();
            lastPacketTime = -1L;
        }
    }

    @ListenerMethod
    public void onPacket(AnticheatPacketEvent event) {
        if (!isMovementPacket(event.packet())) {
            return;
        }

        SwoftyPlayer player = event.packet().getPlayer();
        if (player == null) {
            return;
        }

        UUID uuid = player.getUuid();
        long currentTime = System.currentTimeMillis();

        if (player.shouldBypassMovementChecks() || player.hasMovementGrace()) {
            sampleWindows.remove(uuid);
            violationBuffer.remove(uuid);
            return;
        }

        SampleWindow window = sampleWindows.computeIfAbsent(uuid, ignored -> new SampleWindow());
        window.record(currentTime);

        if (!window.isFull()) {
            return;
        }

        double averageInterval = window.averageInterval();
        long suspiciousSamples = window.suspiciousSampleCount();
        int violations = Math.max(0, violationBuffer.getOrDefault(uuid, 0) - 1);

        if (averageInterval <= MAX_SUSPICIOUS_AVERAGE_MS && suspiciousSamples >= MIN_SUSPICIOUS_SAMPLES) {
            violations += averageInterval <= 25 ? 3 : 2;
        }

        if (violations >= 4) {
            double speedMultiplier = EXPECTED_TICK_MS / averageInterval;
            double certainty = Math.min(0.99, 0.6 + (speedMultiplier - 1.0) * 0.25);
            player.flag(FlagType.TIMER, certainty);
            violations = Math.max(0, violations - 2);
        }

        violationBuffer.put(uuid, violations);
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();
        UUID uuid = player.getUuid();

        if (player.hasMovementGrace()) {
            SampleWindow window = sampleWindows.get(uuid);
            if (window != null) {
                window.reset();
            }
            violationBuffer.remove(uuid);
            return;
        }
    }

    private boolean isMovementPacket(Object packet) {
        return packet instanceof PositionPacket
            || packet instanceof PositionAndRotationPacket
            || packet instanceof RotationPacket
            || packet instanceof IsOnGroundPacket;
    }
}
