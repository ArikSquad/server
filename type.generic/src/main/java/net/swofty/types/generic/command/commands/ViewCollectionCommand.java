package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection.GUICollectionItem;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Opens up a collections GUI",
        usage = "/viewcollection <collection>",
        permission = Rank.DEFAULT,
        aliases = "vc",
        allowsConsole = false)
public class ViewCollectionCommand extends SkyBlockCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemTypeLinker> itemArgument = ArgumentType.Enum("item", ItemTypeLinker.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final ItemTypeLinker itemTypeLinker = context.get(itemArgument);

            if (CollectionCategories.getCategory(itemTypeLinker) == null) {
                sender.sendMessage("§cThis item does not have a collection!");
            }

            new GUICollectionItem(itemTypeLinker).open((SkyBlockPlayer) sender);
        }, itemArgument);
    }
}
