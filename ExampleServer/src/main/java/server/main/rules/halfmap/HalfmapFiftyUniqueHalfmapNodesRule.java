package server.main.rules.halfmap;

import java.util.Set;
import java.util.HashSet;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

import server.main.rules.IBusinessRule;
import server.exceptions.map.MapExpectedFiftyUniqueHalfMapNodesException;

public class HalfmapFiftyUniqueHalfmapNodesRule implements IBusinessRule {
	
	private final PlayerHalfMap map;

	private final static int HALFMAP_VALID_UNIQUE_NODES_COUNT = 50;
	
	public HalfmapFiftyUniqueHalfmapNodesRule(final PlayerHalfMap map) {
		this.map = map;
	}
	
	@Override
	public void validate() {
		final Set<PlayerHalfMapNode> collectedNodes = new HashSet<>(this.map.getMapNodes());
		
		if(collectedNodes.size() != HALFMAP_VALID_UNIQUE_NODES_COUNT) {
			final String errMsg = "Expected 50 unique nodes! Your halfmap had: [" + collectedNodes.size() + "]!"; 
			throw new MapExpectedFiftyUniqueHalfMapNodesException("MapExpectedFiftyUniqueHalfMapNodesException", errMsg);
		}
	}

}