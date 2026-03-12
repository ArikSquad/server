package net.swofty.type.generic.entity.npc.behavior;

@FunctionalInterface
public interface NPCBehavior {

    NPCBehavior NONE = controller -> {
    };

    void tick(NPCBehaviorController controller);

    static NPCBehavior none() {
        return NONE;
    }
}
