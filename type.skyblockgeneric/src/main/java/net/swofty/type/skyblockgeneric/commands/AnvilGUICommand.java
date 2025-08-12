package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.gui.HypixelAnvilGUI;
import SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "anvilgraphicaluserinterface",
        description = "Opens a graphical user interface",
        usage = "/anvilgui <text>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class AnvilGUICommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString text = ArgumentType.String("text");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String anvilText = context.get(text);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            new HypixelAnvilGUI(player).open(anvilText).thenAccept(line -> {
                if (line == null) {
                    return;
                }

                player.sendMessage("§7You wrote: §a" + line);
            });
        }, text);
    }
}
