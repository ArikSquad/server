package net.swofty.type.generic.entity.npc.runtime;

import java.util.List;

public record NPCRoute(
        String id,
        List<RouteWaypoint> waypoints,
        double movementSpeed,
        RouteMode routeMode,
        ActivationMode activationMode
) {
    public NPCRoute {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank");
        }
        if (waypoints == null || waypoints.isEmpty()) {
            throw new IllegalArgumentException("waypoints cannot be empty");
        }
        waypoints = List.copyOf(waypoints);
        if (movementSpeed <= 0) {
            throw new IllegalArgumentException("movementSpeed must be positive");
        }
        if (routeMode == null) {
            routeMode = RouteMode.ONCE;
        }
        if (activationMode == null) {
            activationMode = ActivationMode.MANUAL;
        }
    }
}
