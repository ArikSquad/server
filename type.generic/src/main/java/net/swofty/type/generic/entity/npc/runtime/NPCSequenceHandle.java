package net.swofty.type.generic.entity.npc.runtime;

public final class NPCSequenceHandle {
    private final NPCController controller;

    NPCSequenceHandle(NPCController controller) {
        this.controller = controller;
    }

    public void start(String id) {
        controller.startSequence(id);
    }

    public void stop(String id) {
        controller.stopSequence(id);
    }

    public void stopAll() {
        controller.stopAllSequences();
    }
}
