package net.swofty.type.generic.entity.npc.behavior;

import net.minestom.server.coordinate.Pos;

import java.util.List;

public record NPCBehaviorState(
    Pos position,
    NPCLoadout loadout,
    float headYawOffset,
    double verticalOffset,
    boolean blocksAutoLook,
    List<NPCAnimation> animations
) {
    public NPCBehaviorState {
        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }
        loadout = loadout == null ? NPCLoadout.EMPTY : loadout;
        animations = animations == null ? List.of() : List.copyOf(animations);
    }

    public Pos renderedPosition() {
        return position.add(0, verticalOffset, 0);
    }
}
