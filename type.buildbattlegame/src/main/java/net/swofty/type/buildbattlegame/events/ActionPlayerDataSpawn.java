package net.swofty.type.buildbattlegame.events;

import net.minestom.server.entity.EntityType;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.pvp.projectile.BowModule;
import net.swofty.pvp.projectile.entities.ArrowProjectile;
import net.swofty.type.buildbattlegame.user.BuildBattlePlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        final BuildBattlePlayer player = (BuildBattlePlayer) event.getPlayer();

        // Registers the bow module for this player
        new BowModule(player.eventNode(), (p, i) -> new ArrowProjectile(EntityType.ARROW, p));
    }
}
