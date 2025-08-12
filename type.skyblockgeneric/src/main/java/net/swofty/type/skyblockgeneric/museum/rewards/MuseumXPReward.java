package net.swofty.type.skyblockgeneric.museum.rewards;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;
import net.swofty.type.skyblockgeneric.museum.MuseumReward;
import net.swofty.type.skyblockgeneric.museum.MuseumRewards;
import SkyBlockPlayer;

@Getter
public class MuseumXPReward extends MuseumReward {
    private final int xp;

    public MuseumXPReward(int xp) {
        this.xp = xp;
    }
    @Override
    public void onUnlock(MuseumRewards rewards, SkyBlockPlayer player) {
        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getMuseumCause(rewards));
    }

    @Override
    public String loreDisplay() {
        return "§b" + xp + " SkyBlock XP";
    }
}
