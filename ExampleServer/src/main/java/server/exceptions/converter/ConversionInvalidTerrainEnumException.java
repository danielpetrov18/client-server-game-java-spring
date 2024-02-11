package server.exceptions.converter;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class ConversionInvalidTerrainEnumException extends GenericExampleException {

	public ConversionInvalidTerrainEnumException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}
	
}