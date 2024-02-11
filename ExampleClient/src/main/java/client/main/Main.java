package client.main;

import client.game.Game;

public class Main {

	public static void main(String[] args) {
		
		final String serverUrl = args[1];
		final String gameId = args[2];
		
		Game.newGame(serverUrl, gameId);
					
	}
	
}