package net.swofty.type.skyblockgeneric.entity;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.collision.*;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.projectile.ArrowMeta;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ArrowEntityImpl extends LivingEntity {
    private long cooldown = 0;
    @Getter
    private final Entity shooter;
    @Getter
    private final SkyBlockItem arrowItem;

    public ArrowEntityImpl(Entity player, SkyBlockItem arrowItem) {
        super(EntityType.ARROW);

        this.collidesWithEntities = false;
        this.hasPhysics = false;

        this.shooter = player;
        this.arrowItem = arrowItem;

        ArrowMeta arrowMeta = (ArrowMeta) getEntityMeta();
        arrowMeta.setShooter(player);
        arrowMeta.setPiercingLevel((byte) 5);
    }

    @Override
    public void spawn() {
        super.spawn();
        cooldown = System.currentTimeMillis();

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (!isRemoved()) {
                kill();
                remove();
            }
        }, TaskSchedule.tick(30), TaskSchedule.stop());
    }

    @Override
    public void tick(long time) {
        final Pos posBefore = getPosition();
        super.tick(time);
        final Pos posNow = getPosition();

        Vec diff = Vec.fromPoint(posNow.sub(posBefore));
        PhysicsResult result = CollisionUtils.handlePhysics(
                instance, this.getChunk(),
                this.getBoundingBox(),
                posBefore, diff,
                null, true
        );

        if (cooldown + 200 < System.currentTimeMillis()) {
            float yaw = (float) Math.toDegrees(Math.atan2(diff.x(), diff.z()));
            float pitch = (float) Math.toDegrees(Math.atan2(diff.y(), Math.sqrt(diff.x() * diff.x() + diff.z() * diff.z())));
            super.refreshPosition(new Pos(posNow.x(), posNow.y(), posNow.z(), yaw, pitch));
            cooldown = System.currentTimeMillis();
        }

        @NotNull Collection<EntityCollisionResult> collided = CollisionUtils.checkEntityCollisions(instance, this.getBoundingBox(), posBefore, diff, 3, (e) -> e != this, result);
        for (EntityCollisionResult collisionResult : collided) {
            if (collisionResult != null && collisionResult.entity() != shooter) {
                Entity entity = collisionResult.entity();
                EntityType entityType = entity.getEntityType();
                if (entityType == EntityType.PLAYER ||
                        entityType == EntityType.ARMOR_STAND) {
                    return;
                }

                var e = new ProjectileCollideWithEntityEvent(
                        this,
                        Pos.fromPoint(collisionResult.collisionPoint()),
                        entity
                );
                MinecraftServer.getGlobalEventHandler().call(e);
                if (!e.isCancelled()) {
                    remove();
                    kill();
                }
                return;
            }
        }

        if (result.hasCollision()) {
            Block hitBlock = null;
            for (Shape shape : result.collisionShapes()) {
                if (shape instanceof ShapeImpl block) {
                    hitBlock = getInstance().getBlock(block.relativeEnd());
                }
            }

            if (hitBlock == null) return;

            // We've hit a block
            MathUtility.delay(() -> {
                if (isDead() || isRemoved()) return;
                remove();
                kill();
            }, 20);
        }
    }
}
