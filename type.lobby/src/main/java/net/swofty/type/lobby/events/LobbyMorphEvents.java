package net.swofty.type.lobby.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.cosmetics.MorphManager;

public class LobbyMorphEvents implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void onUseItem(PlayerUseItemEvent event) {
        if (!(event.getPlayer() instanceof HypixelPlayer player)) return;
        if (!MorphManager.hasMorph(player)) return;

        MorphManager.useAbility(player);
    }

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onDisconnect(PlayerDisconnectEvent event) {
        MorphManager.cleanup(event.getPlayer().getUuid());
    }
}
