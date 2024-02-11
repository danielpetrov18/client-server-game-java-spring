package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapInvalidFullMapPlacementException extends GenericExampleException {

	public MapInvalidFullMapPlacementException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}