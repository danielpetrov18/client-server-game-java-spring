package client.controller.extractor;

import java.util.Optional;
import java.util.Collection;

import client.player.EPlayer;
import client.model.ClientData;
import client.map.fullmap.MyFullMapNode;
import client.movement.coordinate.Coordinate;
import client.converter.NetworkDataConverter;
import client.map.fullmap.EMyPlayerPositionState;

import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;

public class GameStateRequestDataExtractor {

	/**
	 * This method updated the players information and map.
	 * Additionally if the position of atleast one player has changed the notifyView method in
	 * 		the Model is called and the View is called and represents the changes.
	 * @param data The Model object.
	 * @param gamestate The latest GameState after requesting it.
	 */
	public static void extractData(final ClientData data, final GameState gamestate) {	
		/*
		 *  Gathering both players information. Using Optional because after the
		 *  first player registers the second one is not present so it will be
		 *  an empty object. To avoid if(object != null) I've used Optional.
		 */
		final Optional<PlayerState> newMyPlayer = getPossiblePlayer(data, gamestate, EPlayer.ME);
		final Optional<PlayerState> newEnemyPlayer = getPossiblePlayer(data, gamestate, EPlayer.ENEMY);

		/* 
		 * Check if enemy registration data is present -> if not register him.
		 * This basically sets the opponents credentials once so that we don't need to overwrite every time. 
		 * This happens after the second player/opponent is registered on the server.
		 */
		if(newEnemyPlayer.isPresent() && !data.getPlayer(EPlayer.ENEMY).isRegistered()) {
			final PlayerState enemyNewInfo = newEnemyPlayer.get();
			data.registerPlayer(enemyNewInfo.getUAccount(), enemyNewInfo.getFirstName(), enemyNewInfo.getLastName(), enemyNewInfo.getUniquePlayerID(), EPlayer.ENEMY);
		}
		
		// Update both players if their state or collectedTreasure variable has changed. If not - do nothing.
		updatePlayerInfo(data, EPlayer.ME, newMyPlayer);
		updatePlayerInfo(data, EPlayer.ENEMY, newEnemyPlayer);
		
		/* 
		 * Then update map. Because the MyFullMap object also contains coordinates and the map type
		 * I've decided to pass only the nodes rather than the full map to the converter so that
		 * I can only update the nodes not the entire map object.
		 */
		final Collection<MyFullMapNode> newMapNodes = NetworkDataConverter.convertFullMap(gamestate.getMap().getMapNodes());
		// Nodes are updated.
		data.setMap(newMapNodes);
		
		// Only update coordinates if the fullmap is already present because otherwise players coordinates are invalid.
		if(data.getMap().isFullMapPresent()){
			// Old players coordinates.
			final Coordinate myOld = data.getPosition(EPlayer.ME);
			final Coordinate enemyOld = data.getPosition(EPlayer.ENEMY);
			
			// Getting new ones.
			final Coordinate myNew = newMapNodes
					.stream()
					.filter(p -> p.getPlayerPositionState().equals(EMyPlayerPositionState.MY_PLAYER) 
							||   p.getPlayerPositionState().equals(EMyPlayerPositionState.BOTH_PLAYERS))
					.findFirst()
					.get()
					.getPosition();			
			final Coordinate enemyNew = newMapNodes
					.stream()
					.filter(p -> p.getPlayerPositionState().equals(EMyPlayerPositionState.ENEMY_PLAYER) 
							 ||  p.getPlayerPositionState().equals(EMyPlayerPositionState.BOTH_PLAYERS))
					.findFirst()
					.get()
					.getPosition();
			
			// Check if any of the players coordinates have changed.
			boolean changedPos = false;
			
			boolean newPositionMe = checkPositionsChanged(myOld, myNew);	
			boolean newPositionEnemy = checkPositionsChanged(enemyOld, enemyNew);
			if(newPositionMe || newPositionEnemy) {
				data.setPosition(myNew, EPlayer.ME);
				data.setPosition(enemyNew, EPlayer.ENEMY);	
				changedPos = true;
			}
					
			// If either one of the coordinates have been updated -> notify the view.
			if(changedPos) {
				data.notifyView();				
			}
		}
	}
	
	/**
	 * @return Checks to see if either of the players' position has changed after last request.
	 */
	private static boolean checkPositionsChanged(final Coordinate previous, final Coordinate current) {
		return !previous.equals(current);
	}
	
	/**
	 * @param gameState New game state after request.
	 * @param meOrEnemy The specific player you are interested in.s
	 * @return Data about the corresponding player player.
	 */
	private static Optional<PlayerState> getPossiblePlayer(final ClientData data, final GameState gameState, final EPlayer meOrEnemy) {
		if(meOrEnemy.equals(EPlayer.ME)) {
			return gameState
					.getPlayers()
					.stream()
					.filter(p -> p.getUniquePlayerID().equals(data.getPlayerId(EPlayer.ME)))
					.findFirst();
		}
		else {
			return gameState
					.getPlayers()
					.stream()
					.filter(p -> !p.getUniquePlayerID().equals(data.getPlayerId(EPlayer.ME)))
					.findFirst();
		}
	}
	
	/**
	 * @param meOrEnemy Specific player ME or ENEMY.
	 * @param possiblePlayer Optional of PlayerState returned from server.
	 */
	private static void updatePlayerInfo(final ClientData data, final EPlayer meOrEnemy, final Optional<PlayerState> possiblePlayer) {
		possiblePlayer.ifPresent(player -> {
			NetworkDataConverter.convertPlayerState(data.getPlayer(meOrEnemy).getGameState(), player);
		});
 	}
	
}