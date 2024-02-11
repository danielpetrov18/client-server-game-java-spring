package server.main.map.fullmap;

import java.util.Set;
import java.util.HashSet;

import server.main.rules.map.FullmapTwoValidTreasureFieldsPresentRule;

public class MyFullMap {

	private final EFullMapType shape;					// Either a square or rectangle.
	private final Set<String> playerIDs;				// Player id's of players sent their half map. Used when checking who has sent a half map.
	private EFullMapPlacement halfmapPos;				// The position of the to be added half map.
	private final Set<MyFullMapNode> nodes;		
	private final static int FULLMAP_NODE_COUNT = 100;
		
	public MyFullMap() {
		this.shape = MyFullMapHelper.pickRandomShape();
		this.playerIDs = new HashSet<>();
		this.halfmapPos = EFullMapPlacement.Undefined;
		this.nodes = new HashSet<>();
	}
	
	/*
	 * The method adds the player id into a set so that we keep track of which player has already sent a half map.
	 * 
	 * Based on the randomly generated map shape in the constructor we pick and choose a side to put each half map.
	 * For example: If the map is rectangular one of the half maps would go on the left side and the second one will be 
	 * automatically placed on the right side. The map that is being placed on the right side will get its node coordinates increased accordingly. 
	 */
	public void addHalfmap(final String playerId, final Set<MyFullMapNode> newNodes) {
		this.playerIDs.add(playerId);
		
		if(this.halfmapPos.equals(EFullMapPlacement.Undefined)) {
			this.halfmapPos = MyFullMapHelper.pickCardPlacement(this.shape);
		}
		else {
			this.halfmapPos = MyFullMapHelper.getOppositeCardPlacement(this.halfmapPos);
		}
		
		MyFullMapHelper.placeHalfmap(this.halfmapPos, this.nodes, newNodes, playerId);
		
		if(this.nodes.size() == FULLMAP_NODE_COUNT) {
			final FullmapTwoValidTreasureFieldsPresentRule treasuresRule = new FullmapTwoValidTreasureFieldsPresentRule(this.nodes);
			treasuresRule.validate();
		}
	}
	
	public boolean checkSentHalfmap(final String playerId) {
		return this.playerIDs.contains(playerId);
	}
	
	public Set<MyFullMapNode> getMapNodes() {
		return this.nodes;
	}
	
	public boolean isMapEmpty() {
		return this.nodes.isEmpty();
	}
	
}