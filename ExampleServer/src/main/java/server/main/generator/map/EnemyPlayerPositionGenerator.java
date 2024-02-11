package server.main.generator.map;

import java.util.Set;

import server.main.map.Coordinate;
import server.main.map.fullmap.MyFullMapNode;

public class EnemyPlayerPositionGenerator {
	
	/*
	 * Generates random position for the enemy player during the first 16 rounds.
	 * 
	 * The position is completely random regardless of field type or half map.
	 * This means a valid position might be a water field.
	 */
	public static Coordinate generateRandomCoordinate(final Set<MyFullMapNode> nodes) {
		return nodes
				  .stream()
				  .findFirst()
				  .get()
				  .getPosition();
	}
	
}