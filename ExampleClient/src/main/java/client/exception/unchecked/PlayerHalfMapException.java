package client.exception.unchecked;

/**
 * <p>Checked exception that is thrown when an error occurs after sending the PlayerHalfMap object.</p> 
 * <p>Usually meaning the half map that was generated and send is invalid. Look at "Spielidee."</p>
 */
@SuppressWarnings("serial")
public class PlayerHalfMapException extends RuntimeException {

	public PlayerHalfMapException(String message) {
		super(message);
	}

}