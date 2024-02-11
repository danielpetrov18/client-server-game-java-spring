package server.main.map.validator;

import messagesbase.messagesfromclient.PlayerHalfMap;

import server.main.rules.halfmap.HalfmapNoIslandsAllowedRule;
import server.main.rules.halfmap.HalfmapCorrectNodeDimensionsRule;
import server.main.rules.halfmap.HalfmapEnoughFieldsOfEachTypeRule;
import server.main.rules.halfmap.HalfmapFiftyUniqueHalfmapNodesRule;
import server.main.rules.halfmap.HalfmapOneCastlePlacedOnGrassFieldRule;
import server.main.rules.halfmap.HalfmapNotTooManyWaterFieldsOnEdgesRule;

public class HalfmapValidator {

    /*
     * First this method checks if there are 50 nodes with unique coordinates.
     * 
     * Second it checks if the coordinates of all these nodes have valid ranges.
     * 
     * Third it checks if there are enough water, grass and mountain fields.
     * 
     * Fourth it checks for exactly one castle and whether it's placed on grass field or not.
     * 
     * Fifth it checks if all the nodes are accessible using flood fill algorithm.
     * 
     * Sixth it checks if there are too many water fields on the map edges.
     */
    public static void validateHalfmap(final PlayerHalfMap halfmap) {
    	final HalfmapFiftyUniqueHalfmapNodesRule uniqueNodeCountRule = new HalfmapFiftyUniqueHalfmapNodesRule(halfmap);
    	uniqueNodeCountRule.validate();
    	
    	final HalfmapCorrectNodeDimensionsRule correctNodeDimensionsRule = new HalfmapCorrectNodeDimensionsRule(halfmap);
    	correctNodeDimensionsRule.validate();
    	
    	final HalfmapEnoughFieldsOfEachTypeRule enoughNodeTypeCountRule = new HalfmapEnoughFieldsOfEachTypeRule(halfmap);
    	enoughNodeTypeCountRule.validate();
    	
    	final HalfmapOneCastlePlacedOnGrassFieldRule castleGrassRule = new HalfmapOneCastlePlacedOnGrassFieldRule(halfmap);
    	castleGrassRule.validate();
    	
    	final HalfmapNotTooManyWaterFieldsOnEdgesRule waterEdgeRule = new HalfmapNotTooManyWaterFieldsOnEdgesRule(halfmap);
    	waterEdgeRule.validate();

    	final HalfmapNoIslandsAllowedRule islandRule = new HalfmapNoIslandsAllowedRule(halfmap);
    	islandRule.validate();
    }
           
}