package server.main.map.validator;

import java.util.LinkedList;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

public class FloodFillAlgorithm {
	/*
 	  *	TAKEN FROM <1> 
	  *	In the provided link there is an explanation about flood fill algorithm. 
	  *	I've used the pseudo code for defining my own flood fill logic to check if all the nodes on the map are accessible.
	  *	https://www.baeldung.com/cs/flood-fill-algorithm
	 */
	
	private static final int ROWS = 5;
	private static final int COLS = 10;
	 
	/*
	 * <1>TAKEN FROM START <1> 
	 * 
	 * Starts from a given point and tries to visit all fields if possible.
	 */
    public static boolean floodFill(final PlayerHalfMap map) {
    	final PlayerHalfMapNode startNode = selectStartingNode(map);
    	boolean[][] visited = new boolean[ROWS][COLS];
    	
        final LinkedList<PlayerHalfMapNode> queue = new LinkedList<>();
        queue.add(startNode);
        
        // Traversing the map using a Queue structure to check if all the nodes are accessible. No islands should be present.
        while (!queue.isEmpty()) {
        	// Getting the current node and marking it as visited.
            final PlayerHalfMapNode current = queue.poll();
            int x = current.getX();
            int y = current.getY();
            visited[y][x] = true;

            int[][] neighbors = {
            //  leftwards	rightwards	 upwards     downwards
                {x - 1, y}, {x + 1, y}, {x, y - 1}, {x, y + 1}
            };

            // Visiting all the neighbors.
            for (int[] neighbor : neighbors) {
                int nx = neighbor[0];
                int ny = neighbor[1];

                // Check if the new X and Y coordinates are inside the bounds of the map and also see if the given node is unvisited.
                if (nx >= 0 && nx < COLS && ny >= 0 && ny < ROWS && !visited[ny][nx]) {
                    final PlayerHalfMapNode neighborNode = map
                    									.getMapNodes()
                    									.stream()
                    									.filter(field -> field.getX() == nx && field.getY() == ny)
                    									.findFirst()
                    									.get();

                    // Check if the neighbor node is not water.
                    if (!neighborNode.getTerrain().equals(ETerrain.Water)) {
                        queue.add(neighborNode);
                    }
                }
            }
        }

        final boolean isValid = map
				.getMapNodes()
				.stream()
				.filter(node -> !node.getTerrain().equals(ETerrain.Water))		// Check all non-water type fields.
				.allMatch(node -> {											    // Check if they are all visited.
					return visited[node.getY()][node.getX()];
				});
        
        return isValid;
    }
    // TAKEN FROM END <1>
    
    
    // Filters out all water fields and then selects a random node as a starting point.
	private static PlayerHalfMapNode selectStartingNode(final PlayerHalfMap map) {	
		return   map
					.getMapNodes()
					.stream()
					.filter(node -> !node.getTerrain().equals(ETerrain.Water))
					.findFirst()
					.get();
	}
    
}