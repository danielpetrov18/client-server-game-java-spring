package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapInaccessibleNodeHalfmapException extends GenericExampleException {

	public MapInaccessibleNodeHalfmapException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}