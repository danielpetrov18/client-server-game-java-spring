package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapInvalidNodeCoordinateValueException extends GenericExampleException {

	public MapInvalidNodeCoordinateValueException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}