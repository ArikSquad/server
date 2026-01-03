package net.swofty.type.buildbattlegame.events;

import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.swofty.type.buildbattlegame.TypeBuildBattleGameLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionGameCustomItems implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerUseItemOnBlockEvent event) {
        TypeBuildBattleGameLoader.getItemHandler().onItemUseOnBlock(event);
    }

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerFinishItemUseEvent event) {
        TypeBuildBattleGameLoader.getItemHandler().onItemFinishUse(event);
    }

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerUseItemEvent event) {
        TypeBuildBattleGameLoader.getItemHandler().onItemUse(event);
    }

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerBlockPlaceEvent event) {
        TypeBuildBattleGameLoader.getItemHandler().onBlockPlace(event);
    }
}
