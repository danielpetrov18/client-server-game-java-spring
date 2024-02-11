package client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerRegistration;

import client.exception.unchecked.GameStateException;
import client.exception.unchecked.PlayerMoveException;
import client.exception.unchecked.PlayerHalfMapException;
import client.exception.unchecked.PlayerRegistrationException;

/**
 * <p>This class is used to encapsulate all the data transferring and requesting logic.</p>
 * <p>It's supposed to be used by the controller to manage the communication between the server and the client.</p>
 */
public class ClientNetwork implements INetwork {
	
	private final NetworkQueryBuilder queryBuilder;
	private final static Logger logger = LoggerFactory.getLogger(ClientNetwork.class);

	public ClientNetwork(final String serverBaseUrl, final String gameId) {
		this.queryBuilder = new NetworkQueryBuilder(serverBaseUrl, gameId);
	}
	
	/**
	 * <p> This method is for transferring the player registration to the server using HTTP Post method.</p>
	 * @param playerReg This is the data sent to register a player AI on the server.
	 * @return Returns the unique player identifier after successful registration on the server.
	 * @throws PlayerRegistrationException Exception that occurs usually when the u:account provided is invalid.
	 */
	public String trySendRegistration(final PlayerRegistration playerReg) throws PlayerRegistrationException {
		// Sending the registration data.
		final ResponseEnvelope<?> resultRegistration = this.queryBuilder
																	.createQuery(ENetworkOperation.REGISTER, playerReg)
																	.block();
		
		// If an error occurred when transferring the data handle it. 
		if (resultRegistration.getState() == ERequestState.Error) {
			logger.error("Error:[{}] during registration ...", resultRegistration.getExceptionName());
			throw new PlayerRegistrationException(resultRegistration.getExceptionMessage());
		} 
		
		// Extracting the data from the ResponseEnvelope.
		final UniquePlayerIdentifier playerId = (UniquePlayerIdentifier)resultRegistration.getData().get();
		
		return  playerId.getUniquePlayerID();
	}
	
	/**
	 * <p> This is method is for transferring the player half map to the server using HTTP Post method.</p>
	 * @param playerHalfMap This is the half map data to be sent.
	 * @throws PlayerHalfMapException Gets thrown if the half map sent to the server does not meet the requirements.
	 */
	public void trySendHalfMap(final PlayerHalfMap playerHalfMap) throws PlayerHalfMapException {
		// Sending the halfmap.
		final ResponseEnvelope<?> resultSendHalfMap = this.queryBuilder
																	.createQuery(ENetworkOperation.SEND_HALFMAP, playerHalfMap)
																	.block();
		
		// Only if an error occurs.
		if (resultSendHalfMap.getState() == ERequestState.Error) {
			logger.error("Error:[{}] while sending halfmap ...", resultSendHalfMap.getExceptionName());
			throw new PlayerHalfMapException(resultSendHalfMap.getExceptionMessage());
		} 
	}
	
	/**
	 * <p> This is method is for transferring the player movement to the server using HTTP Post method.</p>
	 * @param playerMove A movement object containing direction and unique player id.
	 * @throws PlayerMoveException Exception that occurs if the algorithm for traversing the map is flawed. Going in a water field for example.
	 */
	public void trySendMove(final PlayerMove playerMove) throws PlayerMoveException {	
		// Sending a move.
		final ResponseEnvelope<?> resultSendMove = this.queryBuilder
																.createQuery(ENetworkOperation.SEND_MOVE, playerMove)
																.block();
		
		// Handling the error if one occurs.
		if (resultSendMove.getState() == ERequestState.Error) {
			logger.error("Error:[{}] while sending a move ...", resultSendMove.getExceptionName());
			throw new PlayerMoveException(resultSendMove.getExceptionMessage());
		}
	}
	
	/**
	 * <p>This is method is getting the game state over GET HTTP method.</p>
	 * @param uniquePlayerId This is a unique identifier specifying to the server which player AI to move.
	 * @return Return the game state after getting the data from the server.
	 * @throws GameStateException If there was something wrong while requesting data from server. Usually not keeping a delay of 0.4 seconds.
	 */
	@SuppressWarnings("unchecked")
	public GameState tryRequestGameState(final String uniquePlayerId) throws GameStateException {
		// Sending the request.
		final ResponseEnvelope<GameState> requestResult = this.queryBuilder
																.createQuery(ENetworkOperation.REQUEST_GAMESTATE, uniquePlayerId)
																.block();

		// Handling the error.
		if (requestResult.getState() == ERequestState.Error) {
			logger.error("Error:[{}] while asking for game state ...", requestResult.getExceptionName());
			throw new GameStateException(requestResult.getExceptionMessage());
		}
		
		// Or a valid response.
		return requestResult.getData().get();
	}
	
}