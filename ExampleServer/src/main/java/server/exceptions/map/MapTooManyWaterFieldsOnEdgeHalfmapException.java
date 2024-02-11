package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapTooManyWaterFieldsOnEdgeHalfmapException extends GenericExampleException {

	public MapTooManyWaterFieldsOnEdgeHalfmapException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}