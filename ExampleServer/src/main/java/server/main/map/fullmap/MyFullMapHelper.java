package server.main.map.fullmap;

import java.util.Set;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import server.main.map.Coordinate;
import server.exceptions.map.MapInvalidCardPlacementException;
import server.exceptions.map.MapInvalidFullMapPlacementException;

public class MyFullMapHelper {

	private final static Random RND = new Random();
	private final static int INCREMENT_Y_COORDINATE = 5;  // This is used when the map shape is square and a half map needs to be placed on the bottom half.
	private final static int INCREMENT_X_COORDINATE = 10; // This is used when the map shape is rectangular and a half map needs to be placed on the right side.
	
	
	/*
	 * Depending on the position the nodes that are to be inserted can get updated.
	 * 	  For example: If the position is on the right (rectangle map) one should increment
	 * 	  the X-value of each nodes coordinate (increasing the column index) by 10.
	 *    If one is to place the nodes on the left or top side no update has to be done.
	 */
	public static void placeHalfmap(final EFullMapPlacement pos, final Set<MyFullMapNode> map, final Set<MyFullMapNode> insertNodes, final String playerId) {
		Set<MyFullMapNode> updatedNodes = insertNodes;
		
		switch(pos) {
			default:
				final String errMsg = "Invalid card placement: [" + pos + "]!";
				throw new MapInvalidFullMapPlacementException("MapInvalidFullMapPlacementException", errMsg);
			case Left:
			case Top:
				break;
			case Right:
				updatedNodes = insertNodes
									.stream()
			    					.map(node -> new MyFullMapNode(
			    						 node.getPlayerId(),
			    						 node.getTerrain(),
			    						 node.getPlayerPositionState(),
			    						 node.getTreasureState(),
			    						 node.getFortState(),
			    						 new Coordinate(node.getX() + INCREMENT_X_COORDINATE, node.getY())
			    					))
			    					.collect(Collectors.toSet());
				break;
			case Bottom:
				updatedNodes = insertNodes
									.stream()
									.map(node -> new MyFullMapNode(
										 node.getPlayerId(),
										 node.getTerrain(),
										 node.getPlayerPositionState(),
										 node.getTreasureState(),
										 node.getFortState(),
										 new Coordinate(node.getX(), node.getY() + INCREMENT_Y_COORDINATE)
									))
									.collect(Collectors.toSet());
				break;
		}
		
		map.addAll(updatedNodes);
	}
	
	public static EFullMapType pickRandomShape() {
		return List.of(EFullMapType.values()).get(randomIndexBetween0And1());
	}
	
	public static EFullMapPlacement pickCardPlacement(final EFullMapType shape) {
		if(shape.equals(EFullMapType.Rectangle))
			return List.of(EFullMapPlacement.Left, EFullMapPlacement.Right).get(randomIndexBetween0And1());
		
		return List.of(EFullMapPlacement.Top, EFullMapPlacement.Bottom).get(randomIndexBetween0And1());
	}
	
	public static EFullMapPlacement getOppositeCardPlacement(final EFullMapPlacement placement) {
		switch(placement) {
			case Left:
				return EFullMapPlacement.Right;
			case Right:
				return EFullMapPlacement.Left;
			case Top:
				return EFullMapPlacement.Bottom;
			case Bottom:
				return EFullMapPlacement.Top;
			default:
				throw new MapInvalidCardPlacementException("MapInvalidCardPlacementException", "Invalid card placement!");
		}
	}
	
	private static int randomIndexBetween0And1 () {
		return RND.nextInt(2);
	}
	
}