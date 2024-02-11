package server.exceptions.game;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class GameNonRegisteredPlayerException extends GenericExampleException {
	
	public GameNonRegisteredPlayerException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}