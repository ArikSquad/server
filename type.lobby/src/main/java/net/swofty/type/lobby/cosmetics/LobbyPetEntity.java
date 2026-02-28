package net.swofty.type.lobby.cosmetics;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.AgeableMobMeta;
import net.minestom.server.instance.Instance;

import java.util.UUID;

@Getter
public class LobbyPetEntity extends EntityCreature {
    private static final double FOLLOW_DISTANCE = 3.0;
    private static final double TELEPORT_DISTANCE = 15.0;
    private static final double SPEED = 0.15;

    private final UUID ownerUuid;
    private final LobbyPetData petData;

    public LobbyPetEntity(LobbyPetData petData, UUID ownerUuid, String displayName) {
        super(petData.getEntityType());
        this.ownerUuid = ownerUuid;
        this.petData = petData;

        if (petData.isBaby() && this.entityMeta instanceof AgeableMobMeta ageable) {
            ageable.setBaby(true);
        }

        this.set(DataComponents.CUSTOM_NAME, Component.text(
            petData.getRarity().getColorCode() + displayName
        ));
        this.setCustomNameVisible(true);
        this.setNoGravity(false);
        this.hasPhysics = true;
    }

    public void spawnForOwner(Instance instance, Pos ownerPos) {
        Pos spawnPos = ownerPos.add(
            Math.random() * 2 - 1,
            0,
            Math.random() * 2 - 1
        );
        this.setInstance(instance, spawnPos);
    }

    @Override
    public void tick(long time) {
        Instance instance = getInstance();
        if (instance == null) return;

        Pos position = getPosition();
        if (!instance.isChunkLoaded(position)) {
            return;
        }

        try {
            super.tick(time);
        } catch (Exception ignored) {
        }

        Player owner = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(ownerUuid);
        if (owner == null || !owner.isOnline()) {
            remove();
            return;
        }

        Pos ownerPos = owner.getPosition();
        double distance = position.distance(ownerPos);

        if (distance > TELEPORT_DISTANCE) {
            teleport(ownerPos.add(Math.random() * 2 - 1, 0, Math.random() * 2 - 1));
            return;
        }

        if (distance > FOLLOW_DISTANCE) {
            Vec direction = ownerPos.asVec().sub(position.asVec()).normalize().mul(SPEED);
            double yaw = Math.toDegrees(Math.atan2(-direction.x(), direction.z()));
            double pitch = Math.toDegrees(-Math.atan2(direction.y(),
                Math.sqrt(direction.x() * direction.x() + direction.z() * direction.z())));

            Vec velocity = direction.mul(20);
            if (position.y() < ownerPos.y()) {
                velocity = velocity.withY(5);
            }
            setVelocity(velocity);
            teleport(getPosition().withYaw((float) yaw).withPitch((float) pitch));
        } else {
            float yaw = (float) Math.toDegrees(Math.atan2(
                -(ownerPos.x() - position.x()),
                ownerPos.z() - position.z()
            ));
            teleport(getPosition().withYaw(yaw));
        }
    }
}
