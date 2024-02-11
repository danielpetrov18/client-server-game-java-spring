package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapTreasureNotOnGrassNodeException extends GenericExampleException {

	public MapTreasureNotOnGrassNodeException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}