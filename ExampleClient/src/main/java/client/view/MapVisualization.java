package client.view;

import java.util.Collection;

import client.player.Player;
import client.view.color.EColor;
import client.map.fullmap.EMyFortState;
import client.map.fullmap.MyFullMapNode;
import client.movement.coordinate.Coordinate;
import client.view.emojis.VisualizationEmojis;
import client.map.fullmap.EMyPlayerPositionState;
import client.map.fullmap.EMyTreasureState;
import client.movement.coordinate.calculator.CoordinatesCalculator;

/**
 * <p>Visualizes the latest map.</p>
 * <p>All different types of nodes + meta data about location of a given player and fortresses.</p>
 */
public class MapVisualization {
	
	// At first its going to be invalid until the AI has uncovered its location.
	// I decided not to define this variable in the map because it's relevant only for the visualization.
	private static Coordinate treasureCoordinate = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
	
	/**
	 * @param map The most recent map after the gamestate was requested.
	 * @param player The corresponding information about my player.
	 * @return Returns a string of the current map representation to be displayed by the view.
	 */
	public static String mapString(final Collection<MyFullMapNode> mapNodes, final Player player) {
		final var result = new StringBuilder();
		
		// Calculate the map boundaries. Relevant for the for loop.
		final Coordinate minCoords = CoordinatesCalculator.getMapMinCoordinates(mapNodes);
		final Coordinate maxCoords = CoordinatesCalculator.getMapMaxCoordinates(mapNodes);
		
		// Using nested for-loop instead of Iterator<FullMapNode> because its easier to access the map nodes.
		for(int row = minCoords.getY(); row <= maxCoords.getY(); ++row) {
			for(int col = minCoords.getX(); col <= maxCoords.getX(); ++col) {
				final int curRow = row;
				final int curCol = col;

				// The current column and row values are used to access the nodes from left to right, from top to bottom of the map.
				final MyFullMapNode currNode = mapNodes
												.stream()
												.filter(n -> n.getX() == curCol && n.getY() == curRow)
												.findFirst()
												.get();
				
				switch(currNode.getTerrain()) {
					case MOUNTAIN:
						if(currNode.isMyPlayerHere()) {
							result.append(EColor.PURPLE_BG + VisualizationEmojis.getPlayer(EMyPlayerPositionState.MY_PLAYER) + " " + EColor.RESET);							
						}
						else if(currNode.isEnemyPlayerHere()) {
							result.append(EColor.RED_BG + VisualizationEmojis.getPlayer(EMyPlayerPositionState.ENEMY_PLAYER) + " " + EColor.RESET);									
						}
						else if(currNode.areBothPlayersHere()) {
							result.append(EColor.CYAN_BG + VisualizationEmojis.getPlayer(EMyPlayerPositionState.BOTH_PLAYERS) + " " + EColor.RESET);							
						}
						else {
							result.append(EColor.BLACK_BG + "" + EColor.WHITE + " # "  + EColor.RESET);													
						}
						break;
						
					case GRASS:
						if(currNode.isMyPlayerHere()) {
							result.append(EColor.PURPLE_BG + VisualizationEmojis.getPlayer(EMyPlayerPositionState.MY_PLAYER) + " " + EColor.RESET);														
						}
						else if(currNode.isEnemyPlayerHere()) {
							result.append(EColor.RED_BG + VisualizationEmojis.getPlayer(EMyPlayerPositionState.ENEMY_PLAYER) + " " + EColor.RESET);														
						}
						else if(currNode.areBothPlayersHere()) {
							result.append(EColor.CYAN_BG + VisualizationEmojis.getPlayer(EMyPlayerPositionState.BOTH_PLAYERS) + " " + EColor.RESET);							
						}
						else if(currNode.isMyCastleHere()) {
							result.append(VisualizationEmojis.getCastle(EMyFortState.MY_FORT) + " ");														
						}
						else if(currNode.isEnemyCastleHere()) {
							result.append(VisualizationEmojis.getCastle(EMyFortState.ENEMY_FORT) + " ");														
						}
						else if(currNode.isMyTreasureHere() && !player.hasCollectedTreasure()) {
							result.append(EColor.YELLOW_BG + "" + EColor.WHITE + " $ " + EColor.RESET);
							treasureCoordinate = new Coordinate(currNode.getX(), currNode.getY());
						}
						else if(treasureCoordinate.getX() == curCol && treasureCoordinate.getY() == curRow && player.hasCollectedTreasure()) {
							result.append(EColor.YELLOW_BG + "" + EColor.WHITE + " # " + EColor.RESET);														
						}
						else {
							result.append(EColor.GREEN_BG + "" + EColor.WHITE + " # " + EColor.RESET);														
						}
						break;
						
					case WATER:
						result.append(EColor.BLUE_BG + "" + EColor.WHITE + " # " + EColor.RESET);
						break;
				}
			}
			// End of nested for-loop.
			result.append("\n");
		}

		return result.toString();
	}
	
}