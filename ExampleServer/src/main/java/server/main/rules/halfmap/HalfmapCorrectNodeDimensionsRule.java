package server.main.rules.halfmap;

import java.util.Iterator;

import server.main.rules.IBusinessRule;
import server.exceptions.map.MapInvalidNodeCoordinateValueException;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

public class HalfmapCorrectNodeDimensionsRule implements IBusinessRule {
	
	private static final int MIN_X = 0;
	private static final int MAX_X = 9;
	private static final int MIN_Y = 0;
	private static final int MAX_Y = 4;
	
	private final PlayerHalfMap halfmap;
	
	public HalfmapCorrectNodeDimensionsRule(final PlayerHalfMap halfmap) {
		this.halfmap = halfmap;
	}

	@Override
	public void validate() {
		final Iterator<PlayerHalfMapNode> halfmapIt = this.halfmap.getMapNodes().iterator();
		
		while(halfmapIt.hasNext()) {
			final PlayerHalfMapNode currentNode = halfmapIt.next();
			
			if(currentNode.getX() < MIN_X || currentNode.getX() > MAX_X) {
				final String errMsg = "Nodes' X value was out of bounds: [" + currentNode.getX() + "]!";
				throw new MapInvalidNodeCoordinateValueException("MapInvalidNodeCoordinateValueException", errMsg);
			}
			
			if(currentNode.getY() < MIN_Y || currentNode.getY() > MAX_Y) {
				final String errMsg = "Nodes' Y value was out of bounds: [" + currentNode.getY() + "]!";
				throw new MapInvalidNodeCoordinateValueException("MapInvalidNodeCoordinateValueException", errMsg);
 			}
		}
		
	}
	
}