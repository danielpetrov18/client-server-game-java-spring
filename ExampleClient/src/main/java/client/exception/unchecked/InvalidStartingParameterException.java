package client.exception.unchecked;

/**
 * <p>Gets thrown if the user enters invalid URL or game ID at the start of the game.</p>
 */
@SuppressWarnings("serial")
public class InvalidStartingParameterException extends RuntimeException {

	public InvalidStartingParameterException(String message) {
		super(message);
	}

}