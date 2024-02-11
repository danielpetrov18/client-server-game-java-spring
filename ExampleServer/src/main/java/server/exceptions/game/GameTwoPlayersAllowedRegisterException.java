package server.exceptions.game;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class GameTwoPlayersAllowedRegisterException extends GenericExampleException {

	public GameTwoPlayersAllowedRegisterException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}