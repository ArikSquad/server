package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;

@CommandParameters(aliases = "setrank", description = "Rank command", usage = "/rank <player> <rank>", permission = Rank.ADMIN, allowsConsole = true)
public class RankCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEntity entityArgument = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        ArgumentEnum<Rank> rankArgument = ArgumentType.Enum("rank", Rank.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final Player player = context.get(entityArgument).findFirstPlayer(sender);
            final Rank rank = context.get(rankArgument);

            if (player == null) {
                sender.sendMessage("§cCouldn't find a player by the name of §e" + context.getRaw(entityArgument) + "§c.");
                return;
            }

            final String playerName = player.getUsername();

            SkyBlockDataHandler.getUser(player.getUuid()).get(net.swofty.type.generic.data.HypixelDataHandler.Data.RANK, DatapointRank.class).setValue(rank);

            sender.sendMessage("§aSuccessfully set §e" + playerName + "§a's rank to §e" + rank.name() + "§a.");
        }, entityArgument, rankArgument);
    }
}
