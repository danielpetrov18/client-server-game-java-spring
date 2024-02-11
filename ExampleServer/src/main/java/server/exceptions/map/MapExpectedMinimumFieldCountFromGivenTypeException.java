package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapExpectedMinimumFieldCountFromGivenTypeException extends GenericExampleException {
	
	public MapExpectedMinimumFieldCountFromGivenTypeException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}