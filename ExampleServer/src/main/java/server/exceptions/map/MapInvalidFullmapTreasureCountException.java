package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapInvalidFullmapTreasureCountException extends GenericExampleException {

	public MapInvalidFullmapTreasureCountException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}