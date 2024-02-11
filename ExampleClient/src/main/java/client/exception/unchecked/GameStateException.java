package client.exception.unchecked;

/**
 * <p>Checked exception thrown if an error occurs when using GET method to access the game state.</p>
 * <p>Usually happens if the specified unique player identifier does not belong to a player or if there is not atleast 0.4 seconds timespan between requests.</p>
 */
@SuppressWarnings("serial")
public class GameStateException extends RuntimeException {

	public GameStateException(String message) {
		super(message);
	}

}