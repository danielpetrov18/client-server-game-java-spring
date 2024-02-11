package client.exception.unchecked;

/**
 * <p>This error is thrown if a node is generated with invalid (out of bounds) coordinates.</p>
 */
@SuppressWarnings("serial")
public class InvalidPlayerNodeCoordinateValuesException extends RuntimeException {

	public InvalidPlayerNodeCoordinateValuesException(String message) {
		super(message);
	}

}