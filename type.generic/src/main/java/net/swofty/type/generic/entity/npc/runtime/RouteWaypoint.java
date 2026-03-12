package net.swofty.type.generic.entity.npc.runtime;

import net.minestom.server.coordinate.Pos;

public record RouteWaypoint(
        Pos position,
        int dwellTicks,
        Float yaw,
        Float pitch,
        boolean jumpHint
) {
    public RouteWaypoint {
        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }
    }

    public static RouteWaypoint of(Pos position) {
        return new RouteWaypoint(position, 0, null, null, false);
    }

    public Pos resolvedPosition() {
        return new Pos(
                position.x(),
                position.y(),
                position.z(),
                yaw != null ? yaw : position.yaw(),
                pitch != null ? pitch : position.pitch()
        );
    }
}
