package net.swofty.types.generic.event.actions.player.offhand;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionOffhandItemClick extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerSwapItemEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerSwapItemEvent event = (PlayerSwapItemEvent) tempEvent;

        event.getPlayer().sendMessage("§cYou cannot use your offhand!");
        event.setCancelled(true);
    }
}
