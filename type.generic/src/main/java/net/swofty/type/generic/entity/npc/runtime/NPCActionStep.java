package net.swofty.type.generic.entity.npc.runtime;

public sealed interface NPCActionStep permits NPCActionStep.WaitStep, NPCActionStep.SwingStep,
        NPCActionStep.JumpStep, NPCActionStep.HeadShakeStep {

    int durationTicks();

    record WaitStep(int durationTicks) implements NPCActionStep {
    }

    record SwingStep(NPCAnimationHand hand, int durationTicks, int pulseIntervalTicks) implements NPCActionStep {
        public SwingStep {
            if (pulseIntervalTicks <= 0) {
                throw new IllegalArgumentException("pulseIntervalTicks must be positive");
            }
        }
    }

    record JumpStep(int durationTicks, double heightBlocks) implements NPCActionStep {
    }

    record HeadShakeStep(int durationTicks, float amplitudeDegrees, int oscillations) implements NPCActionStep {
        public HeadShakeStep {
            if (oscillations <= 0) {
                throw new IllegalArgumentException("oscillations must be positive");
            }
        }
    }
}
