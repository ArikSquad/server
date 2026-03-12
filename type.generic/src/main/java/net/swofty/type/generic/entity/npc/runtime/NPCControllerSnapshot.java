package net.swofty.type.generic.entity.npc.runtime;

import net.minestom.server.coordinate.Pos;

import java.util.List;

public record NPCControllerSnapshot(
        Pos position,
        NPCLoadout loadout,
        float headYawOffset,
        double verticalOffset,
        boolean headLocked,
        List<NPCVisualPulse> pulses
) {
    public NPCControllerSnapshot {
        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }
        loadout = loadout == null ? NPCLoadout.EMPTY : loadout;
        pulses = pulses == null ? List.of() : List.copyOf(pulses);
    }

    public Pos renderedPosition() {
        return position.add(0, verticalOffset, 0);
    }
}
