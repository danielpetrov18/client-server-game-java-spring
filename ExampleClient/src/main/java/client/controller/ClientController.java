package client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.player.EPlayer;
import client.network.INetwork;
import client.model.ClientData;
import client.player.EMyPlayerGameState;
import client.map.validator.MapValidator;
import client.movement.direction.EMyMove;
import client.map.halfmap.MyPlayerHalfMap;
import client.controller.engine.MoveEngine;
import client.converter.NetworkDataConverter;
import client.map.generator.HalfmapGenerator;
import client.exception.unchecked.GameStateException;
import client.exception.unchecked.PlayerMoveException;
import client.exception.unchecked.PlayerHalfMapException;
import client.exception.unchecked.PlayerRegistrationException;
import client.exception.checked.InvalidMyPlayerHalfMapGenerated;
import client.controller.extractor.GameStateRequestDataExtractor;

import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;

/**
 * <p>This class implements the business logic and makes sure everything gets coordinated.</p>
 */
public class ClientController {
	
	private ClientData data;
	private INetwork connection;
	private MoveEngine moveEngine;
	private final static String LASTNAME = "Petrov";      // Hardcoded data for registration.
	private final static String FIRSTNAME = "Daniel";     // Hardcoded data for registration.
	private final static String UACCOUNT = "danielp29";   // Hardcoded data for registration.
	private final static int POLLING_TIMESPAN_WAIT = 600; // Those are milliseconds. Should be at least 400 according to the requirements.
	private final static EMyPlayerGameState WAIT = EMyPlayerGameState.MUST_WAIT; // If this condition is not met the server is polled again.
	private final static Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	public ClientController(final ClientData data, final INetwork conn) {		
		this.data = data;
		this.connection = conn;
		this.moveEngine = new MoveEngine();		
	}
	
	/**
	 * <p>A player is being registered with the following data -> (Daniel Petrov danielp29).</p>
	 * <p>It then receives a Response Envelope containing a UniquePlayerIdentifier object and the network returns a String.</p>
	 * @throws PlayerRegistrationException If something goes wrong during the transfer an exception is going to be thrown. Or if the u:account is invalid.
	 */
	public void trySendRegistration() throws PlayerRegistrationException {
		final String playerId = this.connection.trySendRegistration(new PlayerRegistration(FIRSTNAME, LASTNAME, UACCOUNT));
		// Saves the data in my Model.
		this.data.registerPlayer(UACCOUNT, FIRSTNAME, LASTNAME, playerId, EPlayer.ME);
		logger.info("Client instance was successfully registered on the server: {} ...", this.data.getPlayerId(EPlayer.ME));
	}
		
	/**
	 * <p>Sends a randomly generated and validated halfmap to the server.</p>
	 * @throws PlayerHalfMapException If the halfmap that was sent to the server does not meet the requirements defined by the server.
	 */
	public void trySendHalfMap() throws PlayerHalfMapException {
		// A map will be generated, if it was invalid an exception is caught and will be generated again.
		final PlayerHalfMap map = this.generateValidNetworkMap(new HalfmapGenerator(), new MapValidator());
		this.connection.trySendHalfMap(map);
	}

	/**
	 * <p>Requesting game state. Basically all data relevant to the game.</p>
	 * <p>Then updates the model.</p>
	 * @throws GameStateException Throws exception if something goes wrong. For example if one queries the server in less than 0.4 seconds.
	 */
	public void tryRequestGameState() throws GameStateException {
		final GameState newGameState = this.connection.tryRequestGameState(this.data.getPlayerId(EPlayer.ME));
		// This takes care of all the data assigning, validation and conversion.
		GameStateRequestDataExtractor.extractData(this.data, newGameState);
	}
	
	/**
	 * <p>Generates a direction based on the current position and map using a DFS and then sends it to the server.</p>
	 * @throws PlayerMoveException If the movement makes the AI go into a water terrain for example an exception is going to be thrown.
	 */
	public void trySendMove() throws PlayerMoveException {
		final EMyMove movement = this.moveEngine.generateMovement(this.data);
		// Converted movement + player Id is contained in this object.
		final PlayerMove playerMove = NetworkDataConverter.convertMovement(this.data.getPlayerId(EPlayer.ME), movement);
		this.connection.trySendMove(playerMove);
		logger.debug("Player: {} sent movement: {}", playerMove.getUniquePlayerID(), movement);
		// Get updated location after sending move.
		this.tryRequestGameState();
	}
	
	/**
	 * <p>Ask if your player is in MustAct state in intervalls of atleast 0.4 seconds.
	 *    If not this polls the server until the condition is met.
	 * </p>
	 * @throws GameStateException, InterruptedException 
	 */
	public void pollServer() throws InterruptedException, GameStateException {
		while(true) {
			this.tryRequestGameState();	
			
			// If not in MustWait state break out of the loop.
			if(!this.data.getPossiblePlayerState(EPlayer.ME).get().equals(WAIT))
				break;
			
			Thread.sleep(POLLING_TIMESPAN_WAIT); 
		}
	}
	
	/**
	 * <p>First a map gets generated.</p>
	 * <p>A new one will be generated if the first was invalid.</p>
	 * <p>After that it gets sent to the server.</p>
	 * @return Returns a randomly generated and validated map.
	 */
	private PlayerHalfMap generateValidNetworkMap(final HalfmapGenerator mapGenerator, final MapValidator mapValidator) {
		// A map gets generated.
		MyPlayerHalfMap map = mapGenerator.generateMap();
		
		// It gets validated.
		try {
			mapValidator.floodFill(map);			
		}
		catch(InvalidMyPlayerHalfMapGenerated e) {
			map = mapGenerator.generateMap();
		}
		
		// When valid it gets converted to a map type specified in the network protocol.
		final PlayerHalfMap networkMap = NetworkDataConverter.convertHalfMap(this.data.getPlayerId(EPlayer.ME), map);
		
		return networkMap;
	}

}