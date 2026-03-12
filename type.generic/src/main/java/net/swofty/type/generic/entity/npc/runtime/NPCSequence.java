package net.swofty.type.generic.entity.npc.runtime;

import java.util.List;

public record NPCSequence(
        String id,
        List<NPCActionStep> steps,
        ActivationMode activationMode,
        boolean loop
) {
    public NPCSequence {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank");
        }
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("steps cannot be empty");
        }
        steps = List.copyOf(steps);
        if (activationMode == null) {
            activationMode = ActivationMode.MANUAL;
        }
    }
}
