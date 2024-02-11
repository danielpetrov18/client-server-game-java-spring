package server.exceptions.game;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class GameMaxCapacityExceededException extends GenericExampleException {

	public GameMaxCapacityExceededException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}