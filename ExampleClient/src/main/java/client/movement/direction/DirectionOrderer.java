package client.movement.direction;

import client.map.EMapType;
import client.movement.EObjective;
import client.map.fullmap.MyFullMapNode;
import client.movement.coordinate.Coordinate;

/**
 * <p>
 * 		The idea of this class is basically to reorder the directions in such a way so that the 
 * 		preferred direction would be on first position. For example if my AI is on the left side 
 * 		of the fullmap and is trying to cross to the other side the preferred direction would 
 * 		be right so it can move rightwards and reach the other side of the fullmap as fast as possible.
 * </p> 
 */
public class DirectionOrderer {

	// Invalid directions by default.
	private static int[][] directions = {
			{Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE}, 
			{Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE}, 
			{Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE},
			{Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE}
	};
	// All possible valid directions that get reordered in the variable on top based on current position of the AI, map type and what we are looking for.
	private final static int[][] NEIGHBORS = {{-1, 0},{0, -1}, {1, 0}, {0, 1}};
	
	/**
	 * @return A specific order of directions in which the AI would have to go depending on the current position and objective.
	 */
	public static int[][] orderDirections(final MyFullMapNode currentPosition, final EMapType mapType, final EObjective objective) {
		// No need to change the neighbors exploration strategy. Standard one.
		if(objective.equals(EObjective.ENEMY_CASTLE) || objective.equals(EObjective.MY_TREASURE)) {
			return NEIGHBORS;			
		}
	
		// If the player has to cross to the enemies halfmap to find the enemy castle.
		if(mapType.equals(EMapType.RECTANGLE)) {
			// Currently on the left side of the map -> prefer rightwards. 
			if(currentPosition.getX() < 10) {		
				// rightwards
				directions[0] = NEIGHBORS[2];
				
				// upwards
				directions[1] = NEIGHBORS[1];
				
				// downwards
				directions[2] = NEIGHBORS[3];
				
				// leftwards
				directions[3] = NEIGHBORS[0];
			}
			// Currently on the right side of the map -> prefer leftwards.
			else {
				// leftwards
				directions[0] = NEIGHBORS[0];

				// upwards
				directions[1] = NEIGHBORS[1];
				
				// downwards
				directions[2] = NEIGHBORS[3];

				// prefer rightwards
				directions[3] = NEIGHBORS[2];	
			}
		}
		else {
			// Currently on the top side of the map -> prefer downwards.
			if(currentPosition.getY() < 5) {
				// downwards
				directions[0] = NEIGHBORS[3];

				// leftwards
				directions[1] = NEIGHBORS[0];
				
				// rightwards
				directions[2] = NEIGHBORS[2];

				// upwards
				directions[3] = NEIGHBORS[1];
			}
			// Currently on the bottom side of the map -> prefer upwards.
			else {								
				// upwards
				directions[0] = NEIGHBORS[1];

				// leftwards
				directions[1] = NEIGHBORS[0];
				
				// rightwards
				directions[2] = NEIGHBORS[2];
				
				// downwards
				directions[3] = NEIGHBORS[3];
			}
		}
		
		return directions;
	}
	
}