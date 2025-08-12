package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.enchantment.EnchantmentType;
import net.swofty.type.generic.enchantment.SkyBlockEnchantment;
import net.swofty.type.generic.item.ItemAttributeHandler;
import net.swofty.type.generic.item.updater.PlayerItemOrigin;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "ench",
        description = "Enchants the contents o the players hand",
        usage = "/enchant",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class EnchantCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<EnchantmentType> typeArgument =
                ArgumentType.Enum("enchantment_type", EnchantmentType.class);
        ArgumentInteger level = ArgumentType.Integer("level");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ((HypixelPlayer) sender).updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
                itemAttributeHandler.addEnchantment(
                        SkyBlockEnchantment.builder()
                                .level(context.get(level))
                                .type(context.get(typeArgument))
                                .build()
                );
                sender.sendMessage("§aYour item has been enchanted");
            });
        }, typeArgument, level);
    }
}
