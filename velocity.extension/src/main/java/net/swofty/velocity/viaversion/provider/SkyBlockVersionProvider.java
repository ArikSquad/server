package net.swofty.velocity.viaversion.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.IntStream;

import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.viaversion.injector.SkyBlockViaInjector;
import org.jetbrains.annotations.Nullable;

public class SkyBlockVersionProvider implements VersionProvider {
    private static final Method GET_ASSOCIATION = getAssociationMethod();

    private static @Nullable Method getAssociationMethod() {
        try {
            return Class.forName("com.velocitypowered.proxy.connection.MinecraftConnection").getMethod("getAssociation");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            System.out.println("Failed to get association method from Velocity");
            return null;
        }
    }

    @Override
    public ProtocolVersion getClosestServerProtocol(UserConnection user) throws Exception {
        return user.isClientSide() ? getBackProtocol(user) : getFrontProtocol(user);
    }

    private ProtocolVersion getBackProtocol(UserConnection user) throws Exception {
        return ProtocolVersion.v1_21_4; // backend server version
    }

    private ProtocolVersion getFrontProtocol(UserConnection user) throws Exception {
        ProtocolVersion playerVersion = user.getProtocolInfo().protocolVersion();

        IntStream versions = com.velocitypowered.api.network.ProtocolVersion.SUPPORTED_VERSIONS.stream()
                .mapToInt(com.velocitypowered.api.network.ProtocolVersion::getProtocol);

        // Modern forwarding mode needs 1.13 Login plugin message
        if (SkyBlockViaInjector.GET_PLAYER_INFO_FORWARDING_MODE != null
                && ((Enum<?>) SkyBlockViaInjector.GET_PLAYER_INFO_FORWARDING_MODE.invoke(SkyBlockVelocity.getServer().getConfiguration()))
                .name().equals("MODERN")) {
            versions = versions.filter(ver -> ver >= ProtocolVersion.v1_13.getVersion());
        }
        int[] compatibleProtocols = versions.toArray();

        if (Arrays.binarySearch(compatibleProtocols, playerVersion.getVersion()) >= 0) {
            return playerVersion;
        }

        if (playerVersion.getVersion() < compatibleProtocols[0]) {
            return ProtocolVersion.getProtocol(compatibleProtocols[0]);
        }

        for (int i = compatibleProtocols.length - 1; i >= 0; i--) {
            int protocol = compatibleProtocols[i];
            if (playerVersion.getVersion() > protocol && ProtocolVersion.isRegistered(protocol)) {
                return ProtocolVersion.getProtocol(protocol);
            }
        }

        System.out.println("Panic, no protocol id found for " + playerVersion);
        return playerVersion;
    }
}
