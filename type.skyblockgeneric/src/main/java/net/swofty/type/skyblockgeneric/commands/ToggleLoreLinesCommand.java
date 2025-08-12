package net.swofty.type.skyblockgeneric.commands;

import net.swofty.commons.Configuration;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.updater.PlayerItemOrigin;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "togglelines",
        description = "Toggles the lore lines of a sandbox item",
        usage = "/togglelines",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class ToggleLoreLinesCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (Configuration.get("sandbox-mode").equals("false")) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }

            HypixelPlayer player = (HypixelPlayer) sender;
            SkyBlockItem itemInHand = new SkyBlockItem(player.getItemInMainHand());
            ItemType type = itemInHand.getAttributeHandler().getPotentialType();

            if (type != ItemType.SANDBOX_ITEM) {
                player.sendMessage("§cYou can only toggle the lore lines of sandbox items.");
                return;
            }

            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                item.getAttributeHandler().getSandboxData().setShowLoreLinesToggle(!item.getAttributeHandler().getSandboxData().isShowLoreLinesToggle());
            });

            player.sendMessage("§aToggled the lore lines of the item in your hand.");
        });
    }
}
