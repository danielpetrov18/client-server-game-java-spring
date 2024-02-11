package server.main.player;

/*
 * Holds all player relevant data. That is available at a specific time.
 */
public class Player {

	private final String playerId;
	private final PlayerInfo registerInfo;
	private final PlayerGameState playerState;
	
	public Player (final String fname, final String lname, final String uacc, final String playerId) {
		this.playerId = playerId;
		this.registerInfo = new PlayerInfo(fname, lname, uacc);
		this.playerState = new PlayerGameState();
	}
	
	public String getFirstname() {
		return this.registerInfo.getFirstname();
	}
	
	public String getLastname() {
		return this.registerInfo.getLastname();
	}
	
	public String getUAccount() {
		return this.registerInfo.getUAccount();
	}
	
	public String getPlayerId() {
		return this.playerId;
	}
	
	public EPlayerState getState() {
		return this.playerState.getState();
	}
	
	public void updateState(final EPlayerState newState) {
		this.playerState.updateState(newState);
	}
	
	public boolean hasCollectedTreasure() {
		return this.playerState.hasCollectedTreasure();
	}
	
	public void collectTreasure() {
		if(!this.playerState.hasCollectedTreasure())
			this.playerState.collectTreasure();
	}
	
	@Override
	public String toString() {
		return "[playerId= " + this.playerId + " " + this.registerInfo + " " + this.playerState + ']';
	}
	
}