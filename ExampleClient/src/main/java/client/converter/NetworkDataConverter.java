package client.converter;

import java.util.Set;
import java.util.Optional;
import java.util.Collection;
import java.util.stream.Collectors;

import client.map.EMyTerrain;
import client.player.PlayerGameState;
import client.map.fullmap.EMyFortState;
import client.map.fullmap.MyFullMapNode;
import client.player.EMyPlayerGameState;
import client.movement.direction.EMyMove;
import client.map.halfmap.MyPlayerHalfMap;
import client.map.fullmap.EMyTreasureState;
import client.movement.coordinate.Coordinate;
import client.map.fullmap.EMyPlayerPositionState;

import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.PlayerState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;

/**
 * <p>Converts my custom defined classes to the ones provided in the protocol and the other way around.</p>
 */
public class NetworkDataConverter {
	
	/**
	 * @param currPlayerState This is a object containing a player of my type.
	 * @param playerState Information about a player either enemy or me that is received from the network.
	 */
	public static void convertPlayerState(final PlayerGameState currPlayerState, final PlayerState playerState) {		
		// Check if the new players state is different from the current one or if it's null (at the beginning) -> if so change.
		final Optional<EMyPlayerGameState> curState = currPlayerState.getPossibleState();
		final EMyPlayerGameState convertedNetworkState = NetworkDataEnumConverter.determineMyPlayerGameState(playerState.getState());
		if(curState.isEmpty() || !curState.get().equals(convertedNetworkState)) {
			currPlayerState.updateState(convertedNetworkState);			
		}
		
		// Check if player has collected treasure and it's not already set in my class -> if yes -> update.
		if(!currPlayerState.hasCollectedTreasure() && playerState.hasCollectedTreasure()) {
			currPlayerState.collectTreasure();			
		}
	}
	
	/**
	 * The reason why I pass the nodes instead of the Fullmap is because the map
	 * also contains other relevant data so I just want to overwrite the nodes.
	 * @param networkMapNodes Nodes of the fullmap we receive on gamestate request.
	 * @return Returns the same nodes just converted to my type of node.
	 */
	public static Collection<MyFullMapNode> convertFullMap(final Collection<FullMapNode> networkMapNodes) {	
		// Mapping each FullMapNode to MyFullMapNode and then collecting them in a Set.
		final Set<MyFullMapNode> myNodes = networkMapNodes
				.stream()
				.map(networkNode -> {
					final EMyTerrain terrain = NetworkDataEnumConverter.determineMyTerrain(networkNode.getTerrain());
					final EMyPlayerPositionState playerPosState = NetworkDataEnumConverter.determineMyPlayerPositionState(networkNode.getPlayerPositionState());
					final EMyTreasureState treasureState = NetworkDataEnumConverter.determineTreasureState(networkNode.getTreasureState());
					final EMyFortState fortState = NetworkDataEnumConverter.determineMyFortState(networkNode.getFortState());
					final Coordinate coord = new Coordinate(networkNode.getX(), networkNode.getY());				
					final MyFullMapNode myNode = new MyFullMapNode(terrain, playerPosState, treasureState, fortState, coord);
					return myNode;
				})
				.collect(Collectors.toSet());
		
		return myNodes;
	}
	
	/**
	 * @param uniquePlayerId Player id that is uniquelly tied to the map.
	 * @param map Map of my type.
	 * @return Returns the same map basically but converts the nodes to the type specified in the network protocol.
	 */
	public static PlayerHalfMap convertHalfMap(final String uniquePlayerId, final MyPlayerHalfMap map) {
		final Set<PlayerHalfMapNode> networkNodes = map
								.getMapNodes()
								.stream()
								.map(myNode -> {
									final ETerrain terrain = NetworkDataEnumConverter.determineNetworkTerrain(myNode.getTerrain());
									final PlayerHalfMapNode networkNode = new PlayerHalfMapNode(myNode.getX(), myNode.getY(), myNode.isFortPresent(), terrain);
									return networkNode;
								}) 
								.collect(Collectors.toSet());
		
		// Convert to map specified in network protocol.
		return new PlayerHalfMap(uniquePlayerId, networkNodes);
	}
		
	/**
	 * @param move Takes my custom ENUM EMyMove object.
	 * @return Returns a PlayerMove object.
	 */
	public static PlayerMove convertMovement(final String uniquePlayerId, final EMyMove move) {
		final EMove convertedMove = NetworkDataEnumConverter.determineNetworkMove(move);
		final PlayerMove convertedObj = PlayerMove.of(uniquePlayerId, convertedMove);
		return convertedObj;
	}

}