package client.exception.unchecked;

/**
 * <p>Checked exception that is thrown when something goes wrong during player registration.</p>
 * <p>Probably the u:account is invalid - does not exist.</p>
 */
@SuppressWarnings("serial")
public class PlayerRegistrationException extends RuntimeException {

	public PlayerRegistrationException(String message) {
		super(message);
	}
	
}