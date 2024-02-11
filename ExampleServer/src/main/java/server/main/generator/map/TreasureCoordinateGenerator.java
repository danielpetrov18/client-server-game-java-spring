package server.main.generator.map;

import java.util.Set;

import server.main.map.Coordinate;
import server.main.map.EMyTerrain;
import server.main.map.fullmap.EMyFortState;
import server.main.map.fullmap.MyFullMapNode;

public class TreasureCoordinateGenerator {

	/*
	 * Generates a random position upon receiving the half map by the server.
	 * 
	 * The treasure can be placed only on a grass field where a castle is not present.
	 * There can only be one treasure for each player in each game.
	 */
	public static Coordinate generateFullMapTreasurePosition(final Set<MyFullMapNode> halfmap) {
		return halfmap
					.stream()
					.filter(node -> node.compareTerrain(EMyTerrain.Grass))				      // Get all grass fields.
					.filter(node -> !node.compareFortState(EMyFortState.MyFortPresent))       // Remove the field that contains my castle.
					.findFirst()														      // Returns a random grass field without a castle.
					.get()
					.getCoordinate();
	}
	
}