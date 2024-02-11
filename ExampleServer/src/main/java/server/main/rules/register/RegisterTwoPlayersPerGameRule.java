package server.main.rules.register;

import server.main.game.Game;
import server.main.rules.IBusinessRule;
import server.exceptions.game.GameTwoPlayersAllowedRegisterException;

public class RegisterTwoPlayersPerGameRule implements IBusinessRule {

	private final int playersCount;
	
	public RegisterTwoPlayersPerGameRule(final int playersCount) {
		this.playersCount = playersCount;
	}
	
	@Override
	public void validate() {
		if(this.playersCount >= Game.MAX_STUDENT_COUNT) {
			final String errMsg = "Two players are already registered for a game!";
			throw new GameTwoPlayersAllowedRegisterException("GameTwoPlayersAllowedRegisterException", errMsg);
		}
	}
	
}