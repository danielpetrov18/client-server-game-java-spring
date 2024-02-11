package server.main.rules.halfmap;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;

import server.main.rules.IBusinessRule;
import server.exceptions.map.MapCastleOnlyOnGrassFieldException;
import server.exceptions.map.MapExactlyOneCastleOnHalfmapAllowedException;

public class HalfmapOneCastlePlacedOnGrassFieldRule implements IBusinessRule {
	
	private final PlayerHalfMap map;
	
	private static final int VALID_CASTLE_COUNT = 1;
	private static final ETerrain VALID_TERRAIN = ETerrain.Grass;
	
	public HalfmapOneCastlePlacedOnGrassFieldRule(final PlayerHalfMap map) {
		this.map = map;
	}
	
	@Override
	public void validate() {
		int castleCount = this.map
								.getMapNodes()
								.stream()
								.filter(node -> node.isFortPresent())
								.toList()
								.size();

		if(castleCount != VALID_CASTLE_COUNT) {
			final String errMsg =  "Each halfmap can only have 1 castle. Your map had: [" + castleCount + "]!";
			throw new MapExactlyOneCastleOnHalfmapAllowedException("MapExactlyOneCastleOnHalfmapAllowedException", errMsg);
		}
		
		final ETerrain castleNodeTerrain = this.map
												.getMapNodes()
												.stream()
												.filter(node -> node.isFortPresent())
												.findFirst()
												.get()
												.getTerrain();
		
		if(!castleNodeTerrain.equals(VALID_TERRAIN)) {
			final String errMsg =  "A castle needs to be placed on a grass field. Yours was placed on: [" + castleNodeTerrain + "]!";
			throw new MapCastleOnlyOnGrassFieldException("MapCastleOnlyOnGrassFieldException", errMsg);
		}
		
	}
	
}