package net.swofty.type.generic.entity.npc.behavior;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.configuration.NPCConfiguration;

import java.util.ArrayList;
import java.util.List;

public final class NPCBehaviorController {
    private static final int ANIMATION_RETENTION_TICKS = 3;

    private final boolean enabled;
    private final Pos startPosition;
    private final NPCBehavior behavior;
    private final List<RetainedAnimation> retainedAnimations = new ArrayList<>();

    private Pos position;
    private NPCLoadout loadoutOverride = NPCLoadout.EMPTY;
    private long ticksLived;
    private int jumpTick;
    private int jumpDuration;
    private double jumpHeight;
    private float headYawOffset;
    private boolean blocksAutoLook;
    private NPCBehaviorState state;

    public NPCBehaviorController(NPCConfiguration configuration, NPCBehavior behavior) {
        this.enabled = configuration.supportsBehavior();
        this.startPosition = enabled ? configuration.behaviorStartPosition() : Pos.ZERO;
        this.behavior = behavior == null ? NPCBehavior.none() : behavior;
        this.position = startPosition;
        this.state = new NPCBehaviorState(position, loadoutOverride, 0, 0, false, List.of());
    }

    public synchronized void register() {
        reset();
    }

    public synchronized void unregister() {
        reset();
    }

    public synchronized void tick() {
        if (!enabled) {
            return;
        }

        ticksLived++;
        headYawOffset = 0;
        blocksAutoLook = false;

        behavior.tick(this);
        rebuildState(tickJump());
        ageAnimations();
    }

    public synchronized NPCBehaviorState state() {
        return state;
    }

    public synchronized long ticksLived() {
        return ticksLived;
    }

    public synchronized Pos position() {
        return position;
    }

    public synchronized void setPosition(Pos position) {
        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }
        this.position = position;
    }

    public synchronized void move(double x, double y, double z) {
        position = position.add(x, y, z);
    }

    public synchronized void setView(float yaw, float pitch) {
        position = new Pos(position.x(), position.y(), position.z(), normalizeYaw(yaw), pitch);
    }

    public synchronized void rotate(float yawDelta) {
        setView(position.yaw() + yawDelta, position.pitch());
    }

    public synchronized void jump(double heightBlocks, int durationTicks) {
        if (heightBlocks <= 0) {
            throw new IllegalArgumentException("heightBlocks must be positive");
        }
        if (durationTicks <= 0) {
            throw new IllegalArgumentException("durationTicks must be positive");
        }
        jumpHeight = heightBlocks;
        jumpDuration = durationTicks;
        jumpTick = 0;
    }

    public synchronized void swingMainHand() {
        retainedAnimations.add(new RetainedAnimation(new NPCAnimation(NPCAnimation.Type.SWING_MAIN_HAND), ANIMATION_RETENTION_TICKS));
    }

    public synchronized void swingOffHand() {
        retainedAnimations.add(new RetainedAnimation(new NPCAnimation(NPCAnimation.Type.SWING_OFF_HAND), ANIMATION_RETENTION_TICKS));
    }

    public synchronized void setHeadYawOffset(float headYawOffset) {
        this.headYawOffset = headYawOffset;
    }

    public synchronized void blockAutoLook() {
        blocksAutoLook = true;
    }

    public synchronized void setLoadout(NPCLoadout loadout) {
        loadoutOverride = loadout == null ? NPCLoadout.EMPTY : loadout;
        rebuildState(state.verticalOffset());
    }

    private void reset() {
        position = startPosition;
        loadoutOverride = NPCLoadout.EMPTY;
        ticksLived = 0;
        jumpTick = 0;
        jumpDuration = 0;
        jumpHeight = 0;
        headYawOffset = 0;
        blocksAutoLook = false;
        retainedAnimations.clear();
        rebuildState(0);
    }

    private double tickJump() {
        if (jumpDuration <= 0) {
            return 0;
        }

        double progress = (double) (jumpTick + 1) / (double) (jumpDuration + 1);
        double offset = Math.sin(Math.PI * progress) * jumpHeight;
        jumpTick++;
        if (jumpTick >= jumpDuration) {
            jumpTick = 0;
            jumpDuration = 0;
            jumpHeight = 0;
        }
        return offset;
    }

    private void rebuildState(double verticalOffset) {
        state = new NPCBehaviorState(
            position,
            loadoutOverride,
            headYawOffset,
            verticalOffset,
            blocksAutoLook,
            retainedAnimations.stream().map(RetainedAnimation::animation).toList()
        );
    }

    private void ageAnimations() {
        retainedAnimations.removeIf(animation -> animation.tick() <= 0);
    }

    private static float normalizeYaw(float yaw) {
        float normalized = yaw % 360f;
        return normalized < 0 ? normalized + 360f : normalized;
    }

    private static final class RetainedAnimation {
        private final NPCAnimation animation;
        private int ticksRemaining;

        private RetainedAnimation(NPCAnimation animation, int ticksRemaining) {
            this.animation = animation;
            this.ticksRemaining = ticksRemaining;
        }

        private NPCAnimation animation() {
            return animation;
        }

        private int tick() {
            return --ticksRemaining;
        }
    }
}
