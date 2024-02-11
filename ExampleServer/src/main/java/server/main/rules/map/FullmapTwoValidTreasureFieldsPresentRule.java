package server.main.rules.map;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import server.main.map.EMyTerrain;
import server.main.rules.IBusinessRule;
import server.main.map.fullmap.EMyFortState;
import server.main.map.fullmap.MyFullMapNode;
import server.main.map.fullmap.EMyTreasureState;
import server.exceptions.map.MapTreasureOnCastleNodeException;
import server.exceptions.map.MapTreasureNotOnGrassNodeException;
import server.exceptions.map.MapInvalidFullmapTreasureCountException;

public class FullmapTwoValidTreasureFieldsPresentRule implements IBusinessRule {

	private final Set<MyFullMapNode> map;
	private final static int VALID_TREASURE_COUNT = 2;
	private final static EMyTerrain VALID_TERRAIN = EMyTerrain.Grass;
	
	public FullmapTwoValidTreasureFieldsPresentRule(final Set<MyFullMapNode> map) {
		this.map = map;
	}
	
	/*
	 * Checks if there's no castle and if the field type is grass. 

	 * Collects the player id of each treasure node to check if there are 2 different treasure nodes
	 * that belong to a different player.
	 */
	@Override
	public void validate() {
		final Set<String> playerIds = new HashSet<>();
		final Iterator<MyFullMapNode> treasuresIt = this.map
															.stream()
															.filter(node -> node.getTreasureState().equals(EMyTreasureState.MyTreasureIsPresent))
															.iterator();
		while(treasuresIt.hasNext()) {
			final MyFullMapNode treasureNode = treasuresIt.next();
			playerIds.add(treasureNode.getPlayerId());
			
			if(treasureNode.compareFortState(EMyFortState.MyFortPresent)) {
				final String errMsg = "Treasure on same field as castle: [" + treasureNode + "]!";
				throw new MapTreasureOnCastleNodeException("MapTreasureOnCastleNodeException", errMsg);
			}

			if(!treasureNode.compareTerrain(VALID_TERRAIN)) {
				final String errMsg = "Treasure not placed on grass field: [" + treasureNode + "]!";
				throw new MapTreasureNotOnGrassNodeException("MapTreasureNotOnGrassNodeException", errMsg);
			}
		}
		
		int treasureCount = playerIds.size();
		if(treasureCount != VALID_TREASURE_COUNT) {
			final String errMsg = "Fullmap must have 2 treasure nodes. Your has: [" + treasureCount + "]!";
			throw new MapInvalidFullmapTreasureCountException("MapInvalidFullmapTreasureCountException", errMsg);
		}
	}
	
}