package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.data.monogdb.CrystalDatabase;
import net.swofty.type.skyblockgeneric.entity.ServerCrystalImpl;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.ServerOrbComponent;
import net.swofty.type.skyblockgeneric.item.components.SkullHeadComponent;
import SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "spawncrystal",
        description = "Spawns in a crystal at the player's location.",
        usage = "/addcrystal",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class AddCrystalCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemType> itemType = new ArgumentEnum<>("itemType", ItemType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ItemType type = context.get(itemType);
            SkyBlockItem item = new SkyBlockItem(type);

            // Spawn the orb
            ServerOrbComponent asOrb = item.getComponent(ServerOrbComponent.class);
            ServerCrystalImpl crystal = new ServerCrystalImpl(
                    asOrb.getSpawnMaterialFunction(),
                    item.getComponent(SkullHeadComponent.class).getSkullTexture(item),
                    asOrb.getValidBlocks()
            );
            crystal.setInstance(((SkyBlockPlayer) sender).getInstance(), ((SkyBlockPlayer) sender).getPosition());

            CrystalDatabase.addCrystal(item.getComponent(SkullHeadComponent.class).getSkullTexture(item),
                    ((SkyBlockPlayer) sender).getPosition(),
                    type);
        }, itemType);
    }
}
