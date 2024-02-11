package server.main.rules.halfmap;

import java.util.List;
import java.util.stream.Collectors;

import server.main.rules.IBusinessRule;
import server.exceptions.map.MapTooManyWaterFieldsOnEdgeHalfmapException;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

public class HalfmapNotTooManyWaterFieldsOnEdgesRule implements IBusinessRule {

	private final PlayerHalfMap map;
	
	private static final int MIN_X = 0;
	private static final int MAX_X = 9;
	private static final int MIN_Y = 0;
	private static final int MAX_Y = 4;
    private static final int MAX_TOP_BOTTOM_WATER = 4;
    private static final int MAX_LEFT_RIGHT_WATER = 2;
	
    public HalfmapNotTooManyWaterFieldsOnEdgesRule(final PlayerHalfMap map) {
    	this.map = map;
    }

	@Override
	public void validate() {
	    int topWaterCnt = 0, bottomWaterCnt = 0, leftWaterCnt = 0, rightWaterCnt = 0;
	    	
	    final List<PlayerHalfMapNode> waterFields = this.map
	    													.getMapNodes()
	    													.stream()
	    													.filter(node -> node.getTerrain().equals(ETerrain.Water))
	    													.collect(Collectors.toList());
	    	
	    for(int waterIndex = 0; waterIndex < waterFields.size(); ++waterIndex) {
	    	final PlayerHalfMapNode waterNode = waterFields.get(waterIndex);
	    		
	    	if(waterNode.getX() == MIN_X) {
	    		++leftWaterCnt;	    		
	    	}
	    	else if(waterNode.getX() == MAX_X) {
	    		++rightWaterCnt;	    		
	    	}
	    		
	    	if(waterNode.getY() == MIN_Y) {
	    		++topWaterCnt;	    		
	    	}
	    	else if(waterNode.getY() == MAX_Y) {
	    		++bottomWaterCnt;	    		
	    	}
	    }
	    	
	    if(leftWaterCnt >= MAX_LEFT_RIGHT_WATER || rightWaterCnt >= MAX_LEFT_RIGHT_WATER || topWaterCnt >= MAX_TOP_BOTTOM_WATER || bottomWaterCnt >= MAX_TOP_BOTTOM_WATER) {
	    	final String errMsg = "Too many water fields on edges. Top: " + topWaterCnt + ", bottom: " + bottomWaterCnt + 
	    			", left: " + leftWaterCnt + ", right: " + rightWaterCnt + "!";
	    	throw new MapTooManyWaterFieldsOnEdgeHalfmapException("MapTooManyWaterFieldsOnEdgeHalfmapException", errMsg);
	    }
				
	}
    	
}