package server.main.map.fullmap;

import server.main.map.Coordinate;
import server.main.map.EMyTerrain;
import server.exceptions.map.MapInvalidNodeCoordinateValueException;

public class MyFullMapNode {
	
	private final static int MIN_X = 0;
	private final static int MAX_X = 19;
	private final static int MIN_Y = 0;
	private final static int MAX_Y = 9;

	private final String playerId;
	private final EMyTerrain terrain;
	private final Coordinate coordinate;
	private final EMyFortState fortState;
	private EMyTreasureState treasureState;
	private final EMyPlayerPositionState playerPositionState; 
	 
	public MyFullMapNode(String playerId, EMyTerrain t, EMyPlayerPositionState pS, EMyTreasureState tS, EMyFortState fS, Coordinate coord) {
		if(coord.getX() < MIN_X && coord.getX() > MAX_X && coord.getY() < MIN_Y && coord.getY() > MAX_Y) {
			throw new MapInvalidNodeCoordinateValueException("InvalidPlayerNodeCoordinateValuesException", "Invalid coordinate: " + coord + "!");
		}
	
		this.terrain = t;
		this.coordinate = coord;
		this.fortState = fS;
		this.treasureState = tS;
		this.playerId = playerId;
		this.playerPositionState = pS;			
	}

	public String getPlayerId() {
		return this.playerId;
	}

	public EMyTerrain getTerrain() {
		return this.terrain;
	}
	
	public boolean compareTerrain(final EMyTerrain other) {
		return this.terrain.equals(other);
	}
	
	public Coordinate getPosition() {
		return this.coordinate;
	}	
	
	public int getX() {
		return this.coordinate.getX();
	}
	
	public int getY() {
		return this.coordinate.getY();
	}
	
	public Coordinate getCoordinate() {
		return this.coordinate;
	}
	
	public EMyFortState getFortState() {
		return this.fortState;
	}
	
	public boolean compareFortState(final EMyFortState other) {
		return this.fortState.equals(other);
	}
	
	public EMyTreasureState getTreasureState() {
		return this.treasureState;
	}	
	
	public void placeTreasure() {
		this.treasureState = EMyTreasureState.MyTreasureIsPresent;
	}
	
	public EMyPlayerPositionState getPlayerPositionState() {
		return this.playerPositionState;
	}
	
	public boolean amIHere(final String playerID) {
		return this.playerId.equals(playerID) && this.playerPositionState.equals(EMyPlayerPositionState.MyPlayerPosition);
	}
	
	@Override
	public String toString() {
		return "pId=" + this.playerId + ", coord=" + this.coordinate + ", fS=" + this.fortState + ", tS=" + this.treasureState 
				+ ", t=" + this.terrain + ", posS=" + this.playerPositionState + "";
	}
	
}