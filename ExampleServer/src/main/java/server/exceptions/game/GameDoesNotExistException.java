package server.exceptions.game;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class GameDoesNotExistException extends GenericExampleException {

	public GameDoesNotExistException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}