package client.movement.finder;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.movement.EObjective;
import client.map.fullmap.MyFullMap;
import client.map.fullmap.EMyFortState;
import client.map.fullmap.MyFullMapNode;
import client.movement.direction.EMyMove;
import client.map.fullmap.EMyTreasureState;
import client.movement.direction.Direction;
import client.movement.coordinate.Coordinate;
import client.movement.validator.MoveValidator;
import client.map.fullmap.EMyPlayerPositionState;
import client.movement.direction.DirectionFactory;
import client.movement.direction.DirectionOrderer;
import client.movement.coordinate.calculator.CoordinatesCalculator;

/**
 * <p>This class is responsible for the pathfinding.</p>
 * <p>Uses Depth First Search with backtracking.</p>
 */
public class ObjectiveFinder {
	
	private boolean visitedNodes[][];
	private Stack<MyFullMapNode> stack;
	private Direction currentDirection;
	private MyFullMapNode nextPosition;
	private MyFullMapNode currentPosition;
	private final static Logger logger = LoggerFactory.getLogger(ObjectiveFinder.class);
	
	/**
	 * <p>Calculates a starting position and all necessery coordinates used during pathfinding.</p>
	 * <p>Creates a 2d array of boolean to keep track of all visited and unvisited nodes.</p>
	 * @param map The most recent map.
	 */
	public ObjectiveFinder(final MyFullMap map) {	
		this.calculateStartingPosition(map);
		this.currentDirection = null;	
		
		// Calculate map coordinates and type.
		CoordinatesCalculator.calculateHalfMapBoundaries(map, this.currentPosition);
		
		// To keep track of visited nodes. We add plus 1 because the max X might be 19 or 9 and the max Y might be 4 or 9.
		this.visitedNodes = new boolean[map.getMax().getX() + 1][map.getMax().getY() + 1];
		this.stack = new Stack<>();
		this.stack.push(this.currentPosition);
		this.visitedNodes[this.currentPosition.getX()][this.currentPosition.getY()] = true;	
		logger.debug("Visited: {}", this.currentPosition.getPosition());
	}

	/**
	 * @return Returns a EMyMove object letting the controller send the move with the help of the network.
	 */
	public EMyMove getDirection() {
		return this.currentDirection.getDirection();
	}
	
	/**
	 * @return true If the currents direction rounds reached zero. We need to calculate a need move.
	 */
	public boolean isDirectionExhausted() {
		return this.currentDirection.isDirectionExhausted();
	}
	
	/**
	 * @param map The most recent map.
	 * @param objective The thing we are looking for -> my treasure, the enemy castle or trying to cross to the other halfmap.
	 */
	public void findObjective(final MyFullMap map, final EObjective objective) {		
		while(!stack.isEmpty()) {
			// Take a look, but don't pop the current node (where I'm at currently).
			final MyFullMapNode currNode = stack.peek();
			
			// Calculates optimal directions based on what we are looking for.
			int[][] directions = DirectionOrderer.orderDirections(this.currentPosition, map.getType(), objective);
			
			// If the objective has been found on some of the neighboring coordinates just stop execution.
			if(this.checkNeighboringNodesForObjective(map, objective, directions)) return;
			
			// Go in the current direction if possible until you have found something.
			Coordinate newCoordinate = new Coordinate(currNode.getX() + directions[0][0], currNode.getY() + directions[0][1]);
			if(MoveValidator.isValidMove(map, newCoordinate, objective, this.visitedNodes)) {
				final MyFullMapNode newNode = this.getNewNode(map, newCoordinate);
				this.visitNewNode(newNode);
				return;
			}
			
			// Trying to move in all other directions rather than the most preferred one.
			for(int i = 1; i < directions.length; ++i) {
				newCoordinate = new Coordinate(currNode.getX() + directions[i][0], currNode.getY() + directions[i][1]);		
				if(MoveValidator.isValidMove(map, newCoordinate, objective, this.visitedNodes)) {
					final MyFullMapNode newNode = this.getNewNode(map, newCoordinate);
					this.visitNewNode(newNode);
					return;
				}
			}
				
			logger.debug("Reached a dead end. We have to backtrack ...");
			// Path not found -> backtrack (reached a dead end).
			// Removing the top most node -> one step backwards.
			this.currentPosition = this.stack.pop();
			// The previosly visited node.
			this.nextPosition = this.stack.peek();
			this.currentDirection = DirectionFactory.of(this.currentPosition, this.nextPosition);
			this.currentPosition = this.nextPosition;	
			return;
		}
	}
	
