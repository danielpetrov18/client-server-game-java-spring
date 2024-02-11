package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapInvalidCardPlacementException extends GenericExampleException {

	public MapInvalidCardPlacementException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}