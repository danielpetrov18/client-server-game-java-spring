package server.main.rules.game;

import server.main.rules.IBusinessRule;
import server.exceptions.game.GameMaxCapacityExceededException;

public class GameMaxCapacityRule implements IBusinessRule {

	private int gameCount;
	final static int MAX_PARALLEL_GAME_COUNT = 99;
	
	public GameMaxCapacityRule(final int gameCount) {
		this.gameCount = gameCount;
	}

	@Override
	public void validate() {
		if(this.gameCount > MAX_PARALLEL_GAME_COUNT) {
			final String errMsg = "Maximum game capacity of " + MAX_PARALLEL_GAME_COUNT + " was exceeded! Current game count is: [" + this.gameCount + "]!";
			throw new GameMaxCapacityExceededException("MaxGameCapacityExceededException", errMsg);
		}
	}
	
}