	/**
	 * @param map The most recent map.
	 * @param objective Either EObjective.MyTreasure or EObjective.EnemyCastle
	 * @param directions All possible directions in which the AI could make a move.
	 * @return Returns true if there was a found objective in any of the neighboring nodes (horizontally, vertically).
	 */
	public boolean checkNeighboringNodesForObjective(final MyFullMap map, final EObjective objective, int[][] directions) {
		/*
		 * When we are on a given node we check our surroundings for a treasure or enemy castle.
		 * We look in all possible directions (excluding vertical movements) and for each one
		 * we validate the movement (check if it's not water, outside of the map boundaries or
		 * if it's not visited already). If it's valid we check if there's an objective on that node.
		 * If there is we make a movevent towards it.
		 */
		if(!objective.equals(EObjective.CROSSING_SIDES)) {			
			for(int[] dir : directions) {
				final Coordinate newCoordinate = new Coordinate(this.currentPosition.getX() + dir[0], this.currentPosition.getY() + dir[1]);	

				if(MoveValidator.isValidMove(map, newCoordinate, objective, this.visitedNodes)) {
					final MyFullMapNode newNode = this.getNewNode(map, newCoordinate);
					switch(objective) {
						case MY_TREASURE:
							if(newNode.getTreasureState().equals(EMyTreasureState.TREASURE_PRESENT)) {
								this.visitNewNode(newNode);
								return true;
							}
						case ENEMY_CASTLE:
							if(newNode.getFortState().equals(EMyFortState.ENEMY_FORT)) {
								this.visitNewNode(newNode);
								return true;
							}
						default:
							break;
					}				
				}
			}
		}
		return false;
	}
	
	/**
	 * @return True if the AI figure is on the other side of the map.
	 */
	public boolean isOnOtherSide(final MyFullMap map) {
		return MoveValidator.isOnOtherSide(map, this.currentPosition);
	}
		
	/**
	 * <p>Pushes the new node in the stack and marks it as visited.</p>
	 * <p>Calculates the current direction and sets the current position to equal the new one.</p>
	 * @param newNode The new node to be visited.
	 */
	private void visitNewNode(final MyFullMapNode newNode) {
		this.nextPosition = newNode;
		this.stack.push(newNode);							
		this.visitedNodes[newNode.getX()][newNode.getY()] = true;
		logger.debug("Visited: {}", newNode.getPosition());
		this.currentDirection = DirectionFactory.of(this.currentPosition, this.nextPosition);
		this.currentPosition = this.nextPosition;
	}
	
	/**
	 * @param map The most recent map.
	 * @param newCoord The new nodes coordinate.
	 * @return Returns a new FullMapNode based on the new nodes coordinate.
	 */
	private MyFullMapNode getNewNode(final MyFullMap map, final Coordinate newCoord) {
		return  map
					.getMapNodes()
					.stream()
					.filter(n -> n.getX() == newCoord.getX() && n.getY() == newCoord.getY())
					.findFirst()
					.get();
	}	
		
	/**
	 * <p>Finds where my fort is present on the table.</p>
	 * @param map The most recent map.
	 * @throws Throws an exception if no starting position could be found.
	 */
 	private void calculateStartingPosition(final MyFullMap map) {
		this.currentPosition = map
							.getMapNodes()
							.stream()
							.filter(node -> node.getFortState().equals(EMyFortState.MY_FORT) 
								 || node.getPlayerPositionState().equals(EMyPlayerPositionState.MY_PLAYER))
							.findFirst()
							.get();	
	}
	
}