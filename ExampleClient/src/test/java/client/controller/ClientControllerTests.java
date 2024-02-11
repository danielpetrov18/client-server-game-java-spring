package client.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Set;

import java.beans.PropertyChangeSupport;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import client.player.Player;
import client.player.EPlayer;
import client.map.EMyTerrain;
import client.model.ClientData;
import client.network.INetwork;
import client.map.fullmap.EMyFortState;
import client.player.EMyPlayerGameState;
import client.map.fullmap.MyFullMapNode;
import client.map.fullmap.EMyTreasureState;
import client.movement.coordinate.Coordinate;
import client.map.fullmap.EMyPlayerPositionState;
import client.exception.unchecked.GameStateException;
import client.exception.unchecked.PlayerHalfMapException;
import client.exception.unchecked.PlayerRegistrationException;

import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.PlayerState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;

public class ClientControllerTests {

	// Tests which implement mocking

	@Mock
	private INetwork mockConn = Mockito.mock(INetwork.class);
	
	@Test
	public void GameStarted_ControllerSendsRegistration_ExpectedCorrectString() throws PlayerRegistrationException {
		// Arrange
		final ClientData model = new ClientData(new PropertyChangeSupport(ClientData.class));
		
		// Passing the mocked connection class to test the controller.
		final ClientController controller = new ClientController(model, this.mockConn);
		
		final String expectedUniquePId = "007";
		final String expectedFirstname = "Daniel";
		final String expectedLastname = "Petrov";
		final String expectedUAccount = "danielp29";
		
		final String expectedResponse = expectedUniquePId;
		
		// Overwrite the functionality of the network class.
		// No matter what PlayerRegistration object is passed the specified UniquePlayerIdentifier String is always returned.
		when(this.mockConn.trySendRegistration(any())).thenReturn(expectedResponse);
		
		// Act
		controller.trySendRegistration();
		
		// Assert
		final Player p = model.getPlayer(EPlayer.ME);		
		assertThat(p.getPlayerId(), is(equalTo(expectedUniquePId)));
		assertThat(p.getFirstname(), is(equalTo(expectedFirstname)));
		assertThat(p.getLastname(), is(equalTo(expectedLastname)));
		assertThat(p.getUAccount(), is(equalTo(expectedUAccount)));
		assertThat(p.isRegistered(), is(equalTo(true)));	
	}
	
	@Test
	public void GeneratedHalfMap_ControllerSendsHalfMapToServer_ExpectedNoExceptionThrown() throws PlayerHalfMapException {
		final ClientData model = new ClientData(new PropertyChangeSupport(ClientData.class));
		
		// Register prior to sending the half map is mandatory because we need the player id and we fake a player id (007).
		model.registerPlayer("danielp29", "Daniel", "Petrov", "007", EPlayer.ME);
		final ClientController controller = new ClientController(model, this.mockConn);
		
		controller.trySendHalfMap();
		
		verify(this.mockConn, times(1)).trySendHalfMap(any());
	}

	@Test
	public void RequestedGameState_ReceivedGameState_ExpectedCorrectUpdatedPlayerAndMapInfoInModel() throws GameStateException {
		// Arrange
		final ClientData model = new ClientData(new PropertyChangeSupport(ClientData.class));
		model.registerPlayer("danielp29", "Daniel", "Petrov", "007", EPlayer.ME);
		final ClientController controller = new ClientController(model, this.mockConn);
		
		// Contructing an expected GameState to receive after requesting.
		final String gameStateId = "Valid game state id";
		
		// Make 2 nodes for the map.
		final FullMapNode node1 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.MyPlayerPosition, ETreasureState.NoOrUnknownTreasureState, 
				EFortState.NoOrUnknownFortState, 0, 0);
		final FullMapNode node2 = new FullMapNode(ETerrain.Grass, EPlayerPositionState.EnemyPlayerPosition, ETreasureState.NoOrUnknownTreasureState, 
				EFortState.NoOrUnknownFortState, 0, 1);
		final FullMap dummyMap = new FullMap(Set.of(node1, node2));
		
		// Create 2 players.
		final PlayerState me = new PlayerState("Daniel", "Petrov", "danielp29", EPlayerGameState.MustWait, UniquePlayerIdentifier.of("007"), false);
		final PlayerState enemy = new PlayerState("Enemy", "Enemov", "danielp29", EPlayerGameState.MustAct, UniquePlayerIdentifier.of("420"), false);
		
		final GameState expectedResponse = new GameState(dummyMap, Set.of(me, enemy), gameStateId);
		
		// Overwrite the methods return value when the given Unique Player Id is passed as a parameter.
		when(this.mockConn.tryRequestGameState("007")).thenReturn(expectedResponse);
		
		// Act
		controller.tryRequestGameState();
		
		// Assert
		assertThat(model.getPossiblePlayerState(EPlayer.ME).get(), is(equalTo(EMyPlayerGameState.MUST_WAIT)));
		assertThat(model.getPossiblePlayerState(EPlayer.ENEMY).get(), is(equalTo(EMyPlayerGameState.MUST_ACT)));
		
		final MyFullMapNode resNode1 = model
				.getMap()
				.getMapNodes()
				.stream()
				.filter(n -> n.getPlayerPositionState().equals(EMyPlayerPositionState.MY_PLAYER))
				.findFirst()
				.get();
		
		assertThat(resNode1.getTerrain(), is(equalTo(EMyTerrain.GRASS)));
		assertThat(resNode1.getPlayerPositionState(), is(equalTo(EMyPlayerPositionState.MY_PLAYER)));
		assertThat(resNode1.getTreasureState(), is(equalTo(EMyTreasureState.NO_OR_UNKNOWN)));
		assertThat(resNode1.getFortState(), is(equalTo(EMyFortState.NO_OR_UNKNOWN)));
		assertThat(resNode1.getPosition(), is(equalTo(new Coordinate(0,0))));
		
		final MyFullMapNode resNode2 = model
				.getMap()
				.getMapNodes()
				.stream()
				.filter(n -> n.getPlayerPositionState().equals(EMyPlayerPositionState.ENEMY_PLAYER))
				.findFirst()
				.get();

		assertThat(resNode2.getTerrain(), is(equalTo(EMyTerrain.GRASS)));
		assertThat(resNode2.getPlayerPositionState(), is(equalTo(EMyPlayerPositionState.ENEMY_PLAYER)));
		assertThat(resNode2.getTreasureState(), is(equalTo(EMyTreasureState.NO_OR_UNKNOWN)));
		assertThat(resNode2.getFortState(), is(equalTo(EMyFortState.NO_OR_UNKNOWN)));
		assertThat(resNode2.getPosition(), is(equalTo(new Coordinate(0,1))));
	}
	
}