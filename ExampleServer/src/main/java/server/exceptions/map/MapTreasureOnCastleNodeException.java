package server.exceptions.map;

import server.exceptions.GenericExampleException;

@SuppressWarnings("serial")
public class MapTreasureOnCastleNodeException extends GenericExampleException {

	public MapTreasureOnCastleNodeException(String errorName, String errorMessage) {
		super(errorName, errorMessage);	}

}