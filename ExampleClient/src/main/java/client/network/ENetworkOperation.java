package client.network;

/**
 * <p>Used in the NetworkQueryBuilder to create a query depending on the use case to be sent to the server.</p>
 */
public enum ENetworkOperation { 
	REGISTER, 
	SEND_HALFMAP, 
	SEND_MOVE, 
	REQUEST_GAMESTATE 
}