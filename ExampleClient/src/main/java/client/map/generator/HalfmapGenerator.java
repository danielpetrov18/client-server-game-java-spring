package client.map.generator;

import java.util.Random;
import java.util.HashSet;
import java.util.Collection;

import client.map.EMyTerrain;
import client.map.halfmap.MyPlayerHalfMap;
import client.movement.coordinate.Coordinate;
import client.map.halfmap.MyPlayerHalfMapNode;

/**
 * <p>No particular algorithm is used just randomizing node placements.
 *    First the whole 5x20 map is initialized with only grass fields.
 *    Then the minimum amount of mountain fields is added by replacing 
 *    		some of the grass fields.
 *    Water fields are also added but only replacing the grass fields and
 *    		being careful not to put too many water fields on the edges.
 *    Lastly a castle is placed on a grass field. 
 * </p>
 */
public class HalfmapGenerator {
    
	private MyPlayerHalfMapNode[][] map;
    private final static int MAX_X = 9;
    private final static int MAX_Y = 4;
    private static final int MIN_WATER = 7;
    private static final int MIN_MOUNTAIN = 5;
    private static final int CASTLE_COUNT = 1;
    private static final int MAX_TOP_BOTTOM_WATER = 4;
    private static final int MAX_LEFT_RIGHT_WATER = 2;
    private static final Random random = new Random();

    public HalfmapGenerator() {
    	this.map = new MyPlayerHalfMapNode[MyPlayerHalfMap.ROWS][MyPlayerHalfMap.COLS];
    }
    
    /**
     * <p>Initializes the map, then randomizes it.</p>
     * @return Returns MyPlayerHalfMap consisting of the nodes.
     */
    public MyPlayerHalfMap generateMap() {
        this.initializeMap();
        this.randomizeMap();
        
        final MyPlayerHalfMap map = new MyPlayerHalfMap(this.createCollection());
        
        return map;
    }
      
    // Helper methods
    
    /**
     * <p>Initializes the map with only grass fields.</p>
     * <p>Have in mind that the X-Coordinate is from 0 to 9 and the Y-Coordinate from 0 to 4.</p>
     */
    private void initializeMap() {
    	for (int row = 0; row < MyPlayerHalfMap.ROWS; row++) {
    		for (int col = 0; col < MyPlayerHalfMap.COLS; col++) {			
    			this.map[row][col] = new MyPlayerHalfMapNode(new Coordinate(col, row), EMyTerrain.GRASS, false, MAX_X, MAX_Y); 
    		}
    	}
    }
    
    private void randomizeMap() {
        this.placeMountains();
        this.placeWater(); 
        this.placeCastle();
    }
    
    private void placeMountains() {
    	int count = 0;
    	// Adding some more mountains than required to make it easier to reveal my treasure or enemy castle.
    	int additionalMountains = random.nextInt(MIN_MOUNTAIN, MIN_MOUNTAIN + MIN_MOUNTAIN + MIN_MOUNTAIN);
    	int total = additionalMountains + MIN_MOUNTAIN;
    	
    	while(count < total) {
    		int row = random.nextInt(MyPlayerHalfMap.ROWS);
    	    int col = random.nextInt(MyPlayerHalfMap.COLS);
    	    
    	    if(this.map[row][col].getTerrain() != EMyTerrain.MOUNTAIN) {
    	    	this.map[row][col] = new MyPlayerHalfMapNode(new Coordinate(col, row), EMyTerrain.MOUNTAIN, false, MAX_X, MAX_Y);
    	        count++;
    	    }
    	}   
    }
    
    /**
     * <p>Generating random water fields.</p>
     * <p>At least 7 water fields must be present, 4 pieces at most on the longer sides and 2 pieces at most on the shorter ones.</p>
     */
    private void placeWater() {
    	int count = 0;
        int topWaterCnt = 0;
        int bottomWaterCnt = 0;
        int leftWaterCnt = 0;
        int rightWaterCnt = 0;
        
        while (count < MIN_WATER) {
            int row = random.nextInt(MyPlayerHalfMap.ROWS);
            int col = random.nextInt(MyPlayerHalfMap.COLS);
        
            // Only replacing the grass fields.
            if(this.map[row][col].getTerrain() == EMyTerrain.GRASS) {

                // If the are more than 4 water fields on the top side don't place anything.
                if(row == 0 && topWaterCnt >= MAX_TOP_BOTTOM_WATER) {
                	continue;                	
                }
                
                // If the are more than 4 water fields on the bottom side don't place anything.
                if(row == 4 && bottomWaterCnt >= MAX_TOP_BOTTOM_WATER) {
                	continue;                	
                }
                
                // If the are more than 2 water fields on the left side don't place anything.
                if(col == 0 && leftWaterCnt >= MAX_LEFT_RIGHT_WATER) {
                	continue;                	
                }
                
                // If the are more than 2 water fields on the right side don't place anything.
                if(col == 9 && rightWaterCnt >= MAX_LEFT_RIGHT_WATER) {
                	continue;                	
                }

                // Otherwise place the water field.
                if(row == 0)  {
                	topWaterCnt++;                	
                }
                if(row == 4) {
                	bottomWaterCnt++;                	
                }
                if(col == 0) {
                	leftWaterCnt++;                	
                }
                if(col == 9) {
                	rightWaterCnt++;                	
                }

                this.map[row][col] = new MyPlayerHalfMapNode(new Coordinate(col, row), EMyTerrain.WATER, false, MAX_X, MAX_Y);
                count++;
            }
        }
    }
    
    private void placeCastle() {
    	int count = 0;
        while (count < CASTLE_COUNT) {
        	// Only placing castle between the first and third row -> trying to place it more centrally on the map.
            int row = random.nextInt(1, MyPlayerHalfMap.ROWS - 1);
            // Only placing castle between third and 8th column.  
            int col = random.nextInt(2, MyPlayerHalfMap.COLS - 2);

            if (this.map[row][col].getTerrain() == EMyTerrain.GRASS) {	
            	this.map[row][col] = new MyPlayerHalfMapNode(new Coordinate(col, row), EMyTerrain.GRASS, true, MAX_X, MAX_Y);
                count++;
            }
        }
    }

    private Collection<MyPlayerHalfMapNode> createCollection() {
    	final Collection<MyPlayerHalfMapNode> resultMap = new HashSet<>();
    	
    	for(int row = 0; row < MyPlayerHalfMap.ROWS; ++row) {
    		for(int col = 0; col < MyPlayerHalfMap.COLS; ++col) {
    			resultMap.add(this.map[row][col]);
    		}
    	}
    	
    	return resultMap;
    }

}