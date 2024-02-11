package client.movement.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.EMyTerrain;
import client.movement.EObjective;
import client.map.fullmap.MyFullMap;
import client.map.fullmap.MyFullMapNode;
import client.movement.coordinate.Coordinate;

public class MoveValidator {
	
	private final static Logger logger = LoggerFactory.getLogger(MoveValidator.class);
	
	/**
	 * @param map The most recent map.
	 * @param coord The new nodes coordinate.
	 * @param objective The thing we are trying to do -> look for treasure, enemy castle or cross to the other side of the map.
	 * @param visitedNodes A 2d array of nodes that are explored already.
	 * @return Returns true if a movement in that direction is possible. (e.i., not a water field, unvisited, inside the map boundaries).
	 */
	public static boolean isValidMove(final MyFullMap map, final Coordinate coord, final EObjective objective, final boolean[][] visitedNodes) {
		if(objective.equals(EObjective.MY_TREASURE)) {
			if(coord.getX() < map.getMinTreasure().getX() || coord.getX() > map.getMaxTreasure().getX() 
			|| coord.getY() < map.getMinTreasure().getY() || coord.getY() > map.getMaxTreasure().getY()) {
				logger.debug("{} is INVALID.", coord);
				return false;
			}						
		}
		else if (objective.equals(EObjective.ENEMY_CASTLE)){
			if(coord.getX() < map.getMinEnemyCastle().getX() || coord.getX() > map.getMaxEnemyCastle().getX() 
			|| coord.getY() < map.getMinEnemyCastle().getY() || coord.getY() > map.getMaxEnemyCastle().getY()) {
				logger.debug("{} is INVALID.", coord);
				return false;
			}
		}
		else {
			if(coord.getX() < 0 || coord.getX() > map.getMax().getX()
			|| coord.getY() < 0 || coord.getY() > map.getMax().getY()) {
				logger.debug("{} is INVALID.", coord);
				return false;
			}
		}

		final MyFullMapNode newNode = map
										.getMapNodes()
										.stream()
										.filter(n -> n.getX() == coord.getX() && n.getY() == coord.getY())
										.findFirst()
										.get();	
				
		if(newNode.getTerrain().equals(EMyTerrain.WATER)) {
			logger.debug("{} is INVALID because a water field.", coord);
			return false;
		}
		
		if(visitedNodes[newNode.getX()][newNode.getY()]) {
			logger.debug("{} is INVALID because already visited. ", coord);
			return false;
		}
		
		return true;
	}
	
	/**
	 * @return True if the AI figure is on the other side of the map.
	 */
	public static boolean isOnOtherSide(final MyFullMap map, final MyFullMapNode currentPosition) {
		boolean isValidX = currentPosition.getX() >= map.getMinEnemyCastle().getX() 
						&& currentPosition.getX() <= map.getMaxEnemyCastle().getX();
		
		boolean isValidY = currentPosition.getY() >= map.getMinEnemyCastle().getY() 
						&& currentPosition.getY() <= map.getMaxEnemyCastle().getY();
		
		return isValidX && isValidY;
	}
	
}