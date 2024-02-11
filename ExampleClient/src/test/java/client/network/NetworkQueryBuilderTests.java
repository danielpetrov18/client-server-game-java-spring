package client.network;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

import client.exception.unchecked.InvalidStartingParameterException;

public class NetworkQueryBuilderTests {

	private final static String VALID_GAME_ID = "12345";
	private final static String INVALID_GAME_ID = "1234";
	private final static String INVALID_SERVER_URL = "dummy text";
	private final static String VALID_SERVER_URL = "http://swe1.wst.univie.ac.at:18235";
	
	// Negative tests
	
	@Test
	public void InvalidUrlPassed_QueryBuilderConstructorCalled_ExceptionThrown() {
		assertThrows(InvalidStartingParameterException.class, () -> new ClientNetwork(INVALID_SERVER_URL, VALID_GAME_ID));
	}
	
	@Test
	public void InvalidGameIdPassed_QueryBuilderConstructorCalled_ExceptionThrown() {
		assertThrows(InvalidStartingParameterException.class, () -> new ClientNetwork(VALID_SERVER_URL, INVALID_GAME_ID));
	}
	
}