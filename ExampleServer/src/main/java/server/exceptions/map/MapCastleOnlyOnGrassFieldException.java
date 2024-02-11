package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapCastleOnlyOnGrassFieldException extends GenericExampleException {

	public MapCastleOnlyOnGrassFieldException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}