package client.player;

import java.util.Optional;

/**
 * <p>Stores the dynamic data of a player - if treasure has been collected and the state.</p>
 */
public class PlayerGameState {

	private boolean hasTreasure;
	private EMyPlayerGameState state;
	
	public PlayerGameState() {
		this.hasTreasure = false;
		this.state = null;
	}
	
	public boolean hasCollectedTreasure() {
		return this.hasTreasure;
	}

	public void collectTreasure() {
		this.hasTreasure = true;
	}

	// I've used Optional because at first the state is null.
	public Optional<EMyPlayerGameState> getPossibleState() {
		return Optional.ofNullable(this.state);
	}
	
	public void updateState(final EMyPlayerGameState newState) {
		this.state = newState;			
	}
	
	@Override
	public String toString() {
		return "[hasTreasure=" + this.hasTreasure + ", state=" + this.state + "]";
	}
	
}