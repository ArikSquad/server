package net.swofty.type.generic.entity.npc.runtime;

public final class NPCNavigationHandle {
    private final NPCController controller;
    private final String routeId;

    NPCNavigationHandle(NPCController controller, String routeId) {
        this.controller = controller;
        this.routeId = routeId;
    }

    public void start() {
        controller.startRoute(routeId, false);
    }

    public void stop() {
        controller.stopRoute(routeId);
    }

    public void resume() {
        controller.resumeRoute(routeId);
    }

    public void reset() {
        controller.resetRoute(routeId);
    }
}
