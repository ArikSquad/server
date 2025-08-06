package net.swofty.types.generic.command.commands;

import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@CommandParameters(aliases = "iteminfo",
        description = "Returns the players held item info",
        usage = "/nbt",
        permission = Rank.HELPER,
        allowsConsole = false)
public class NBTCommand extends SkyBlockCommand {
    private static Map<UUID, String> itemNBTCache = new HashMap<>();

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ItemStack item = ((SkyBlockPlayer) sender).getItemInMainHand();
            AtomicReference<String> values = new AtomicReference<>("");

            for (ItemAttribute possibleAttribute : ItemAttribute.getPossibleAttributes()) {
                String key = possibleAttribute.getKey();
                String value = item.getTag(Tag.String(key));
                if (value != null) {
                    values.set(key + ": " + value + "\n");
                    sender.sendMessage(key + ": " + value);
                }
            }

            if (itemNBTCache.containsKey(((SkyBlockPlayer) sender).getUuid())) {
                if (itemNBTCache.get(((SkyBlockPlayer) sender).getUuid()).equals(values.get())) {
                    sender.sendMessage("Item NBT has not changed since last check");
                } else {
                    sender.sendMessage("Item NBT has changed since last check");
                    sender.sendMessage("This is what has changed:");

                    String[] oldValues = itemNBTCache.get(((SkyBlockPlayer) sender).getUuid()).split("\n");
                    String[] newValues = values.get().split("\n");

                    for (int i = 0; i < oldValues.length; i++) {
                        if (!oldValues[i].equals(newValues[i])) {
                            sender.sendMessage("Old: " + oldValues[i]);
                            sender.sendMessage("New: " + newValues[i]);
                        }
                    }

                    itemNBTCache.put(((SkyBlockPlayer) sender).getUuid(), values.get());
                }
            } else {
                itemNBTCache.put(((SkyBlockPlayer) sender).getUuid(), values.get());
            }
        });
    }
}
