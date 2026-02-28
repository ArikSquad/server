package net.swofty.type.lobby.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.cosmetics.LobbyPetManager;

public class LobbyPetEvents implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void onPlayerSpawn(PlayerSpawnEvent event) {
        if (!(event.getPlayer() instanceof HypixelPlayer player)) return;
        if (!event.isFirstSpawn()) return;

        LobbyPetManager.trySpawnOnJoin(player, HypixelConst.getInstanceContainer());
    }

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        if (!(event.getPlayer() instanceof HypixelPlayer player)) return;

        LobbyPetManager.despawnPet(player);
    }
}
