package server.main.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Set;
import java.util.Iterator;
import java.util.Optional;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromclient.PlayerHalfMap;

import server.main.game.Game;
import server.main.player.Player;
import server.main.player.EPlayerState;
import server.main.map.fullmap.MyFullMapNode;
import server.main.generator.game.GameIdGenerator;
import server.main.converter.NetworkDataConverter;
import server.main.map.validator.HalfmapValidator;
import server.main.rules.game.GameMaxCapacityRule;
import server.exceptions.game.GameDoesNotExistException;
import server.exceptions.game.GameNonRegisteredPlayerException;
import server.exceptions.map.MapPlayerAlreadySentHalfmapException;
import server.exceptions.map.MapExpectedMinimumFieldCountFromGivenTypeException;

@Component
public class ServerController {
	
	private final static int MAX_GAME_MINUTES = 10;
	private final static int MAX_GAME_COUNT_ALLOWED = 99; 
	private final static int CHECK_GAME_NEEDS_TO_BE_REMOVED_TIMESPAN_MILLISECONDS = 1000; // 1 second  
	
	private final LinkedList<Game> allGames;
	private final NetworkDataConverter dataConverter;
	private final static Logger LOGGER = LoggerFactory.getLogger(ServerController.class);
	private final static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss"); // Used for pretty output.
	
	public ServerController() {
		this.allGames = new LinkedList<>();
		this.dataConverter = new NetworkDataConverter();
	}
	
	/*
	 * Checks if there's space for a new game.
	 * If no the oldest game gets removed, if yes random gameId is generated and a new game is created.
	 * 
	 * There's also a business rule checking to see if the game limit has been exceeded.
	 */
	public String createNewGame() {		
		if(this.allGames.size() == MAX_GAME_COUNT_ALLOWED) {
			final Game removedGame = this.allGames.removeFirst();
			final Optional<Player> possibleStudent = removedGame.getMustActPlayer();
			removedGame.endGame(possibleStudent);
		}

		// Creating a new game.
		final String gameIdentifier = GameIdGenerator.generateRandomGameId();
		final Game game = new Game(gameIdentifier);
			
		// Add the new game.
		this.allGames.add(game);
		LOGGER.debug("Added new game: {}, at: {}. Total game count: {}", gameIdentifier, game.getStartTime().format(DATETIME_FORMATTER), this.allGames.size());
		
		final GameMaxCapacityRule maxGameCountRule = new GameMaxCapacityRule(this.allGames.size());
		maxGameCountRule.validate();
		
		return gameIdentifier;
	}

	public String tryAddPerson(final String gameId, final String fn, final String ln, final String uacc) {
		final Optional<Game> possibleGame = this.fetchGame(gameId);
    	if(possibleGame.isEmpty()) {
    		throw new GameDoesNotExistException("GameDoesNotExistException", "Game with id [" + gameId + "] does not exist!");    		
    	}
    	
    	final Game game = possibleGame.get();
    	return game.addPlayer(fn, ln, uacc);
    }
	
