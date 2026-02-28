package net.swofty.type.lobby.cosmetics;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.cosmetic.PlayerCosmeticData;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointCosmeticData;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MorphManager {
    private static final Map<UUID, MorphSession> activeMorphs = new ConcurrentHashMap<>();

    public static void activateMorph(HypixelPlayer player, LobbyMorphData morph) {
        deactivateMorph(player);

        PlayerCosmeticData data = player.getDataHandler()
            .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();
        data.activateMorph(morph.name());
        player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);

        MorphSession session = new MorphSession(morph, System.currentTimeMillis() + 300_000);
        activeMorphs.put(player.getUuid(), session);

        player.sendMessage(Component.text("§aYou morphed into " + morph.getRarity().getColorCode() + morph.getDisplayName() + "§a! Lasts 5 minutes."));

        startActionBarTask(player);
    }

    public static void deactivateMorph(HypixelPlayer player) {
        MorphSession removed = activeMorphs.remove(player.getUuid());
        if (removed != null) {
            PlayerCosmeticData data = player.getDataHandler()
                .get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).getValue();
            data.deactivateMorph();
            player.getDataHandler().get(HypixelDataHandler.Data.COSMETIC_DATA, DatapointCosmeticData.class).setValue(data);
            player.sendActionBar(Component.empty());
            player.sendMessage(Component.text("§cMorph deactivated!"));
        }
    }

    public static boolean hasMorph(HypixelPlayer player) {
        MorphSession session = activeMorphs.get(player.getUuid());
        if (session == null) return false;
        if (System.currentTimeMillis() >= session.expiryTime) {
            deactivateMorph(player);
            return false;
        }
        return true;
    }

    public static MorphSession getMorphSession(UUID uuid) {
        return activeMorphs.get(uuid);
    }

    public static void useAbility(HypixelPlayer player) {
        MorphSession session = activeMorphs.get(player.getUuid());
        if (session == null) return;

        LobbyMorphData.MorphAbility ability = session.morph.getAbility();
        if (ability == LobbyMorphData.MorphAbility.NONE) return;

        long now = System.currentTimeMillis();
        if (now - session.lastAbilityUse < 3000) {
            player.sendMessage(Component.text("§cAbility on cooldown!"));
            return;
        }
        session.lastAbilityUse = now;

        Pos pos = player.getPosition();
        switch (ability) {
            case TELEPORT -> {
                double offsetX = (Math.random() - 0.5) * 16;
                double offsetZ = (Math.random() - 0.5) * 16;
                player.teleport(pos.add(offsetX, 0, offsetZ));
                player.sendMessage(Component.text("§5Whoosh!"));
            }
            case EXPLOSION -> {
                player.getInstance().getPlayers().forEach(p -> {
                    if (p.equals(player)) return;
                    if (p.getPosition().distance(pos) < 6) {
                        Vec dir = p.getPosition().sub(pos).asVec().normalize().mul(20);
                        p.setVelocity(dir.add(0, 10, 0));
                    }
                });
                player.sendMessage(Component.text("§2Boom!"));
            }
            case FIREBALL -> {
                Vec direction = player.getPosition().direction().mul(20);
                player.setVelocity(direction);
                player.sendMessage(Component.text("§6Fireball!"));
            }
            case FLING_PLAYERS -> {
                player.getInstance().getPlayers().forEach(p -> {
                    if (p.equals(player)) return;
                    if (p.getPosition().distance(pos) < 5) {
                        p.setVelocity(new Vec(0, 25, 0));
                    }
                });
                player.sendMessage(Component.text("§eSmash!"));
            }
            case LAUNCH_FORWARD -> {
                Vec direction2 = player.getPosition().direction().mul(25);
                player.setVelocity(direction2);
                player.sendMessage(Component.text("§aLaunch!"));
            }
            case FLING_UPWARD -> {
                player.setVelocity(new Vec(0, 30, 0));
                player.sendMessage(Component.text("§bBoing!"));
            }
            case SHOOT_ARROW -> {
                player.sendMessage(Component.text("§7Arrow shot!"));
            }
            case SHOOT_LASER -> {
                player.sendMessage(Component.text("§bLaser!"));
            }
            case SHOOT_WEB -> {
                player.sendMessage(Component.text("§fWeb shot!"));
            }
            case THROW_POTION -> {
                player.sendMessage(Component.text("§5Splash!"));
            }
            case CONVERT_ZOMBIES -> {
                long nearbyCount = player.getInstance().getPlayers().stream()
                    .filter(p -> !p.equals(player) && p.getPosition().distance(pos) < 5)
                    .count();
                if (nearbyCount > 0) {
                    player.sendMessage(Component.text("§2Infected " + nearbyCount + " players!"));
                } else {
                    player.sendMessage(Component.text("§cNo players nearby to infect!"));
                }
            }
            case LAY_EGG -> {
                player.sendMessage(Component.text("§eBawk! Egg laid!"));
            }
            case POOP -> {
                player.sendMessage(Component.text("§6...gross!"));
            }
            case CYCLE_COLORS -> {
                player.sendMessage(Component.text("§dColors cycling!"));
            }
            default -> {
            }
        }
    }

    public static void cleanup(UUID uuid) {
        activeMorphs.remove(uuid);
    }

    private static void startActionBarTask(HypixelPlayer player) {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            MorphSession session = activeMorphs.get(player.getUuid());
            if (session == null || !player.isOnline()) return;

            long remaining = session.expiryTime - System.currentTimeMillis();
            if (remaining <= 0) {
                deactivateMorph(player);
                return;
            }

            int seconds = (int) (remaining / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            String abilityText = session.morph.getAbility() != LobbyMorphData.MorphAbility.NONE
                ? " §e§lRIGHT CLICK §7to use §a" + session.morph.getAbility().getDisplayName()
                : "";

            player.sendActionBar(Component.text(
                "§b" + session.morph.getDisplayName() + " Morph §7- §a" +
                    String.format("%d:%02d", minutes, seconds) + abilityText
            ));
        }).repeat(TaskSchedule.tick(20)).schedule();
    }

    public static class MorphSession {
        public final LobbyMorphData morph;
        public final long expiryTime;
        public long lastAbilityUse = 0;

        public MorphSession(LobbyMorphData morph, long expiryTime) {
            this.morph = morph;
            this.expiryTime = expiryTime;
        }
    }
}
