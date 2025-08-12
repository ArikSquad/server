package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.item.ConfigurableSkyBlockItem;
import net.swofty.type.generic.user.categories.Rank;
import org.tinylog.Logger;

@CommandParameters(aliases = "displayconfigids",
        description = "Prints out the item IDs from the config into the console",
        usage = "/displayconfigids",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class PrintConfigIdsCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.sendMessage("§aAvailable Item IDs:");
            for (String id : ConfigurableSkyBlockItem.getIDs()) {
                sender.sendMessage("§7" + id);
                Logger.info("§7" + id);
            }
        });
    }
}
