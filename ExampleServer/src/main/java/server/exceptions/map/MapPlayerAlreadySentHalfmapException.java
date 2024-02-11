package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapPlayerAlreadySentHalfmapException extends GenericExampleException {

	public MapPlayerAlreadySentHalfmapException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}