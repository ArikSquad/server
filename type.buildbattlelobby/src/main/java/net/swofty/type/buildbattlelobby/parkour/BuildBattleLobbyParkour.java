package net.swofty.type.buildbattlelobby.parkour;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointParkourData;
import net.swofty.type.lobby.parkour.Parkour;

import java.util.List;

public class BuildBattleLobbyParkour implements Parkour {

	@Override
	public DatapointParkourData.ParkourType getId() {
		return DatapointParkourData.ParkourType.BUILD_BATTLE;
	}

	@Override
	public Pos getStartLocation() {
		return new Pos(-0.5, 69, 18.5, 45, 0);
	}

	@Override
	public List<Point> getCheckpoints() {
		return List.of(
				new BlockVec(-2, 69, 19),
				new BlockVec(15, 89, 46),
				new BlockVec(-12, 60, 39),
				new BlockVec(-12, 62, -24),
				new BlockVec(7, 69, -64)
		);
	}
}
