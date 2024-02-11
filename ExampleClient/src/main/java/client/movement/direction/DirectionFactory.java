package client.movement.direction;

import client.map.EMyTerrain;
import client.map.fullmap.MyFullMapNode;

/**
 * <p>This class is used to generate new direction objects to be used by the controller.
 *    A direction object consists of a direction and needed rounds to fully make a move.</p>
 */
public class DirectionFactory {

	/**
	 * @param previous The previous node the AI was on.
	 * @param current The new node the AI wants to visit.
	 * @return Returns a direction and an integer representing the number of rounds needed to leave the current node and visit the new one.
	 */
	public static Direction of(final MyFullMapNode previous, final MyFullMapNode current) {
		// Calculates the nessecery rounds + direction.
		final int rounds = calculateRounds(current, previous);
		final EMyMove direction = calculateDirection(current, previous);	
		return new Direction(rounds, direction);
	}
	
	/**
	 * @param current Current node we are trying to visit.
	 * @param previous Previous node we are trying to leave.
	 * @return Based on the terrains of both nodes we can compute how many round we would need to send the same move to the server.
	 */
	private static int calculateRounds(final MyFullMapNode current, final MyFullMapNode previous) {
		int totalRounds = 0;
		
		// One round to leave or enter a grass field.
		// Two rounds to leave or enter a mountain field.
		
		totalRounds += determineRoundsBasedOnNodeType(previous.getTerrain());
		totalRounds += determineRoundsBasedOnNodeType(current.getTerrain());
		
		return totalRounds;
	}
	
	/**
	 * @param currentNode New node we are currently entering. 
	 * @param previousNode Node we are trying to leave.
	 * @return Based on the coordinates of the two nodes we can find out where the AI should go next.
	 */
	private static EMyMove calculateDirection(final MyFullMapNode currentNode, final MyFullMapNode previousNode) {
		// If x values same -> change of rows.
		if(previousNode.getX() == currentNode.getX()) {
			// If new y less than previous -> up.
			if(currentNode.getY() < previousNode.getY()) {
				return EMyMove.UP;				
			}
			else {
				return EMyMove.DOWN;				
			}
		}
		// If y values same -> change of columns.
		else {
			// If new x bigger than previous -> right.
			if(currentNode.getX() > previousNode.getX()) {
				return EMyMove.RIGHT;				
			}
			else {
				return EMyMove.LEFT;				
			}
		}
	} 
	
	/**
	 * @param terrain Type of the node.
	 * @return Returns the number of rounds necessery to either leave or visit a node of the specified type.
	 */
	private static int determineRoundsBasedOnNodeType(final EMyTerrain terrain) {
		if(terrain.equals(EMyTerrain.GRASS))
			return 1;
		
		// If it's mountain field.
		return 2;
	}
	
}