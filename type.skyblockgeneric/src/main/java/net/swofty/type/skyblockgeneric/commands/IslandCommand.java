package net.swofty.type.skyblockgeneric.commands;

import net.swofty.commons.ServerType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "is",
        description = "Sends the player to their island",
        usage = "/is",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class IslandCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = ((SkyBlockPlayer) sender);
            player.sendTo(ServerType.SKYBLOCK_ISLAND);
        });
    }
}