	/*
	 * If the game does not exist, if the player is not registered or has sent his half map already an exception is thrown.
	 * 
	 * If 2 players are not already registered an exception will also be thrown.
	 * 
	 * If there are 2 registered players and if the player is not in MustWait state the half map validated and saved.
	 */
	public void tryAddHalfMap(final String gameId, final PlayerHalfMap halfmap) {
		final Optional<Game> possibleGame = this.fetchGame(gameId);
		if(possibleGame.isEmpty()) {
			throw new GameDoesNotExistException("GameDoesNotExistException", "Game with id [" + gameId + "] does not exist!");			
		}
		
		final Game game = possibleGame.get();
		final String playerId = halfmap.getUniquePlayerID();
		if(game.getPlayer(playerId).isEmpty()) {
			final String errMsg = "Player [" + playerId + "] is not registered for this game!";
			throw new GameNonRegisteredPlayerException("GameNonRegisteredPlayerException", errMsg);			
		}
			
		if(game.hasPlayerSentHalfmapAlready(playerId)) {
			LOGGER.debug("Player {} already sent a halfmap. Game count before ending this one: {}.", playerId, this.allGames.size());
			final String errMsg = "Player [" + playerId + "] has already sent a halfmap!";
			final Optional<Player> possibleStudent = game.getMustActPlayer();
			game.endGame(possibleStudent);
			this.allGames.removeIf(lambdaGame -> lambdaGame.getGameId().equals(gameId));
			LOGGER.debug("Game count after ending this one: {}", this.allGames.size());
			throw new MapPlayerAlreadySentHalfmapException("MapPlayerAlreadySentHalfmapException", errMsg);			
		}
		
		if(game.getRegisteredStudentsCount() < Game.MAX_STUDENT_COUNT || game.getPlayer(playerId).get().getState().equals(EPlayerState.MustWait)) {
			return;			
		}

		/*
		 * I'm purposely throwing this exception because this is the only rule about the half map that the client tests.
		 */
		try {
			HalfmapValidator.validateHalfmap(halfmap);
		}
		catch(MapExpectedMinimumFieldCountFromGivenTypeException e) {
			final Optional<Player> possibleStudent = game.getMustActPlayer();
			game.endGame(possibleStudent);
			throw new MapExpectedMinimumFieldCountFromGivenTypeException("MapExpectedMinimumFieldCountFromGivenTypeException", e.getMessage());
		}
		
		final Set<MyFullMapNode> validConvertedFullMapNodes = this.dataConverter.convertHalfmapFromNetwork(halfmap); 
		game.addHalfmap(playerId, validConvertedFullMapNodes);
		
		// After sending the half map -> update both players state
		game.getPlayer(playerId).get().updateState(EPlayerState.MustWait);
		game.getOtherPlayer(playerId).get().updateState(EPlayerState.MustAct);
	}
	
	public GameState tryGetGameState(final String gameID, final String playerID) {
		final Optional<Game> possibleGame = this.fetchGame(gameID);
		if(possibleGame.isEmpty()) {
			throw new GameDoesNotExistException("GameDoesNotExistException", "Game [" + gameID + "] does not exist!");			
		}
		
		final Game game = possibleGame.get();
		if(game.getPlayer(playerID).isEmpty()) {
			final String msg = "Non registered player: [" + playerID + "] requesting game state for: [" + gameID + "]!";
			throw new GameNonRegisteredPlayerException("GameNonRegisteredPlayerException", msg);
		}
		
		return this.dataConverter.convertGameDataToGameState(playerID, game);
	}
	
    /*
     * Method that makes use of Spring boot Scheduled annotation that checks every 60 seconds if a game has ran longer than 10 minutes.
     * If so the games method endGame() is called and then the game is removed from the ServerController.
     */
    @Scheduled(fixedRate = CHECK_GAME_NEEDS_TO_BE_REMOVED_TIMESPAN_MILLISECONDS)
    private void removeOldGames() {
    	if(!this.allGames.isEmpty()) {
    		final LocalDateTime now = LocalDateTime.now();
    		
    		// Going over each game.
    		final Iterator<Game> iterator = this.allGames.iterator();
    		while (iterator.hasNext()) {
    			final Game game = iterator.next();
    			final LocalDateTime gameStartTime = game.getStartTime();
    			
    			// Check if the game has been running for more than 10 minutes.
    			if (gameStartTime.plusMinutes(MAX_GAME_MINUTES).isBefore(now)) {
    				// Before removing it -> end the game.
    				// The person that loses is the on that is on turn.
    				final Optional<Player> possibleStudent = game.getMustActPlayer();
    				game.endGame(possibleStudent);
    				iterator.remove(); 
    				LOGGER.debug("Removing old game: {}. [Started at: {}. Stopped at: {}.]", game.getGameId(), game.getStartTime().format(DATETIME_FORMATTER), LocalDateTime.now().format(DATETIME_FORMATTER));
    			}
    		}    		
    	}		
    }
    
    private Optional<Game> fetchGame(final String gameId) {
    	return this.allGames
    					.stream()
    					.filter(g -> g.getGameId().equals(gameId))
    					.findFirst();
    }
      
}