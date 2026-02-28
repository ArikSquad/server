package net.swofty.type.lobby.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointPetData;
import net.swofty.type.generic.pet.PlayerPetData;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.lobby.cosmetics.LobbyPetEntity;
import net.swofty.type.lobby.cosmetics.LobbyPetManager;

@CommandParameters(aliases = "pet", allowsConsole = false, description = "Pet commands", permission = Rank.VIP, usage = "/pet <name|teleport|teleportto>")
public class PetCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.setDefaultExecutor((sender, context) -> {
            sender.sendMessage("§a/pet name <name> §7- Rename your pet");
            sender.sendMessage("§a/pet teleport §7- Teleport pet to you");
            sender.sendMessage("§a/pet teleportto §7- Teleport to your pet");
        });

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof HypixelPlayer player)) return;

            String[] nameArgs = context.get("name");
            if (nameArgs == null || nameArgs.length == 0) {
                player.sendMessage(Component.text("§cUsage: /pet name <name>"));
                return;
            }

            String newName = String.join(" ", nameArgs);
            if (newName.length() > 24) {
                player.sendMessage(Component.text("§cName must be 24 characters or fewer!"));
                return;
            }

            PlayerPetData data = player.getDataHandler()
                .get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class).getValue();
            if (data.getActivePetId() == null) {
                player.sendMessage(Component.text("§cYou don't have a pet summoned!"));
                return;
            }

            PlayerPetData.PetInstance inst = data.getActivePetInstance();
            if (inst == null) return;

            inst.setCustomName(newName);
            player.getDataHandler().get(HypixelDataHandler.Data.PET_DATA, DatapointPetData.class)
                .setValue(data);

            LobbyPetEntity entity = LobbyPetManager.getActivePet(player.getUuid());
            if (entity != null) {
                entity.set(net.minestom.server.component.DataComponents.CUSTOM_NAME,
                    Component.text(entity.getPetData().getRarity().getColorCode() + newName));
            }

            player.sendMessage(Component.text("§aRenamed your pet to §e" + newName + "§a!"));
        }, new ArgumentLiteral("name"), new ArgumentStringArray("name"));

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof HypixelPlayer player)) return;

            LobbyPetEntity entity = LobbyPetManager.getActivePet(player.getUuid());
            if (entity == null) {
                player.sendMessage(Component.text("§cYou don't have a pet summoned!"));
                return;
            }

            entity.teleport(player.getPosition());
            player.sendMessage(Component.text("§aTeleported your pet to you!"));
        }, new ArgumentLiteral("teleport"));

        command.addSyntax((sender, context) -> {
            if (!(sender instanceof HypixelPlayer player)) return;

            LobbyPetEntity entity = LobbyPetManager.getActivePet(player.getUuid());
            if (entity == null) {
                player.sendMessage(Component.text("§cYou don't have a pet summoned!"));
                return;
            }

            Pos petPos = entity.getPosition();
            player.teleport(petPos);
            player.sendMessage(Component.text("§aTeleported to your pet!"));
        }, new ArgumentLiteral("teleportto"));
    }
}
