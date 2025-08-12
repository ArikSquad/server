package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.MobRegistry;
import SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "updatebestiary",
        description = "Updates the bestiary of a player",
        usage = "/setbestiary <mob> <amount>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class SetBestiaryCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString mobArgument = new ArgumentString("mob");
        ArgumentInteger amountArgument = new ArgumentInteger("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String mobId = context.get(mobArgument);
            BestiaryMob mob = MobRegistry.getMobById(mobId);

            if (mob == null) {
                player.sendMessage("§cUnknown mob: " + mobId);
                return;
            }

            int amount = context.get(amountArgument);
            player.getBestiaryData().set(mob, amount);

            player.sendMessage("§aSet bestiary kills for §e" + mob.getDisplayName() + "§a to §e" + amount + "§a.");
        }, mobArgument, amountArgument);
    }
}

