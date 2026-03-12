package net.swofty.type.generic.entity.npc.runtime;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.NPCConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class NPCController {
    private static final double ARRIVAL_DISTANCE_SQUARED = 0.20 * 0.20;

    private final HypixelNPC npc;
    private final NPCConfiguration configuration;
    @Getter
    private final NPCBehaviorSpec behaviorSpec;
    private final NPCSequenceHandle sequenceHandle;
    private final Pos spawnPosition;
    private final boolean runtimeEnabled;

    private final Map<String, SequenceState> activeSequences = new LinkedHashMap<>();

    private @Nullable RouteState activeRoute;
    private @Nullable NPCLoadout loadoutOverride;
    private @Nullable NPCNavigationAgent navigationAgent;

    private boolean registered;
    private Pos logicalPosition;
    private int transientJumpTick;
    private int transientJumpDuration;
    private double transientJumpHeight;
    private NPCControllerSnapshot snapshot;

    public NPCController(HypixelNPC npc, NPCConfiguration configuration, NPCBehaviorSpec behaviorSpec) {
        this.npc = npc;
        this.configuration = configuration;
        this.behaviorSpec = behaviorSpec == null ? NPCBehaviorSpec.none() : behaviorSpec;
        this.sequenceHandle = new NPCSequenceHandle(this);
        this.runtimeEnabled = configuration.supportsRuntimeBehavior();
        this.spawnPosition = runtimeEnabled ? configuration.runtimeSpawnPosition() : Pos.ZERO;
        this.logicalPosition = spawnPosition;
        this.snapshot = new NPCControllerSnapshot(logicalPosition, resolvedRuntimeLoadout(), 0, 0, false, List.of());
    }

    public synchronized void register() {
        registered = true;
        if (runtimeEnabled) {
            ensureAgent();
            attachAgent();
        }
        startAutoRoute();
        startAutoSequences();
        rebuildSnapshot(List.of(), 0, 0, false);
    }

    public synchronized void unregister() {
        registered = false;
        activeSequences.clear();
        activeRoute = null;
        if (navigationAgent != null) {
            navigationAgent.remove();
            navigationAgent = null;
        }
        logicalPosition = spawnPosition;
        rebuildSnapshot(List.of(), 0, 0, false);
    }

    public synchronized void tick() {
        if (!runtimeEnabled) {
            return;
        }

        tickRoute();
        SequenceOutput output = tickSequences();
        double verticalOffset = Math.max(output.verticalOffset(), tickTransientJump());
        rebuildSnapshot(output.pulses(), output.headYawOffset(), verticalOffset, output.headLocked());
    }

    public synchronized NPCControllerSnapshot snapshot() {
        return snapshot;
    }

    public synchronized Pos currentPosition() {
        return snapshot.renderedPosition();
    }

    public NPCNavigationHandle navigation(String routeId) {
        return new NPCNavigationHandle(this, routeId);
    }

    public NPCSequenceHandle sequences() {
        return sequenceHandle;
    }

    public synchronized void setLoadout(NPCLoadout loadout) {
        this.loadoutOverride = loadout == null ? NPCLoadout.EMPTY : loadout;
        rebuildSnapshot(List.of(), snapshot.headYawOffset(), snapshot.verticalOffset(), snapshot.headLocked());
    }

    synchronized void startRoute(String routeId, boolean resumeExisting) {
        if (!runtimeEnabled) {
            return;
        }

        NPCRoute route = behaviorSpec.routes().get(routeId);
        if (route == null) {
            throw new IllegalArgumentException("Unknown NPC route: " + routeId);
        }

        if (resumeExisting && activeRoute != null && activeRoute.route.id().equals(routeId)) {
            activeRoute.paused = false;
            beginTravel(activeRoute, false);
            return;
        }

        activeRoute = new RouteState(route);
        logicalPosition = spawnPosition;
        syncAgentPosition(spawnPosition);
        beginTravel(activeRoute, true);
    }

    synchronized void stopRoute(String routeId) {
        if (activeRoute == null || !activeRoute.route.id().equals(routeId)) {
            return;
        }
        activeRoute.paused = true;
        activeRoute.pathing = false;
        if (navigationAgent != null) {
            navigationAgent.getNavigator().reset();
        }
    }

    synchronized void resumeRoute(String routeId) {
        startRoute(routeId, true);
    }

    synchronized void resetRoute(String routeId) {
        if (!runtimeEnabled) {
            return;
        }
        if (activeRoute != null && activeRoute.route.id().equals(routeId)) {
            activeRoute = new RouteState(activeRoute.route);
        }
        logicalPosition = spawnPosition;
        syncAgentPosition(spawnPosition);
        if (activeRoute != null && activeRoute.route.id().equals(routeId) && !activeRoute.paused) {
            beginTravel(activeRoute, true);
        }
        rebuildSnapshot(List.of(), 0, 0, false);
    }

    synchronized void startSequence(String id) {
        NPCSequence sequence = behaviorSpec.sequences().get(id);
        if (sequence == null) {
            throw new IllegalArgumentException("Unknown NPC sequence: " + id);
        }
        activeSequences.put(id, new SequenceState(sequence));
    }

    synchronized void stopSequence(String id) {
        activeSequences.remove(id);
    }

    synchronized void stopAllSequences() {
        activeSequences.clear();
    }

    private void startAutoRoute() {
        for (NPCRoute route : behaviorSpec.routes().values()) {
            if (route.activationMode() == ActivationMode.AUTO_START) {
                startRoute(route.id(), false);
                return;
            }
        }
    }

    private void startAutoSequences() {
        for (NPCSequence sequence : behaviorSpec.sequences().values()) {
            if (sequence.activationMode() == ActivationMode.AUTO_START) {
                activeSequences.put(sequence.id(), new SequenceState(sequence));
            }
        }
    }

    private void tickRoute() {
        if (activeRoute == null || activeRoute.paused) {
            return;
        }

        if (activeRoute.waitTicks > 0) {
            activeRoute.waitTicks--;
            if (activeRoute.waitTicks == 0) {
                advanceRoute(activeRoute);
            }
            return;
        }

        RouteWaypoint waypoint = activeRoute.currentWaypoint();
        if (!activeRoute.pathing) {
            beginTravel(activeRoute, false);
        }

        if (navigationAgent != null && navigationAgent.getInstance() != null) {
            logicalPosition = navigationAgent.getPosition();
            if (isAt(logicalPosition, waypoint.position()) || navigationAgent.getNavigator().isComplete()) {
                logicalPosition = waypoint.resolvedPosition();
                syncAgentPosition(logicalPosition);
                onWaypointReached(activeRoute);
            }
            return;
        }

        logicalPosition = moveTowards(logicalPosition, waypoint.resolvedPosition(), activeRoute.route.movementSpeed());
        if (isAt(logicalPosition, waypoint.position())) {
            logicalPosition = waypoint.resolvedPosition();
            onWaypointReached(activeRoute);
        }
    }

    private void beginTravel(RouteState routeState, boolean resetNavigator) {
        RouteWaypoint waypoint = routeState.currentWaypoint();
        routeState.pathing = true;
        if (navigationAgent == null || navigationAgent.getInstance() == null) {
            return;
        }

        if (resetNavigator) {
            navigationAgent.getNavigator().reset();
        }
        navigationAgent.getNavigator().setPathTo(waypoint.position(), routeState.route.movementSpeed());
    }

    private void onWaypointReached(RouteState routeState) {
        routeState.pathing = false;

        if (routeState.currentWaypoint().jumpHint()) {
            transientJumpTick = 0;
            transientJumpDuration = 8;
            transientJumpHeight = 0.35;
        }

        int dwellTicks = routeState.currentWaypoint().dwellTicks();
        if (dwellTicks > 0) {
            routeState.waitTicks = dwellTicks;
            return;
        }

        advanceRoute(routeState);
    }

    private void advanceRoute(RouteState routeState) {
        int lastIndex = routeState.route.waypoints().size() - 1;
        int nextIndex = routeState.index + routeState.direction;

        switch (routeState.route.routeMode()) {
            case ONCE -> {
                if (nextIndex > lastIndex) {
                    routeState.paused = true;
                    routeState.pathing = false;
                    return;
                }
            }
            case LOOP -> {
                if (nextIndex > lastIndex) {
                    nextIndex = 0;
                } else if (nextIndex < 0) {
                    nextIndex = lastIndex;
                }
            }
            case PING_PONG -> {
                if (nextIndex > lastIndex) {
                    routeState.direction = -1;
                    nextIndex = Math.max(lastIndex - 1, 0);
                } else if (nextIndex < 0) {
                    routeState.direction = 1;
                    nextIndex = Math.min(1, lastIndex);
                }
            }
        }

        routeState.index = nextIndex;
        routeState.waitTicks = 0;
        beginTravel(routeState, true);
    }

    private SequenceOutput tickSequences() {
        if (activeSequences.isEmpty()) {
            return new SequenceOutput(List.of(), 0, 0, false);
        }

        List<NPCVisualPulse> pulses = new ArrayList<>();
        float headYawOffset = 0;
        double verticalOffset = 0;
        boolean headLocked = false;
        List<String> finished = new ArrayList<>();

        for (Map.Entry<String, SequenceState> entry : activeSequences.entrySet()) {
            SequenceState state = entry.getValue();
            NPCActionStep step = state.currentStep();
            int duration = Math.max(step.durationTicks(), 1);
            double normalizedProgress = duration == 1 ? 1 : (double) state.tickInStep / (double) (duration - 1);

            switch (step) {
                case NPCActionStep.WaitStep ignored -> {
                }
                case NPCActionStep.SwingStep swingStep -> {
                    if (state.tickInStep % swingStep.pulseIntervalTicks() == 0) {
                        pulses.add(new NPCVisualPulse(
                                swingStep.hand() == NPCAnimationHand.MAIN_HAND
                                        ? NPCVisualPulse.Type.SWING_MAIN_HAND
                                        : NPCVisualPulse.Type.SWING_OFF_HAND
                        ));
                    }
                }
                case NPCActionStep.JumpStep jumpStep -> {
                    verticalOffset = Math.max(verticalOffset,
                            Math.sin(Math.PI * normalizedProgress) * jumpStep.heightBlocks());
                }
                case NPCActionStep.HeadShakeStep headShakeStep -> {
                    headLocked = true;
                    headYawOffset += (float) (Math.sin(normalizedProgress * Math.PI * 2 * headShakeStep.oscillations())
                            * headShakeStep.amplitudeDegrees());
                }
            }

            state.tickInStep++;
            if (state.tickInStep >= duration) {
                state.tickInStep = 0;
                state.stepIndex++;
                if (state.stepIndex >= state.sequence.steps().size()) {
                    if (state.sequence.loop()) {
                        state.stepIndex = 0;
                    } else {
                        finished.add(entry.getKey());
                    }
                }
            }
        }

        finished.forEach(activeSequences::remove);
        return new SequenceOutput(pulses, headYawOffset, verticalOffset, headLocked);
    }

    private double tickTransientJump() {
        if (transientJumpTick >= transientJumpDuration || transientJumpDuration <= 0) {
            transientJumpTick = 0;
            transientJumpDuration = 0;
            transientJumpHeight = 0;
            return 0;
        }

        double progress = transientJumpDuration == 1
                ? 1
                : (double) transientJumpTick / (double) (transientJumpDuration - 1);
        transientJumpTick++;
        return Math.sin(Math.PI * progress) * transientJumpHeight;
    }

    private void ensureAgent() {
        if (navigationAgent != null || !runtimeEnabled) {
            return;
        }
        navigationAgent = new NPCNavigationAgent();
    }

    private void attachAgent() {
        if (navigationAgent == null || !registered) {
            return;
        }

        Instance instance = configuration.instance();
        if (instance == null) {
            return;
        }
        navigationAgent.attach(instance, logicalPosition);
    }

    private void syncAgentPosition(Pos position) {
        if (navigationAgent == null || navigationAgent.getInstance() == null) {
            return;
        }
        navigationAgent.teleport(position);
    }

    private void rebuildSnapshot(List<NPCVisualPulse> pulses, float headYawOffset, double verticalOffset, boolean headLocked) {
        Pos base = runtimeEnabled ? logicalPosition : spawnPosition;
        snapshot = new NPCControllerSnapshot(base, resolvedRuntimeLoadout(), headYawOffset, verticalOffset, headLocked, pulses);
    }

    private NPCLoadout resolvedRuntimeLoadout() {
        if (loadoutOverride != null) {
            return loadoutOverride;
        }
        return behaviorSpec.defaultLoadout();
    }

    private static boolean isAt(Pos current, Pos target) {
        return current.distanceSquared(target) <= ARRIVAL_DISTANCE_SQUARED;
    }

    private static Pos moveTowards(Pos current, Pos target, double movementSpeed) {
        double step = Math.max(movementSpeed * 0.05, 0.05);
        double dx = target.x() - current.x();
        double dy = target.y() - current.y();
        double dz = target.z() - current.z();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance <= step || distance == 0) {
            return target;
        }

        double ratio = step / distance;
        double nextX = current.x() + (dx * ratio);
        double nextY = current.y() + (dy * ratio);
        double nextZ = current.z() + (dz * ratio);
        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = target.pitch();
        return new Pos(nextX, nextY, nextZ, yaw, pitch);
    }

    private static final class RouteState {
        private final NPCRoute route;
        private int index;
        private int direction = 1;
        private int waitTicks;
        private boolean paused;
        private boolean pathing;

        private RouteState(NPCRoute route) {
            this.route = route;
        }

        private RouteWaypoint currentWaypoint() {
            return route.waypoints().get(index);
        }
    }

    private static final class SequenceState {
        private final NPCSequence sequence;
        private int stepIndex;
        private int tickInStep;

        private SequenceState(NPCSequence sequence) {
            this.sequence = sequence;
        }

        private NPCActionStep currentStep() {
            return sequence.steps().get(stepIndex);
        }
    }

    private record SequenceOutput(List<NPCVisualPulse> pulses, float headYawOffset, double verticalOffset,
                                  boolean headLocked) {
    }
}
