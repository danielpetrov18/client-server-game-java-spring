package client.network;

import client.exception.unchecked.GameStateException;
import client.exception.unchecked.PlayerMoveException;
import client.exception.unchecked.PlayerHalfMapException;
import client.exception.unchecked.PlayerRegistrationException;

import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;

/**
 * <p>The reason why this interface exists is only because it makes it easier to mock the network and test the controller.</p> 
 * <p>My intent was basically to decouple the components ClientNetwork and ClientController and use Dependency Injection.</p>
 */
public interface INetwork {
		
	String trySendRegistration(final PlayerRegistration playerReg) throws PlayerRegistrationException; 
	
	void trySendHalfMap(final PlayerHalfMap playerHalfMap) throws PlayerHalfMapException;
	
	void trySendMove(final PlayerMove playerMove) throws PlayerMoveException;
		
	GameState tryRequestGameState(final String uniquePlayerId) throws GameStateException;
		
}