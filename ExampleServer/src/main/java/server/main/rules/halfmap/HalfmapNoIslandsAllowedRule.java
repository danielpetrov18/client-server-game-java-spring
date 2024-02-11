package server.main.rules.halfmap;

import messagesbase.messagesfromclient.PlayerHalfMap;

import server.main.rules.IBusinessRule;
import server.main.map.validator.FloodFillAlgorithm;
import server.exceptions.map.MapInaccessibleNodeHalfmapException;

public class HalfmapNoIslandsAllowedRule implements IBusinessRule {

	final PlayerHalfMap map;
	
	public HalfmapNoIslandsAllowedRule(final PlayerHalfMap map) {
		this.map = map;
	}
	
	@Override
	public void validate() {
		if(!FloodFillAlgorithm.floodFill(this.map)) {
			throw new MapInaccessibleNodeHalfmapException("MapInaccessibleNodeHalfmapException", "Inaccessible node was found during validation!");
		}
	}
	
}