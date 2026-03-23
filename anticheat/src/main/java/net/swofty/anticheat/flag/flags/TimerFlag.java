package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerFlag extends Flag {
    private static final Map<UUID, SampleWindow> sampleWindows = new HashMap<>();
    private static final long EXPECTED_TICK_MS = 50;
    private static final long MIN_SUSPICIOUS_TICK_MS = 35;
    private static final int SAMPLE_SIZE = 20;
    private static final int MIN_SUSPICIOUS_SAMPLES = 16;

    private static final class SampleWindow {
        private final Deque<Long> packetIntervals = new ArrayDeque<>();
        private long lastPacketTime = -1L;

        void record(long now) {
            if (lastPacketTime != -1L) {
                packetIntervals.addLast(now - lastPacketTime);
                if (packetIntervals.size() > SAMPLE_SIZE) {
                    packetIntervals.removeFirst();
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
            return packetIntervals.stream().filter(interval -> interval <= MIN_SUSPICIOUS_TICK_MS).count();
        }
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();
        UUID uuid = player.getUuid();
        long currentTime = System.currentTimeMillis();

        if (player.shouldBypassMovementChecks()) {
            return;
        }

        SampleWindow window = sampleWindows.computeIfAbsent(uuid, ignored -> new SampleWindow());
        window.record(currentTime);

        if (!window.isFull()) {
            return;
        }

        double averageInterval = window.averageInterval();
        long suspiciousSamples = window.suspiciousSampleCount();
        if (averageInterval >= MIN_SUSPICIOUS_TICK_MS || suspiciousSamples < MIN_SUSPICIOUS_SAMPLES) {
            return;
        }

        double speedMultiplier = EXPECTED_TICK_MS / averageInterval;
        double certainty = Math.min(0.95, 0.55 + (speedMultiplier - 1.0) * 0.2);
        player.flag(net.swofty.anticheat.flag.FlagType.TIMER, certainty);
    }
}
