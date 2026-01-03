package net.swofty.commons.buildbattle;

import lombok.Getter;

@Getter
public enum BuildBattleGameType {
	SOLO_MODE(16),
	TEAMS_MODE(32),
	SPEED_BUILDERS(8),
	GUESS_THE_BUILD(10),
	PRO_MODE(16);

	private final int maxPlayers;
	BuildBattleGameType(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getMinPlayers() {
		return 2;
	}
}
