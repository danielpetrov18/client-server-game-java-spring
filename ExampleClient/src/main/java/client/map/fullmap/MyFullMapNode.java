package client.map.fullmap;

import client.map.EMyTerrain;
import client.movement.coordinate.Coordinate;
import client.map.halfmap.MyPlayerHalfMapNode;

public class MyFullMapNode extends MyPlayerHalfMapNode {
	
	private final static int MAX_X = 19;
	private final static int MAX_Y = 9;
	private final EMyFortState fortState;
	private final EMyTreasureState treasureState;
	private final EMyPlayerPositionState playerPositionState; 
	 
	public MyFullMapNode(EMyTerrain t, EMyPlayerPositionState pS, EMyTreasureState tS, EMyFortState fS, Coordinate coord) {
		// Passing to super ctor (Coordinate, terrain and fortPresent) -> min coords get checked there.
		// If the coordinates are invalid an exception will be thrown in the super constructor.
		super(coord, t, false, MAX_X, MAX_Y);
	
		this.fortState = fS;
		this.treasureState = tS;
		this.playerPositionState = pS;			
	}

	public EMyFortState getFortState() {
		return this.fortState;
	}
	
	public EMyTreasureState getTreasureState() {
		return this.treasureState;
	}	
	
	public EMyPlayerPositionState getPlayerPositionState() {
		return this.playerPositionState;
	}
	
	public boolean isMyPlayerHere() {
		return this.playerPositionState.equals(EMyPlayerPositionState.MY_PLAYER);
	}
	
	public boolean isEnemyPlayerHere() {
		return this.playerPositionState.equals(EMyPlayerPositionState.ENEMY_PLAYER);
	}
	
	public boolean areBothPlayersHere() {
		return this.playerPositionState.equals(EMyPlayerPositionState.BOTH_PLAYERS);
	}
	
	public boolean isMyCastleHere() {
		return this.fortState.equals(EMyFortState.MY_FORT);
	}
	
	public boolean isEnemyCastleHere() {
		return this.fortState.equals(EMyFortState.ENEMY_FORT);
	}
	
	public boolean isMyTreasureHere() {
		return this.treasureState.equals(EMyTreasureState.TREASURE_PRESENT);
	}
	
	@Override
	public String toString() {
		return "MyFMN [FS=" + fortState + ", TS=" + treasureState + ", PS=" + playerPositionState +  ", Coord=" + getPosition() + ", T=" + getTerrain() + "]";
	}
	
}