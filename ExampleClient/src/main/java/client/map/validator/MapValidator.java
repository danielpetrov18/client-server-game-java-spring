package client.map.validator;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.EMyTerrain;
import client.map.halfmap.MyPlayerHalfMap;
import client.map.halfmap.MyPlayerHalfMapNode;
import client.exception.checked.InvalidMyPlayerHalfMapGenerated;

/**
 * <p>A class that checks whether the randomly generated map fulfills the requirements of the game.</p>
 */
public class MapValidator {
	/*
 	  *	TAKEN FROM <1> 
	  *	In the provided link there is an explanation about flood fill algorithm. 
	  *	I've used the pseudo code for defining my own flood fill logic to check if all the nodes on the map are accessible.
	  *	https://www.baeldung.com/cs/flood-fill-algorithm
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(MapValidator.class);
	 
	//TAKEN FROM START <1>
    /**
     * <p>Starts from a given point and tries to visit all fields if possible.</p>
     * @param map The map to be validated.
     * @return Returns true or false based on if a island was found -> inaccessible node excluding water fields.</p>
     * @throws InvalidMyPlayerHalfMapGenerated 
     */
    public void floodFill(final MyPlayerHalfMap map) throws InvalidMyPlayerHalfMapGenerated {
    	logger.trace("Started flood fill algorithm to check for present islands ...");
    	
    	final MyPlayerHalfMapNode startNode = this.selectStartingNode(map);
    	boolean[][] visited = new boolean[MyPlayerHalfMap.ROWS][MyPlayerHalfMap.COLS];
    	
        final LinkedList<MyPlayerHalfMapNode> queue = new LinkedList<>();
        queue.add(startNode);
        
        // Traversing the map using a Queue structure to check if all the nodes are accessible. No islands should be present.
        while (!queue.isEmpty()) {
        	// Getting the current node and marking it as visited.
            final MyPlayerHalfMapNode current = queue.poll();
            int x = current.getX();
            int y = current.getY();
            visited[y][x] = true;
            logger.trace("Visited: X={}, Y={}", x, y);

            int[][] neighbors = {
            //  leftwards	rightwards	 upwards     downwards
                {x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}
            };

            // Visiting all the neighbors.
            for (int[] neighbor : neighbors) {
                int nx = neighbor[0];
                int ny = neighbor[1];

                // Check if the new X and Y coordinates are inside the bounds of the map and also see if the given node is unvisited.
                if (nx >= 0 && nx < MyPlayerHalfMap.COLS && ny >= 0 && ny < MyPlayerHalfMap.ROWS && !visited[ny][nx]) {
                    final MyPlayerHalfMapNode neighborNode = map
                    									.getMapNodes()
                    									.stream()
                    									.filter(field -> field.getX() == nx && field.getY() == ny)
                    									.findFirst()
                    									.get();

                    // Check if the neighbor node is not water.
                    if (!neighborNode.getTerrain().equals(EMyTerrain.WATER)) {
                        queue.add(neighborNode);
                    }
                }
            }
        }

        // Finally checking if all the nodes with exception of water nodes were visited.
        final boolean isValid = map
				.getMapNodes()
				.stream()
				.filter(node -> !node.getTerrain().equals(EMyTerrain.WATER))	// Check all non-water type fields.
				.allMatch(node -> {											    // Check if they are all visited.
					final boolean wasVisited = visited[node.getY()][node.getX()];
					if(!wasVisited)
						logger.warn("Unvisited node found: {}", node);
					return wasVisited;
				});
        
        // If not -> exception is thrown.
        if(!isValid) {
        	logger.warn("The provided map was invalid. A new one has to be generated and passed for validation!");
        	throw new InvalidMyPlayerHalfMapGenerated("Invalid MyHalfMap generated!");
        }
    }
    // TAKEN FROM END <1>
    
	private MyPlayerHalfMapNode selectStartingNode(final MyPlayerHalfMap map) {	
		// Traversing the map for a node either grass or mountain. If no such node has been found an exception gets thrown.
		return   map
					.getMapNodes()
					.stream()
					.filter(node -> !node.getTerrain().equals(EMyTerrain.WATER))	// Filter out all water type fields.
					.findFirst()
					.get();
	}
    
}