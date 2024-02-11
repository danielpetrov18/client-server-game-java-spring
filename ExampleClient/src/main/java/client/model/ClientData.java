package client.model;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import java.util.Optional;
import java.util.Collection;

import client.player.Player;
import client.player.EPlayer;
import client.map.fullmap.MyFullMap;
import client.map.fullmap.MyFullMapNode;
import client.player.EMyPlayerGameState;
import client.movement.coordinate.Coordinate;

/**
 * <p>This class encapsulates the game relevant data . It's the Model of the MVC.</p>
 */
public class ClientData {
	
	private MyFullMap map;
	private final Player myPlayer;
	private final Player enemyPlayer;	
	private final PropertyChangeSupport positionChanges; // Listens for changes in any of the players' coordinates.
	
	// Dependency injection - passing it over the ctor.
	public ClientData(final PropertyChangeSupport propChangeSupp) {
		this.map = new MyFullMap();
		this.myPlayer = new Player();
		this.enemyPlayer = new Player();
		this.positionChanges = propChangeSupp;
	}

	public MyFullMap getMap() {
		return this.map;
	}
	
	public void setMap(final Collection<MyFullMapNode> newMapNodes) {
		this.map.setMapNodes(newMapNodes);
	}
	
	/**
	 * @param p My or enemies player.
	 * @return Depending on what the user specifies a player is returned - either my or the enemies player object.
	 */
	public Player getPlayer(final EPlayer p) {
		switch(p) {
			case ME:
				return this.myPlayer;
			case ENEMY:
				return this.enemyPlayer;
			default:
				return null;
		}
	}
	
	public void registerPlayer(final String uacc, final String fname, final String lname, final String uniquePId, final EPlayer p) {	
		switch(p) {
			case ME:
				this.myPlayer.register(uacc, fname, lname);
				this.myPlayer.setPlayerId(uniquePId);
				return;
			case ENEMY:
				this.enemyPlayer.register(uacc, fname, lname);
				this.enemyPlayer.setPlayerId(uniquePId);
				return;
		}		
	}
	
	public String getPlayerId(final EPlayer p) {
		switch(p) {
			case ME:
				return this.myPlayer.getPlayerId();
			case ENEMY:
				return this.enemyPlayer.getPlayerId();
			default:
				return null;
		}
	}
	
	public boolean hasCollectedTreasure(final EPlayer p) {
		switch(p) {
			case ME:
				return this.myPlayer.hasCollectedTreasure();
			case ENEMY:
				return this.enemyPlayer.hasCollectedTreasure();
			default:
				return false;
		}
	}
	
	public Optional<EMyPlayerGameState> getPossiblePlayerState(final EPlayer p) {
		switch(p) {
			case ME:
				return this.myPlayer.getPossiblePlayerState();
			case ENEMY:
				return this.enemyPlayer.getPossiblePlayerState();
			default:
				return null;
		}
	}
		
	public Coordinate getPosition(final EPlayer p) {
		switch(p) {
			case ME:
				return this.myPlayer.getPositon();
			case ENEMY:
				return this.enemyPlayer.getPositon();
			default:
				return null;
		}
	}

	public void setPosition(final Coordinate newCoordinate, final EPlayer p) {
		switch (p) {
			case ME:
					this.myPlayer.setPosition(newCoordinate);
					return;
			case ENEMY:
					this.enemyPlayer.setPosition(newCoordinate);
					return;
		}
	}

	/**
	 * <p>This method notifies the view of the update.</p>
	 */
	public void notifyView() {
		this.positionChanges.firePropertyChange("playersPosition", false, true);
	}
	
	/**
	 * <p>Method that is used to register listeners (objects that are interested in the changes -> the View) in order to display data on the CLI.</p>
	 * @param listener That gets passed over from the views constructor to be subscribed to the model.
	 */
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		this.positionChanges.addPropertyChangeListener(listener);
	}
	
}