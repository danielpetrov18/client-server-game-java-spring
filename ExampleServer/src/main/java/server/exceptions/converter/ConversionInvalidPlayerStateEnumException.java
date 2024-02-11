package server.exceptions.converter;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class ConversionInvalidPlayerStateEnumException extends GenericExampleException {

	public ConversionInvalidPlayerStateEnumException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}