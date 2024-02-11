package client.map.fullmap;

import java.util.Set;

import client.map.EMapType;
import client.movement.coordinate.Coordinate;

import java.util.HashSet;
import java.util.Collection;

public class MyFullMap {

	private EMapType mapType;
	private final Set<MyFullMapNode> nodes;
	public final static int FULL_MAP_COUNT_NODE = 100;
	// Coordinates that are relevant for the path finding algorithm.
	private Coordinate mapMaxCoordinates = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
	private Coordinate minTreasureCoords = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
	private Coordinate maxTreasureCoords = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
	private Coordinate minEnemyCastleCoords = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
	private Coordinate maxEnemyCastleCoords = new Coordinate(Coordinate.INVALID_COORDINATE, Coordinate.INVALID_COORDINATE);
	
	public MyFullMap() {
		this.nodes = new HashSet<>();
	}

	public MyFullMap(final Set<MyFullMapNode> nodes) {
		this.nodes = nodes;
	}
	
	public Set<MyFullMapNode> getMapNodes() {
		return this.nodes;
	}
	
	public void setMapNodes(final Collection<MyFullMapNode> newNodes) {
		this.nodes.clear();
		this.nodes.addAll(newNodes);
	}
	
	public boolean isFullMapPresent() {
		return this.nodes.size() == FULL_MAP_COUNT_NODE;
	}
	
	public boolean isMapEmpty() {
		return this.nodes.isEmpty();
	}
	
	public EMapType getType() {
		return this.mapType;
	} 
	
	public void setType(final EMapType type) {
		this.mapType = type;
	}

	public Coordinate getMax() {
		return this.mapMaxCoordinates;
	}
	
	public Coordinate getMinTreasure() {
		return this.minTreasureCoords;
	}
	
	public Coordinate getMaxTreasure() {
		return this.maxTreasureCoords;
	}
		
	public Coordinate getMinEnemyCastle() {
		return this.minEnemyCastleCoords;
	}
	
	public Coordinate getMaxEnemyCastle() {
		return this.maxEnemyCastleCoords;
	}
	
	public void setCoordinates(final Coordinate max, final Coordinate minT, final Coordinate maxT, final Coordinate minC, final Coordinate maxC) {
		this.mapMaxCoordinates = max;
		this.minTreasureCoords = minT;
		this.maxTreasureCoords = maxT;
		this.minEnemyCastleCoords = minC;
		this.maxEnemyCastleCoords = maxC;
	}
	
}