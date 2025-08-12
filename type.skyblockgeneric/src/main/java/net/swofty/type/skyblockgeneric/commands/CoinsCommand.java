package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Sets your purse balance",
        usage = "/coins <amount>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class CoinsCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentNumber<Double> doubleArgument = ArgumentType.Double("amount").min(0D);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(context.get(doubleArgument));

            sender.sendMessage("§aSuccessfully set coins to to §e" + context.getRaw(doubleArgument) + "§a.");
        }, doubleArgument);
    }
}
