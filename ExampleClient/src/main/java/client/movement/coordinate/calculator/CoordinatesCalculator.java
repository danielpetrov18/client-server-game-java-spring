package client.movement.coordinate.calculator;

import java.util.Collection;
import java.util.Comparator;

import client.map.EMapType;
import client.map.fullmap.MyFullMap;
import client.map.fullmap.MyFullMapNode;
import client.movement.coordinate.Coordinate;

/**
 * <p>Calculates different coordinates that are relevant for the pathfinding algorithm.</p>
 */
public class CoordinatesCalculator {
	
	//Calculates the min and max coordinates of both my treasure and the enemies castle.
	public static void calculateHalfMapBoundaries(final MyFullMap map, final MyFullMapNode startingNode) {
		EMapType mapType;
		Coordinate minTreasureCoords = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
		Coordinate maxTreasureCoords = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
		Coordinate minEnemyCastleCoords = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
		Coordinate maxEnemyCastleCoords = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
		
		// Max boundaries of the map.
		final Coordinate mapMaxCoordinates = CoordinatesCalculator.getMapMaxCoordinates(map.getMapNodes());
		
		// Calculating the type of the map based on the max coordinates.
		if(mapMaxCoordinates.getX() >= 10) {
			mapType = EMapType.RECTANGLE;			
		}
		else {
			mapType = EMapType.SQUARE;			
		}	
		
		// 5x20
		if(mapType.equals(EMapType.RECTANGLE)) {
			// Find out if in left or right part
			if(startingNode.getX() >= 10) {
				// Based on where my treasure is -> the enemy castle would be on the rest of the map.
				minTreasureCoords = new Coordinate(10, 0);
				maxTreasureCoords = new Coordinate(mapMaxCoordinates.getX(), mapMaxCoordinates.getY());
				
				minEnemyCastleCoords = new Coordinate(0, 0);
				maxEnemyCastleCoords = new Coordinate(9, mapMaxCoordinates.getY());
			}
			else {
				minTreasureCoords = new Coordinate(0, 0);
				maxTreasureCoords = new Coordinate(9, mapMaxCoordinates.getY());
				
				minEnemyCastleCoords = new Coordinate(10, 0);
				maxEnemyCastleCoords = new Coordinate(mapMaxCoordinates.getX(), mapMaxCoordinates.getY());
			}
		}
		// 10x10
		else {
			// Find out if in upper or lower.
			if(startingNode.getY() >= 5) {
				// Lower
				minTreasureCoords = new Coordinate(0, 5);
				maxTreasureCoords = new Coordinate(mapMaxCoordinates.getX(), mapMaxCoordinates.getY());
				
				minEnemyCastleCoords = new Coordinate(0, 0);
				maxEnemyCastleCoords = new Coordinate(mapMaxCoordinates.getX(), 4);
			}
			else {
				// Upper
				minTreasureCoords = new Coordinate(0, 0);
				maxTreasureCoords = new Coordinate(mapMaxCoordinates.getX(), 4);
				
				minEnemyCastleCoords = new Coordinate(0, 5);
				maxEnemyCastleCoords = new Coordinate(mapMaxCoordinates.getX(), mapMaxCoordinates.getY());
			}
		}	
		
		map.setType(mapType);
		map.setCoordinates(mapMaxCoordinates, minTreasureCoords, maxTreasureCoords, minEnemyCastleCoords, maxEnemyCastleCoords);
	}
	
	public static Coordinate getMapMinCoordinates(final Collection<MyFullMapNode> nodes) {
		final int x = nodes
						.stream()	
						.min(Comparator.comparing(MyFullMapNode::getX))	// Comparing all the nodes in the map by their X value and the min.
						.get()											
						.getX();									
			
		final int y = nodes
						.stream()	
						.min(Comparator.comparing(MyFullMapNode::getY))
						.get()											
						.getY();
		
		return new Coordinate(x, y);
	}
	
	public static Coordinate getMapMaxCoordinates(final Collection<MyFullMapNode> nodes) {
		int x = nodes
					.stream()	
					.max(Comparator.comparing(MyFullMapNode::getX))	// Comparing all the nodes in the map by their X value and getting the max.
					.get()											
					.getX();										
			
		int y = nodes
					.stream()	
					.max(Comparator.comparing(MyFullMapNode::getY))	// Same thing here.
					.get()											
					.getY();
		
		return new Coordinate(x, y);
	}
	
}