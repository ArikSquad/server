package net.swofty.type.skyblockgeneric.museum;

import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public abstract class MuseumReward {
    public abstract void onUnlock(MuseumRewards rewards, SkyBlockPlayer player);
    public abstract String loreDisplay();
}
