package net.swofty.type.skyblockgeneric.commands;

import net.swofty.commons.Configuration;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "getsandboxitem",
        description = "Gets a sandbox item",
        usage = "/getsandboxitem",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class SandboxCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (Configuration.get("sandbox-mode").equals("false")) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.addAndUpdateItem(ItemType.SANDBOX_ITEM);
            player.sendMessage("§aAdded a sandbox item to your inventory.");
        });
    }
}
