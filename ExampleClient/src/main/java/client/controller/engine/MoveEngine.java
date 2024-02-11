package client.controller.engine;

import client.player.EPlayer;
import client.model.ClientData;
import client.movement.EObjective;
import client.movement.direction.EMyMove;
import client.movement.finder.ObjectiveFinder;

public class MoveEngine {

	private ObjectiveFinder finder;
	
	/**
	 * <p>Controlls the PathFinder class.</p>
	 * <p>First checks if the treasure is collected. If not it initializes the algorithm class.</p>
	 * <p>After the treasure has been found the AI figure should cross to the other side of the map.</p>
	 * <p>Lastly, the AI figure should find the enemy castle.</p>
	 * @return Returns a direction in which the player AI should move.
	 */
	public EMyMove generateMovement(final ClientData data) {	
		if(!data.hasCollectedTreasure(EPlayer.ME)) {
			// Check if objective finder is created.
			if(this.finder == null) {
				this.finder = new ObjectiveFinder(data.getMap());
				this.finder.findObjective(data.getMap(), EObjective.MY_TREASURE);
			}
			
			// Check if the current direction is not exhausted.
			if(this.finder.isDirectionExhausted()) {
				this.finder.findObjective(data.getMap(), EObjective.MY_TREASURE);
			}
		}
		else {
			if(!this.finder.isOnOtherSide(data.getMap())) {
				if(this.finder.isDirectionExhausted()) {
					this.finder.findObjective(data.getMap(), EObjective.CROSSING_SIDES);									
				}
			}
			else {
				if(this.finder.isDirectionExhausted()) {
					this.finder.findObjective(data.getMap(), EObjective.ENEMY_CASTLE);
				}
			}
		}
		
		return this.finder.getDirection();
	}
	
}