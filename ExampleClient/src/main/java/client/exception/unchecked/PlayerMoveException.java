package client.exception.unchecked;

/**
 * <p>Checked exception that is thrown when an invalid movement was sent to the server.</p>
 * <p>Usually means outside of the map or movement towards a water field.</p>
 */
@SuppressWarnings("serial")
public class PlayerMoveException extends RuntimeException {

	public PlayerMoveException(String message) {
		super(message);
	}

}