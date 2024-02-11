package client.converter;

import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import client.map.EMyTerrain;
import client.player.Player;
import client.map.fullmap.EMyFortState;
import client.player.EMyPlayerGameState;
import client.map.fullmap.MyFullMapNode;
import client.movement.direction.EMyMove;
import client.map.halfmap.MyPlayerHalfMap;
import client.map.fullmap.EMyTreasureState;
import client.movement.coordinate.Coordinate;
import client.map.halfmap.MyPlayerHalfMapNode;
import client.map.fullmap.EMyPlayerPositionState;

import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.PlayerState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromserver.EPlayerPositionState;

public class NetworkDataConverterTests {

	@Test
	public void GeneratedMyMap_ConvertingToPlayerHalfMapSpecifiedInProtocol_ValidPlayerHalfMapExpected() {
		// For the sake of easier testing I've created only a small portion of the map.
		final MyPlayerHalfMapNode node1 = new MyPlayerHalfMapNode(new Coordinate(0,0), EMyTerrain.GRASS, true, 4, 9); 
		final MyPlayerHalfMapNode node2 = new MyPlayerHalfMapNode(new Coordinate(0,1), EMyTerrain.MOUNTAIN, false, 4, 9);
		final MyPlayerHalfMapNode node3 = new MyPlayerHalfMapNode(new Coordinate(0,2), EMyTerrain.WATER, false, 4, 9); 
		final MyPlayerHalfMap map = new MyPlayerHalfMap(Set.of(node1, node2, node3));
		
		final PlayerHalfMap networkMap = NetworkDataConverter.convertHalfMap("Random player id", map);
		final PlayerHalfMapNode networkNode1 = networkMap
				.getMapNodes()
				.stream()
				.filter(n -> n.isFortPresent())
				.findFirst()
				.get();
		final PlayerHalfMapNode networkNode2 = networkMap
				.getMapNodes()
				.stream()
				.filter(n -> n.getX() == node2.getX() && n.getY() == node2.getY())
				.findFirst()
				.get();
		final PlayerHalfMapNode networkNode3 = networkMap
				.getMapNodes()
				.stream()
				.filter(n -> n.getX() == node3.getX() && n.getY() == node3.getY())
				.findFirst()
				.get();
		
		// Comparing on grass type + coordinates.
		Assertions.assertEquals(node1.isFortPresent(), networkNode1.isFortPresent());
		Assertions.assertEquals(networkNode1.getTerrain(), ETerrain.Grass);
		Assertions.assertEquals(networkNode1.getX(), node1.getX());
		Assertions.assertEquals(networkNode1.getY(), node1.getY());
		Assertions.assertEquals(networkNode2.getTerrain(), ETerrain.Mountain);
		Assertions.assertEquals(networkNode2.getX(), node2.getX());
		Assertions.assertEquals(networkNode2.getY(), node2.getY());
		Assertions.assertEquals(networkNode3.getTerrain(), ETerrain.Water);	
		Assertions.assertEquals(networkNode3.getX(), node3.getX());
		Assertions.assertEquals(networkNode3.getY(), node3.getY());	
	}
	
	@Test
	public void RequestedGameStateFromServer_UpdatedPlayerInfoInModel_ExpectedValidConversion() {
		final PlayerState networkInfo = new PlayerState("fake", "fake", "007", EPlayerGameState.Won, UniquePlayerIdentifier.of("1234"), true);
		final Player myInfo = new Player();
		myInfo.register("007", "fake", "fake");
		myInfo.setPlayerState(EMyPlayerGameState.MUST_WAIT);
		myInfo.setPlayerId("1234");
			
		NetworkDataConverter.convertPlayerState(myInfo.getGameState(), networkInfo);
		
		// Basically update the player state and set the variable hasTreasure to true.
		Assertions.assertEquals(myInfo.getFirstname(), networkInfo.getFirstName());
		Assertions.assertEquals(myInfo.getLastname(), networkInfo.getLastName());
		Assertions.assertEquals(myInfo.getUAccount(), networkInfo.getUAccount());
		Assertions.assertEquals(myInfo.getPossiblePlayerState().get(), EMyPlayerGameState.WON);
		Assertions.assertEquals(myInfo.getPlayerId(), networkInfo.getUniquePlayerID());
		Assertions.assertEquals(myInfo.hasCollectedTreasure(), networkInfo.hasCollectedTreasure());
		Assertions.assertTrue(myInfo.hasCollectedTreasure());
	}
	
	@Test
	public void RequestedGameStateFromServer_UpdatedMyFullMapInModel_ExpectedValidConversion() {
		final int x = 4;
		final int y = 2;
		final FullMapNode node = new FullMapNode(ETerrain.Grass, EPlayerPositionState.MyPlayerPosition, ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, x,y);
		// For the sake of simplicity only one node is used.
		final FullMap networkMap = new FullMap(Set.of(node)); 
		
		final MyFullMapNode myNode = NetworkDataConverter
												.convertFullMap(networkMap.getMapNodes())
												.stream()
												.filter(n -> n.getX() == x && n.getY() == y)
												.findFirst()
												.get();
		
		Assertions.assertEquals(myNode.getTerrain(), EMyTerrain.GRASS);
		Assertions.assertEquals(myNode.getPlayerPositionState(), EMyPlayerPositionState.MY_PLAYER);
		Assertions.assertEquals(myNode.getTreasureState(), EMyTreasureState.NO_OR_UNKNOWN);
		Assertions.assertEquals(myNode.getFortState(), EMyFortState.NO_OR_UNKNOWN);
		assertThat(myNode.getX(), is(equalTo(x)));
		assertThat(myNode.getY(), is(equalTo(y)));	
	}
	
	@Test
	public void ClientGeneratedMove_PassedDownToTheConverter_ProperlyConvertedToPlayerMove() {
		final String myId = "007";
		final EMyMove myMove = EMyMove.LEFT;
		
		final PlayerMove convertedMove = NetworkDataConverter.convertMovement(myId, myMove);
		
		assertThat(myId, is(equalTo(convertedMove.getUniquePlayerID())));
		assertThat(myMove.toString().toLowerCase(), is(equalTo(convertedMove.getMove().toString().toLowerCase())));
	}

}