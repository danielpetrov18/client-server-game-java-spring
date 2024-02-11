package server.main.player;

/*
 * Contains the dynamic data that pertains to a particular player. 
 */
public class PlayerGameState {

	private EPlayerState state;
	private boolean hasTreasure;
	
	public PlayerGameState () {
		this.state = null;
		this.hasTreasure = false;
	}
	
	public EPlayerState getState() {
		return this.state;
	}
	
	public void updateState(final EPlayerState newState) {
		this.state = newState;
	}
	
	public boolean hasCollectedTreasure() {
		return this.hasTreasure;
	}
	
	public void collectTreasure() {
		this.hasTreasure = true;
	}
	
	@Override
	public String toString() {
		return "hasTreasure=" + this.hasTreasure + ", state=" + this.state;
	}
	
}