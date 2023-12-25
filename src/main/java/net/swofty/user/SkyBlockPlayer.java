package net.swofty.user;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.SkyBlock;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointMissionData;
import net.swofty.event.value.SkyBlockValueEvent;
import net.swofty.event.value.ValueUpdateEvent;
import net.swofty.event.value.events.MiningValueUpdateEvent;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.updater.PlayerItemOrigin;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.mission.MissionData;
import net.swofty.region.mining.MineableBlock;
import net.swofty.region.SkyBlockRegion;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.PlayerStatistics;
import net.swofty.user.statistics.StatisticDisplayReplacement;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SkyBlockPlayer extends Player {

    @Getter
    private float mana = 100;
    public float health = 100;
    public long joined = 0;
    @Setter
    @Getter
    public boolean bypassBuild = false;

    @Getter
    private StatisticDisplayReplacement manaDisplayReplacement = null;
    @Getter
    private StatisticDisplayReplacement defenseDisplayReplacement = null;
    @Getter
    private PlayerAbilityHandler abilityHandler = new PlayerAbilityHandler();
    @Getter
    private SkyBlockIsland skyBlockIsland = null;

    public SkyBlockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);

        if (SkyBlock.offlineUUIDs.contains(uuid)) {
            this.setUsernameField("cracked" + username);
            SkyBlock.offlineUUIDs.remove(uuid);
        }

        skyBlockIsland = new SkyBlockIsland(this);
        joined = System.currentTimeMillis();
    }

    public DataHandler getDataHandler() {
        return DataHandler.getUser(this.uuid);
    }

    public PlayerStatistics getStatistics() {
        return new PlayerStatistics(this);
    }

    public AntiCheatHandler getAntiCheatHandler() {
        return new AntiCheatHandler(this);
    }

    public LogHandler getLogHandler() {
        return new LogHandler(this);
    }

    public MissionData getMissionData() {
        return getDataHandler().get(DataHandler.Data.MISSION_DATA, DatapointMissionData.class).getValue();
    }

    public boolean isOnIsland() {
        return skyBlockIsland.getCreated();
    }

    public void setDisplayReplacement(StatisticDisplayReplacement replacement, StatisticDisplayReplacement.DisplayType type) {
        // Determine which replacement to update based on type
        StatisticDisplayReplacement currentReplacement =
                (type == StatisticDisplayReplacement.DisplayType.MANA) ? this.manaDisplayReplacement :
                        this.defenseDisplayReplacement;

        // Check if the replacement needs to be updated
        if (currentReplacement == null || currentReplacement.getTicksToLast() > replacement.getTicksToLast()) {
            // Update the appropriate replacement based on type
            if (type == StatisticDisplayReplacement.DisplayType.MANA) {
                this.manaDisplayReplacement = replacement;
            } else if (type == StatisticDisplayReplacement.DisplayType.DEFENSE) {
                this.defenseDisplayReplacement = replacement;
            }

            int hashCode = replacement.hashCode();

            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                StatisticDisplayReplacement scheduledReplacement =
                        (type == StatisticDisplayReplacement.DisplayType.MANA) ? this.manaDisplayReplacement :
                                this.defenseDisplayReplacement;
                if (hashCode == scheduledReplacement.hashCode()) {
                    if (type == StatisticDisplayReplacement.DisplayType.MANA) {
                        this.manaDisplayReplacement = null;
                    } else if (type == StatisticDisplayReplacement.DisplayType.DEFENSE) {
                        this.defenseDisplayReplacement = null;
                    }
                }
            }, TaskSchedule.tick(replacement.getTicksToLast()), TaskSchedule.stop());
        }
    }

    public SkyBlockRegion getRegion() {
        if (isOnIsland()) {
            return SkyBlockRegion.getIslandRegion();
        }

        return SkyBlockRegion.getRegionOfPosition(this.getPosition());
    }

    public void setMana(float mana) {
        this.mana = mana;
    }

    public void addAndUpdateItem(SkyBlockItem item) {
        ItemStack toAdd = PlayerItemUpdater.playerUpdate(this, PlayerItemOrigin.INVENTORY_SLOT, item.getItemStack()).build();
        this.getInventory().addItemStack(toAdd);
    }

    public float getMaxMana() {
        float maxMana = 100;

        PlayerStatistics statistics = this.getStatistics();
        maxMana += statistics.allArmorStatistics().get(ItemStatistic.INTELLIGENCE);
        maxMana += statistics.mainHandStatistics().get(ItemStatistic.INTELLIGENCE);

        return maxMana;
    }

    public int getMiningSpeed() {
        return this.getStatistics().mainHandStatistics().get(ItemStatistic.MINING_SPEED) +
                this.getStatistics().allArmorStatistics().get(ItemStatistic.MINING_SPEED);
    }

    public void sendToHub() {
        if (getInstance() == SkyBlock.getInstanceContainer())
            this.teleport(new Pos(-2.5, 70, -69.5, 180, 0));
        
        this.setInstance(SkyBlock.getInstanceContainer(), new Pos(-2.5, 70, -69.5, 180, 0));
    }

    public CompletableFuture<Boolean> sendToIsland() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        skyBlockIsland.getSharedInstance().thenAccept(sharedInstance -> {
            this.setInstance(sharedInstance, new Pos(0.5, 100, 0.5, 0, 0));
            this.teleport(new Pos(0.5, 100, 0.5, 0, 0));
        });

        return future;
    }

    public double getTimeToMine(SkyBlockItem item, Block b) {
        MineableBlock block = MineableBlock.get(b);
        if (block == null) return -1;
        if (!item.getAttributeHandler().isMiningTool()) return -1;
        if (getRegion() == null) return -1;

        if (block.getMiningPowerRequirement() > item.getAttributeHandler().getBreakingPower()) return -1;
        if (block.getStrength() > 0) {
            double time = (block.getStrength() * 30) / (Math.max(getMiningSpeed(), 1));
            ValueUpdateEvent event = new MiningValueUpdateEvent(
                    this,
                    time,
                    item);

            SkyBlockValueEvent.callValueUpdateEvent(event);
            time = (double) event.getValue();

            double softcap = ((double) 20 / 3) * block.getStrength();
            if (time < 1)
                return 1;

            return Math.min(time, softcap);
        }

        return 0;
    }

    public float getDefense() {
        float defence = 0;

        PlayerStatistics statistics = this.getStatistics();
        defence += statistics.allStatistics().get(ItemStatistic.DEFENSE);

        return defence;
    }

    public void setHearts(float hearts) {
        this.health = hearts;
        this.sendPacket(new UpdateHealthPacket((hearts / getMaxHealth()) * 20, 20, 20));
    }

    public void closeInventoryBypass() {
        super.closeInventory();
    }

    @Override
    public float getMaxHealth() {
        PlayerStatistics statistics = this.getStatistics();
        return statistics.allStatistics().get(ItemStatistic.HEALTH);
    }

    @Override
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(float health) {
        if ((System.currentTimeMillis() - joined) < 3000)
            return;
        this.health = health;
        this.sendPacket(new UpdateHealthPacket((health / getMaxHealth()) * 20, 20, 20));
    }

    @Override
    public void sendMessage(@NotNull String message) {
        super.sendMessage(message.replace("&", "§"));
    }

    @Override
    public void closeInventory() {
        Inventory tempInv = this.getOpenInventory();
        super.closeInventory();
        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(this.getUuid())) {
            SkyBlockInventoryGUI gui = SkyBlockInventoryGUI.GUI_MAP.get(this.getUuid());

            if (gui == null) return;

            gui.onClose(new InventoryCloseEvent(tempInv, this), SkyBlockInventoryGUI.CloseReason.SERVER_EXITED);
            SkyBlockInventoryGUI.GUI_MAP.remove(this.getUuid());
        }
    }
}
