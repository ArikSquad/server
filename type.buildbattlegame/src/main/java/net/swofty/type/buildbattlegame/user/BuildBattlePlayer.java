package net.swofty.type.buildbattlegame.user;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class BuildBattlePlayer extends HypixelPlayer {

    public BuildBattlePlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

}
