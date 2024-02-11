package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapExactlyOneCastleOnHalfmapAllowedException extends GenericExampleException {

	public MapExactlyOneCastleOnHalfmapAllowedException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}