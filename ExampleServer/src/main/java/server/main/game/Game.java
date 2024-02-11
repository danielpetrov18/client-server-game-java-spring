package server.main.game;

import java.util.Set;
import java.util.List;
import java.util.UUID;
import java.util.Random;
import java.util.HashSet;
import java.util.Optional;

import java.time.LocalDateTime;

import server.main.player.Player;
import server.main.player.EPlayerState;
import server.main.map.fullmap.MyFullMap;
import server.main.map.fullmap.MyFullMapNode;
import server.main.rules.register.RegisterTwoPlayersPerGameRule;

public class Game {
	
	private String gameStateID;
	private final String gameID;
	private final MyFullMap fullmap;
	private final Set<Player> students;
	private final LocalDateTime startTime;
	public final static int MAX_STUDENT_COUNT = 2;
	private final static Random RND = new Random();
	
	public Game(final String gameID) {
		this.gameID = gameID;
		this.updateGameStateID();
		this.fullmap = new MyFullMap();
		this.students = new HashSet<>();
		this.startTime = LocalDateTime.now();	
	}
	
	public String getGameStateID() {
		return this.gameStateID;
	}

	public String getGameId() {
		return this.gameID;
	}
	
	public MyFullMap getMap() {
		return this.fullmap;
	}
	
	public Optional<Player> getPlayer(final String playerId) {
		return this.students.stream().filter(stu -> stu.getPlayerId().equals(playerId)).findFirst();
	}

	public Optional<Player> getOtherPlayer(final String playerId) {
		return this.students.stream().filter(stu -> !stu.getPlayerId().equals(playerId)).findFirst();
	}
	
	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	public boolean hasPlayerSentHalfmapAlready(final String playerId) {
		return this.fullmap.checkSentHalfmap(playerId);
	}
	
	public int getRegisteredStudentsCount() {
		return this.students.size();
	}
	
	public Optional<Player> getMustActPlayer() {
		return this.students.stream().filter(stu -> stu.getState().equals(EPlayerState.MustAct)).findFirst();
	}
	
	/*
	 * After adding the first student he always gets a MustWait state.
	 * After adding the second one both get randomly assigned state.
	 */
	public String addPlayer(final String fn, final String ln, final String uacc) {
		final RegisterTwoPlayersPerGameRule twoPlayersRule = new RegisterTwoPlayersPerGameRule(this.students.size());
		twoPlayersRule.validate();
		
		final String playerId = UUID.randomUUID().toString();
		this.students.add(new Player(fn, ln, uacc, playerId));
		
		// Set a random player state for registered player.
		if(this.students.size() < MAX_STUDENT_COUNT) {
			// The first student always receives a MustWait state.
    		this.students.stream().filter(stu -> stu.getPlayerId().equals(playerId)).findFirst().get().updateState(EPlayerState.MustWait);
		}
		else {
			// When the 2nd player gets registered the both players would get a random state.
			final List<EPlayerState> validStartingStates = List.of(EPlayerState.MustWait, EPlayerState.MustAct);
			final int statesCnt = validStartingStates.size();
			
			final EPlayerState firstState = validStartingStates.get(RND.nextInt(statesCnt));
			final EPlayerState secondState = firstState.equals(EPlayerState.MustAct) ? EPlayerState.MustWait : EPlayerState.MustAct;
			
			// Additional randomization.
			if(RND.nextInt(statesCnt) == 0) {
				this.getPlayer(playerId).get().updateState(firstState);
				this.getOtherPlayer(playerId).get().updateState(secondState);				
			}
			else {
				this.getOtherPlayer(playerId).get().updateState(firstState);	
				this.getPlayer(playerId).get().updateState(secondState);
			}
		}		
		
    	this.updateGameStateID();
    	
		return playerId;
	}
		
	public void addHalfmap(final String uniquePlayerId, final Set<MyFullMapNode> halfMapNodes) {
		this.fullmap.addHalfmap(uniquePlayerId, halfMapNodes);
		this.updateGameStateID();
	}
		
	/* 
	 * This method receives the player ID of the loser. -> sets the corresponding states.
	 * If the game ends because the time has ran out or the oldest game has to be removed
	 * because of lack of space the person that is on turn should lose.
	 * But there's possibility that there's only 1 or 0 registered people for particular game.
	 * In that case the possibleLoser will be empty.
	 */
	public void endGame(final Optional<Player> possibleLoser) {
		int peopleCnt = this.getRegisteredStudentsCount();
		
		if(peopleCnt == MAX_STUDENT_COUNT) {
			// If there was a player making a turn when the game ended the object is not empty!
			if(possibleLoser.isPresent()) {
				possibleLoser.get().updateState(EPlayerState.Lost);
				this.getOtherPlayer(possibleLoser.get().getPlayerId()).get().updateState(EPlayerState.Won);			
			}
			// If there was no player with MustAct state -> the game had been ended previously.
		}
		// If only one player is registered he is always in MustWait state.
		else if(peopleCnt == 1) {
			this.students.stream().findFirst().get().updateState(EPlayerState.Lost);		
		}
	
		this.updateGameStateID();
	}
	
	private void updateGameStateID() {
		this.gameStateID = UUID.randomUUID().toString();
	}

}