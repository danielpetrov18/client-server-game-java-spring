package server.main.generator.game;

import java.util.Random;

/*
 * Generates the game id on access of "/games" end-point.
 * 
 * The game id can contain all digits, lower case and upper case letters.
 * 
 * It should have length of 5.
 */
public class GameIdGenerator {

	private final static int GAME_ID_LENGTH = 5;
	private final static Random rnd = new Random();
	private static final String DIGITS = "0123456789";
	private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALLOWED_CHARACTERS = LOWERCASE_LETTERS + UPPERCASE_LETTERS + DIGITS;
    private static final int ALLOWED_CHARACTERS_LENGTH = ALLOWED_CHARACTERS.length();
	
	public static String generateRandomGameId() {
		final StringBuilder newGameId = new StringBuilder();
		
		for(int gameIdIndex = 0; gameIdIndex < GAME_ID_LENGTH; ++gameIdIndex) {
			final int charIndex = rnd.nextInt(0, ALLOWED_CHARACTERS_LENGTH);
			newGameId.append(ALLOWED_CHARACTERS.charAt(charIndex));
		}
		
		return newGameId.toString();
	}
	
}