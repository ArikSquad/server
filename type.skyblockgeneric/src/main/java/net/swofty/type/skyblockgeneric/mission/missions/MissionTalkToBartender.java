package net.swofty.type.skyblockgeneric.mission.missions;

import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.*;

public class MissionTalkToBartender extends HypixelMission {

    @Override
    public String getID() {
        return "talk_to_bartender";
    }

    @Override
    public String getName() {
        return "Talk to the Bartender";
    }

    @Override
    public HashMap<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        mission.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        //TODO move bartender to the bar
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§6100 §7Coins"))).forEach(player::sendMessage);
        player.addCoins(100);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Set.of(RegionType.GRAVEYARD);
    }

}
