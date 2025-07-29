package net.swofty.types.generic.event.actions.player.region;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.CustomDropComponent;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockMiningConfiguration;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.region.mining.MineableBlock;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;

import java.util.List;

public class ActionRegionBlockBreak implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerBlockBreakEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        // Skip if player has build bypass
        if (player.isBypassBuild()) return;

        event.setCancelled(true);
        SkyBlockRegion region = SkyBlockRegion.getRegionOfPosition(event.getBlockPosition());

        Block block = event.getBlock();
        Material material = Material.fromKey(block.name());
        boolean shouldItemDrop = false;

        // Handle island server block breaks
        if (SkyBlockConst.isIslandServer()) {
            event.getInstance().setBlock(event.getBlockPosition(), Block.AIR);
            shouldItemDrop = true;
        }

        // Handle region-specific block breaks
        else if (region != null) {
            RegionType type = region.getType();
            SkyBlockMiningConfiguration mining = type.getMiningHandler();

            // Validate if block can be mined in this region
            if (material != null && mining != null && !mining.getMineableBlocks(player.getInstance(), event.getBlockPosition()).contains(material)) {
                event.setCancelled(true);
                return;
            }

            // Queue block for mining if in valid region
            if (mining != null && material != null) {
                mining.addToQueue(player, Pos.fromPoint(event.getBlockPosition()), (SharedInstance) player.getInstance());
                shouldItemDrop = true;
            }
        }
        // Cancel if not in a region at all
        else {
            event.setCancelled(true);
            return;
        }

        // Handle item drops for valid breaks
        if (material != null && shouldItemDrop) {
            SkyBlockItem brokenBlockItem = new SkyBlockItem(material);

            // Determine which item(s) should be dropped using CustomDropComponent
            List<SkyBlockItem> customDrops = CustomDropComponent.simulateDrop(
                    brokenBlockItem,
                    player,
                    new SkyBlockItem(player.getItemInMainHand()),
                    region,
                    SkyBlockConst.isIslandServer()
            );

            // Call custom break event
            boolean playerPlaced = block.hasTag(Tag.Boolean("player_placed")) && block.getTag(Tag.Boolean("player_placed"));

            SkyBlockEventHandler.callSkyBlockEvent(new CustomBlockBreakEvent(
                    player, material, event.getBlockPosition(), customDrops, playerPlaced
            ));

            // Process each drop with fortune multiplier
            for (SkyBlockItem dropItem : customDrops) {
                // Calculate drop amount with fortune multiplier
                int dropAmount = dropItem.getAmount(); // Base amount from CustomDropComponent

                try {
                    MineableBlock mineableBlock = MineableBlock.get(block);
                    if (mineableBlock != null) {
                        double baseFortune = player.getStatistics().allStatistics().getOverall(mineableBlock.getBlockType().baseSkillFortune());
                        double specificFortune = player.getStatistics().allStatistics().getOverall(mineableBlock.getBlockType().specificBlockFortune());
                        double fortune = baseFortune + specificFortune;
                        double dropMultiplier = (1 + (fortune * 0.01));
                        dropAmount = (int) Math.ceil(dropAmount * dropMultiplier);
                    }
                } catch (NullPointerException e) {
                    // Keep base amount if fortune calculation fails
                }

                // Update the drop item amount
                dropItem.setAmount(dropAmount);
                ItemType droppedItemType = dropItem.getAttributeHandler().getPotentialType();

                // Handle item distribution based on player conditions
                if (player.canInsertItemIntoSacks(droppedItemType, dropAmount)) {
                    player.getSackItems().increase(droppedItemType, dropAmount);
                } else if (player.getSkyBlockExperience().getLevel().asInt() >= 6) {
                    player.addAndUpdateItem(dropItem);
                } else {
                    // Determine nearest air block between ore and player
                    Pos orePos = Pos.fromPoint(event.getBlockPosition());
                    Pos playerPos = player.getPosition();

                    Pos[] offsets = {
                            new Pos(1, 0, 0), new Pos(-1, 0, 0),
                            new Pos(0, 1, 0), new Pos(0, -1, 0),
                            new Pos(0, 0, 1), new Pos(0, 0, -1)
                    };

                    Pos nearestAirBlock = null;
                    double closestDistanceSquared = Double.MAX_VALUE;

                    for (Pos offset : offsets) {
                        Pos adjacentPos = orePos.add(offset);
                        Block block2 = player.getInstance().getBlock(adjacentPos);

                        if (block2.isAir()) {
                            double distanceSquared = adjacentPos.distanceSquared(playerPos);
                            if (distanceSquared < closestDistanceSquared) {
                                closestDistanceSquared = distanceSquared;
                                nearestAirBlock = adjacentPos;
                            }
                        }
                    }

                    // Use the nearest air block or fallback to default position
                    Pos dropPos = (nearestAirBlock != null) ? nearestAirBlock.add(0.5, 0.5, 0.5) : orePos.add(0.5, 1.5, 0.5);

                    // Spawn the item
                    DroppedItemEntityImpl droppedItem = new DroppedItemEntityImpl(dropItem, player);
                    droppedItem.setInstance(player.getInstance(), dropPos);
                    droppedItem.addViewer(player);
                }
            }
        }
    }
}

