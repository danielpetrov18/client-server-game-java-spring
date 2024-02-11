package client.movement.dfs;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import client.map.EMyTerrain;
import client.movement.EObjective;
import client.map.fullmap.MyFullMap;
import client.map.fullmap.EMyFortState;
import client.map.fullmap.MyFullMapNode;
import client.movement.direction.EMyMove;
import client.map.fullmap.EMyTreasureState;
import client.movement.coordinate.Coordinate;
import client.movement.finder.ObjectiveFinder;
import client.map.fullmap.EMyPlayerPositionState;

public class ObjectiveFinderTests {
	
	@Test
	public void CurrentlyAtGrassField_CalculatingNextMove_ExpectedLeftDirectionToBeCalculated() {	
		// A map consiting of 2 nodes where the only possibility is leftwards move.
		final MyFullMapNode node1 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.MY_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, 
				EMyFortState.NO_OR_UNKNOWN, new Coordinate(3,5));
		final MyFullMapNode node2 = new MyFullMapNode(EMyTerrain.MOUNTAIN, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, 
				EMyFortState.NO_OR_UNKNOWN, new Coordinate(2,5));
		final MyFullMap map = new MyFullMap(Set.of(node1, node2));
		final ObjectiveFinder finder = new ObjectiveFinder(map);
		
		finder.findObjective(map, EObjective.MY_TREASURE);
		
		final EMyMove expectedDirection = EMyMove.LEFT;
		final EMyMove actualDirection = finder.getDirection();
		
		// Direction object holds both direction + rounds left to leave the current node and enter a new one.
		final boolean actualExhausted = finder.isDirectionExhausted();
		
		Assertions.assertEquals(actualDirection, expectedDirection);
		Assertions.assertFalse(actualExhausted);
	}
	
	@Test
	public void WasOnGrassFieldThenMovedToMountainField_CannotMakeOtherMovesTopBottomWaterRightOutOfMap_ExpectedToBackTrackWithoutErrorsThrown() {
		// What this test checks is basically if the AI can make a movement, reach a dead end and then backtrack to the previous node.
		// If it does no exception should be thrown.
		MyFullMapNode node1 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.MY_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, 
				EMyFortState.NO_OR_UNKNOWN, new Coordinate(1,1));
		MyFullMapNode node2 = new MyFullMapNode(EMyTerrain.MOUNTAIN, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, 
				EMyFortState.NO_OR_UNKNOWN, new Coordinate(0,1));
		MyFullMapNode node3 = new MyFullMapNode(EMyTerrain.WATER, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, 
				EMyFortState.NO_OR_UNKNOWN, new Coordinate(0,0));
		MyFullMapNode node4 = new MyFullMapNode(EMyTerrain.WATER, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, 
				EMyFortState.NO_OR_UNKNOWN, new Coordinate(0,2));
		MyFullMapNode node5 = new MyFullMapNode(EMyTerrain.WATER, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, 
				EMyFortState.NO_OR_UNKNOWN, new Coordinate(1,0));
		MyFullMapNode node6 = new MyFullMapNode(EMyTerrain.WATER, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, 
				EMyFortState.NO_OR_UNKNOWN, new Coordinate(2,1));
		MyFullMapNode node7 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, 
				EMyFortState.NO_OR_UNKNOWN, new Coordinate(1,2));	
		final MyFullMap map = new MyFullMap(Set.of(node1, node2, node3, node4, node5, node6, node7));
		final ObjectiveFinder finder = new ObjectiveFinder(map);
			
		assertDoesNotThrow(() -> {
			// Make a move rightwards towards a mountain field
			finder.findObjective(map, EObjective.MY_TREASURE);
			
			// Go back.
			finder.findObjective(map, EObjective.MY_TREASURE);
	    });
	}

}