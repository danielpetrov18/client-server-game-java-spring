package server.main.rules.halfmap;

import java.util.Iterator;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

import server.main.rules.IBusinessRule;
import server.exceptions.map.MapExpectedMinimumFieldCountFromGivenTypeException;

public class HalfmapEnoughFieldsOfEachTypeRule implements IBusinessRule {
	
    private final PlayerHalfMap map;
    
    private static final int MIN_WATER = 7;
    private static final int MIN_GRASS = 24;
    private static final int MIN_MOUNTAIN = 5;

    public HalfmapEnoughFieldsOfEachTypeRule(final PlayerHalfMap map) {
        this.map = map;
    }

    @Override
    public void validate() {
        int currWater = 0;
        int currGrass = 0;
        int currMountain = 0;

        final Iterator<PlayerHalfMapNode> iterator = this.map.getMapNodes().iterator();
        while (iterator.hasNext()) {
            final ETerrain currentNodeTerrain = iterator.next().getTerrain();

            switch (currentNodeTerrain) {
                case Grass:
                    currGrass++;
                    break;
                case Water:
                    currWater++;
                    break;
                case Mountain:
                    currMountain++;
                    break;
            }
        }
        
        if (currWater < MIN_WATER || currGrass < MIN_GRASS || currMountain < MIN_MOUNTAIN) {
        	final String errMsg = "Not enough fields of all types. Water fields: " + currWater + ", grass fields: " + currGrass + ", mountain fields: " + currMountain;
            throw new MapExpectedMinimumFieldCountFromGivenTypeException("MapExpectedMinimumFieldCountFromGivenTypeException", errMsg);
        } 
    }
    
}