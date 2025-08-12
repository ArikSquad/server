package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.collection.CollectionCategories;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "updatecollection",
        description = "Updates the collection of a player",
        usage = "/setcollection <item_type> <amount>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class SetCollectionCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemType> itemType = new ArgumentEnum("item_type", ItemType.class);
        ArgumentInteger amountArgument = new ArgumentInteger("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            ItemType type = context.get(itemType);
            int amount = context.get(amountArgument);

            player.getCollection().set(type, amount);
            player.sendMessage("§aUpdated your §e" + CollectionCategories.getCategory(type).getName()
                    + " §acategory for the §c" + type.getDisplayName()
                    + " §acollection to §e" + amount + "§a.");
        }, itemType, amountArgument);
    }
}
