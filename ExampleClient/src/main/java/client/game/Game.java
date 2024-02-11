package client.game;

import java.beans.PropertyChangeSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.player.EPlayer;
import client.model.ClientData;
import client.network.INetwork;
import client.network.ClientNetwork;
import client.view.ClientVisualization;
import client.controller.ClientController;
import client.exception.unchecked.GameStateException;
import client.exception.unchecked.PlayerMoveException;
import client.exception.unchecked.PlayerHalfMapException;
import client.exception.unchecked.PlayerRegistrationException;
import client.exception.unchecked.InvalidStartingParameterException;

public class Game {

	private final static Logger logger = LoggerFactory.getLogger(Game.class);
	
	public static void newGame(final String serverBaseUrl, final String gameId) {
		try {
			final INetwork conn = new ClientNetwork(serverBaseUrl, gameId);
			
			final ClientData model = new ClientData(new PropertyChangeSupport(ClientData.class));
			final ClientController controller = new ClientController(model, conn);
			final ClientVisualization view = new ClientVisualization(model);

			logger.info("About to register ...");
			controller.trySendRegistration();
						
			controller.pollServer();
			switch(model.getPossiblePlayerState(EPlayer.ME).get()) {
				case MUST_ACT:
					controller.trySendHalfMap();
					logger.info("Sent the halfmap ...");
					break;
				case WON:
					logger.info("YOU WON ...");
					System.exit(0);
				case LOST:
					logger.info("YOU LOST ...");
					System.exit(-1);
				default:
					logger.error("Something unexpected happened ...");
					System.exit(-1);
			}
						
			while(true) {
				controller.pollServer();					
				switch(model.getPossiblePlayerState(EPlayer.ME).get()) {
					case MUST_ACT:
						controller.trySendMove();					
						break;
					case WON:
						logger.info("YOU WON ...");
						System.exit(0);
					case LOST:
						logger.info("YOU LOST ...");
						System.exit(-1);
					default:
						logger.error("Something unexpected happened ...");
						System.exit(-1);
				}
			}
		} 
		catch (PlayerRegistrationException | InterruptedException | GameStateException | PlayerHalfMapException | PlayerMoveException | InvalidStartingParameterException e) {
			logger.error("Error msg: [{}]", e.getMessage());
			e.printStackTrace();
		}
	}
	
}
