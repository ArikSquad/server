package net.swofty.type.generic.entity.npc.runtime;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;

import java.util.concurrent.CompletableFuture;

final class NPCNavigationAgent extends EntityCreature {

    NPCNavigationAgent() {
        super(EntityType.ZOMBIE);
        setInvisible(true);
        setNoGravity(false);
        setAutoViewable(false);
        setCustomNameVisible(false);
        setSilent(true);
    }

    CompletableFuture<Void> attach(Instance instance, Pos position) {
        return setInstance(instance, position);
    }
}
