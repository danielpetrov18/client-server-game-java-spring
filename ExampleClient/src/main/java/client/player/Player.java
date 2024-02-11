package client.player;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.movement.coordinate.Coordinate;

/**
 * <p>Stores all the relevant information about a player - [player id, registration information and the state + if the treasure has been collected or not].</p>
 */
public class Player {
	
	private String uniquePlayerId;
	private PlayerGameState playerState = new PlayerGameState();
	private PlayerRegisterInfo regInfo = new PlayerRegisterInfo();
	private final static Logger logger = LoggerFactory.getLogger(Player.class);
	private Coordinate currentPosition = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE); // At first it's invalid.
	
	public String getPlayerId() {
		return this.uniquePlayerId;
	}
	
	public void setPlayerId(final String uniquePlayerId) {
		this.uniquePlayerId = uniquePlayerId;
	}
	
	public String getUAccount() {
		return this.regInfo.getUAccount();
	}

	public String getFirstname() {
		return this.regInfo.getFirstname();
	}

	public String getLastname() {
		return this.regInfo.getLastname();
	}

	public boolean isRegistered() {
		return this.regInfo.isRegistered();
	}

	public void register(final String uacc, final String fname, final String lname) {
		// A player can only be registered once.
		if(!this.regInfo.isRegistered()) {
			this.regInfo = new PlayerRegisterInfo(uacc, fname, lname);			
		}
	}
 	
	public void collectTreasure() {
		// A player can collect his treasure only once.
		if(!this.hasCollectedTreasure()) {
			this.playerState.collectTreasure();
			logger.debug("Player {} has collected his treasure.", this.uniquePlayerId);
		}
	}
	
	public boolean hasCollectedTreasure() {
		return this.playerState.hasCollectedTreasure();
	} 
	
	public Optional<EMyPlayerGameState> getPossiblePlayerState() {
		return this.playerState.getPossibleState();
	} 
	
	public void setPlayerState(final EMyPlayerGameState newState) {
		logger.debug("{} old state: [{}]", this.uniquePlayerId, this.getPossiblePlayerState());
		this.playerState.updateState(newState);
		logger.debug("{} new state: [{}]", this.uniquePlayerId, this.getPossiblePlayerState());
	}
	
	public PlayerGameState getGameState() {
		return this.playerState;
	}
	
	public Coordinate getPositon() {
		return this.currentPosition;
	}
	
	public void setPosition(final Coordinate newPosition) {
		logger.debug("{} old position: [{}]", this.uniquePlayerId, this.currentPosition);
		this.currentPosition = newPosition;
		logger.debug("{} new position: [{}]", this.uniquePlayerId, this.currentPosition);
	}

	@Override
	public String toString() {
		return "Player [ID=" + uniquePlayerId + ", " + this.regInfo + ", " + this.playerState + ", " + this.currentPosition + "]";
	}
	
}