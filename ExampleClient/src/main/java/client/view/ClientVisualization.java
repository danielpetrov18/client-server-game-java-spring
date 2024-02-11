package client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import client.player.EPlayer;
import client.model.ClientData;

/**
  * <p>The View used to display the information after every update on the game state.</p>
 */
public class ClientVisualization implements PropertyChangeListener {
	
	// The observable object.
	private final ClientData data;
	
	/**
	 * <p>The CTOR sets this view as subcriber to the data class.</p>
	 * @param data The observable object. The view listens for changes in its state and displays it accordingly.
	 */
	public ClientVisualization(final ClientData data) {
		this.data = data;
		/* 
		 * Subscribes to the model and gets notified when a given bound variable is changed.
		 * In the case of my project the controller the view is notified when atleast one 
		 * of the players coordinates have changed after the last game state request.
		*/
		this.data.addPropertyChangeListener(this);
	}

	/**
	 * <p>If a bound property changes this method is called. The bound property in this case are the coordinates of the players.</p>
	 * @param evt The event that was fired after change of a bounded property in the model.
	*/
	@Override
	public void propertyChange(final PropertyChangeEvent evt) {	
		String toDisplay = "";
			
		// Only if atleast one of the players have sent their halfmap.
		if(!this.data.getMap().isMapEmpty() && this.data.getPlayer(EPlayer.ME).isRegistered() && this.data.getPlayer(EPlayer.ENEMY).isRegistered()) {
			toDisplay = this.playersDataString();	
			toDisplay += "\n";
			toDisplay += this.mapString();
		} 
		// Print this when both players have already registered, but still no map is available.
		else if(this.data.getPlayer(EPlayer.ME).isRegistered() && this.data.getPlayer(EPlayer.ENEMY).isRegistered()) {
			toDisplay = this.playersDataString();								
		}
			
		// Print out the whole available information at this point.
		System.out.println(toDisplay);
	}
	
	/**
	 * @return Formated string containing all relevant for the game information about both players.
	 */
	private String playersDataString() { 
		return PlayerVisualization.playersInfo(this.data.getPlayer(EPlayer.ME), this.data.getPlayer(EPlayer.ENEMY));
	}
		
	/**
	 * @return Formated string containing all the map fields and relevant meta data like color or objects/entitites.
	 */
	private String mapString() {
		return MapVisualization.mapString(this.data.getMap().getMapNodes(), this.data.getPlayer(EPlayer.ME));
	}
	
}