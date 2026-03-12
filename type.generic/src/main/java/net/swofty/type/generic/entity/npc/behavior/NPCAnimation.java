package net.swofty.type.generic.entity.npc.behavior;

public record NPCAnimation(Type type) {

    public enum Type {
        SWING_MAIN_HAND,
        SWING_OFF_HAND
    }
}
