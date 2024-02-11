package server.main.converter;

import java.util.Set;
import java.util.Optional;
import java.util.Collection;
import java.util.stream.Collectors;

import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.PlayerState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;

import server.main.game.Game;
import server.main.player.Player;
import server.main.map.EMyTerrain;
import server.main.map.Coordinate;
import server.main.map.fullmap.MyFullMap;
import server.main.player.EPlayerIdStatus;
import server.main.map.fullmap.EMyFortState;
import server.main.map.fullmap.MyFullMapNode;
import server.main.map.fullmap.EMyTreasureState;
import server.main.map.fullmap.EMyPlayerPositionState;
import server.main.generator.map.TreasureCoordinateGenerator;
import server.main.generator.map.EnemyPlayerPositionGenerator;

public class NetworkDataConverter {
	
	/*
	 * First the game state id is collected.
	 * 
	 * Then the player information for each player if available.
	 * Optional was used because at the beginning stages when requesting
	 * the game state there might be only one player registered.
	 * Additionally, if a player is present a ternary operator is used (:/?)
	 * just to avoid using if/else statements.
	 * 
	 * Lastly the map information is collected.
	 */
	public GameState convertGameDataToGameState(final String playerId, final Game game) {
		final String gameStateID = game.getGameStateID();
		
		final Optional<Player> myPlayer = game.getPlayer(playerId);
		final Optional<Player> otherPlayer = game.getOtherPlayer(playerId);
		
		// If the player object is present convert the available data. Otherwise -> empty object.
		final PlayerState me = myPlayer.isPresent() 
				? this.convertMyPlayerToPlayerState(myPlayer.get(), EPlayerIdStatus.ShowPlayerID) 
				: new PlayerState();
		final PlayerState enemy = otherPlayer.isPresent() 
				? this.convertMyPlayerToPlayerState(otherPlayer.get(), EPlayerIdStatus.HidePlayerID) 
				: new PlayerState();
		final Collection<PlayerState> players = Set.of(me, enemy);	
		
		final FullMap networkMap = this.convertMyFullMapToNetworkFullMap(playerId, game.getMap());
		
		return new GameState(networkMap, players, gameStateID);
	}
		
	/*
	 * This method goes over each PlayerHalfMapNode element and converts it to a MyFullMapNode
	 * using all the available data and then collects all the new nodes in a set. It makes use
	 * of streams and the map function which maps an object to a new one.
	 * 
	 * FullMapTreasureGenerator then is used to generate a random coordinate for the treasure.
	 * 
	 * The fort state and player position state depend on where the isFortPresent() variable is set to true.
	 * 
	 * The playerId is added to each node which would be relevant later for the game state.
	 */
	public Set<MyFullMapNode> convertHalfmapFromNetwork(final PlayerHalfMap networkMap) {
		final String playerId = networkMap.getUniquePlayerID();
		
		final Set<MyFullMapNode> myNodes = networkMap
				.getMapNodes()
				.stream()
				.map(networkNode -> {	
					final EMyTerrain terrain = EnumConverter.determineMyTerrain(networkNode.getTerrain());
					final EMyPlayerPositionState ps = networkNode.isFortPresent() 
																	? EMyPlayerPositionState.MyPlayerPosition 
																	: EMyPlayerPositionState.NoPlayerPresent;
					final EMyFortState fs = networkNode.isFortPresent() 
																	? EMyFortState.MyFortPresent 
																	: EMyFortState.NoOrUnknownFortState;
					final EMyTreasureState ts = EMyTreasureState.NoOrUnknownTreasureState; 		// Assume it's empty at first.
					final Coordinate coord = new Coordinate(networkNode.getX(), networkNode.getY());

					return new MyFullMapNode(playerId, terrain, ps, ts, fs, coord);
				})
				.collect(Collectors.toSet());
		
		// After the map has been converted a treasure must be placed on a grass field that does not contain a castle.
		final Coordinate treasurePos = TreasureCoordinateGenerator.generateFullMapTreasurePosition(myNodes);
		myNodes
			.stream()
			.filter(node -> node.getCoordinate().equals(treasurePos))
			.findFirst()
			.get()
			.placeTreasure();
		
		return myNodes;
	}
	
	/*
	 * Converts the data that is stored on the server side of a the requesting player into PlayerState object
	 * and depending on which client is requesting the data the true player id or a fake one is returned.
	 */
	private PlayerState convertMyPlayerToPlayerState(final Player stu, final EPlayerIdStatus hideOrShow) {
		final String fname = stu.getFirstname();
		final String lname = stu.getLastname();
		final String uacc = stu.getUAccount();
		final EPlayerGameState state = EnumConverter.determineNetworkGameState(stu.getState());
		// If I'm the requesting player I cannot see the opponents playerId.
		final UniquePlayerIdentifier playerId = hideOrShow.equals(EPlayerIdStatus.ShowPlayerID) 
				? UniquePlayerIdentifier.of(stu.getPlayerId()) 
				: UniquePlayerIdentifier.of("otherPlayer");		
		final boolean hasTreasure = stu.hasCollectedTreasure();
												
		return new PlayerState(fname, lname, uacc, state, playerId, hasTreasure);
	} 
	
	/*
	 * Returns a full map after request from a client.
	 * 
	 * Enemy position should be random.
	 * 
	 * This implementation does not take into consideration the fact
	 * that a player might discover the enemy castle or treasure.
	 * Move end-point is not implemented.
	 */
	private FullMap convertMyFullMapToNetworkFullMap(final String playerId, final MyFullMap fullmap) {
		if(fullmap.isMapEmpty()) {
			return new FullMap();			
		}
		
		final Coordinate randomEnemyPos = EnemyPlayerPositionGenerator.generateRandomCoordinate(fullmap.getMapNodes());
		
		final Set<FullMapNode> networkNodes = fullmap
								.getMapNodes()
								.stream()
								.map(myNode -> {
			
			final ETerrain terrain = EnumConverter.determineNetworkTerrain(myNode.getTerrain());
			EPlayerPositionState ps = EPlayerPositionState.NoPlayerPresent;
			EFortState ft = EFortState.NoOrUnknownFortState;
			if(myNode.amIHere(playerId) && randomEnemyPos.getX() == myNode.getX() && randomEnemyPos.getY() == myNode.getY()) {
				ps = EPlayerPositionState.BothPlayerPosition;
				ft = EFortState.MyFortPresent;
			}
			else if(myNode.amIHere(playerId)) {
				ps = EPlayerPositionState.MyPlayerPosition;
				ft = EFortState.MyFortPresent;
			}
			else if(randomEnemyPos.getX() == myNode.getX() && randomEnemyPos.getY() == myNode.getY()) {
				ps = EPlayerPositionState.EnemyPlayerPosition;
			}
			
			final ETreasureState ts = ETreasureState.NoOrUnknownTreasureState;
			
			return new FullMapNode(terrain, ps, ts, ft, myNode.getX(), myNode.getY());
		})
		.collect(Collectors.toSet());
		
		return new FullMap(networkNodes);
	}
		
}