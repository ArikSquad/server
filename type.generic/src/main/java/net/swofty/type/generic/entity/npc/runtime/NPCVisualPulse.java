package net.swofty.type.generic.entity.npc.runtime;

public record NPCVisualPulse(Type type) {

    public enum Type {
        SWING_MAIN_HAND,
        SWING_OFF_HAND
    }
}
