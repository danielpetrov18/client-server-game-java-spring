package client.view.emojis;

import java.util.Map;
import java.util.HashMap;

import client.map.fullmap.EMyFortState;
import client.player.EMyPlayerGameState;
import client.map.fullmap.EMyPlayerPositionState;

/**
 * <p>This class holds all the emojis for the visualization of the players and the map.</p>
 */
public class VisualizationEmojis {	
	
	/**
	 * <p>Represents the players on the map.</p>
	 */
	private final static Map<EMyPlayerPositionState, String> PLAYER = new HashMap<>() {{
		put(EMyPlayerPositionState.MY_PLAYER, "👽");
		put(EMyPlayerPositionState.ENEMY_PLAYER, "😈");
		put(EMyPlayerPositionState.BOTH_PLAYERS, "🗡️");
	}};
	
	/**
	 * <p>My and enemy castle representation.</p>
	 */
	private final static Map<EMyFortState, String> CASTLE = new HashMap<>() {{
		put(EMyFortState.MY_FORT, "🏛");
		put(EMyFortState.ENEMY_FORT, "🎪");
	}};
	
	/**
	 * <p>Visualizes the current state a given player is.
	 */
	private final static Map<EMyPlayerGameState, String> STATE = new HashMap<>() {{
		put(EMyPlayerGameState.WON, "👑");
		put(EMyPlayerGameState.LOST, "😱");
		put(EMyPlayerGameState.MUST_ACT, "🏃‍");
		put(EMyPlayerGameState.MUST_WAIT, "⏳");
	}};
	
	/**
	 * <p>Shows if a player has collected his treasure or not.</p>
	 */
	private final static Map<Boolean, String> TREASURE = new HashMap<>() {{
		put(true, "✔");
		put(false, "❌");
	}};
	
	/**
	 * @param positionState Position state signifies if a given player or both players are located on a given field.<p>
	 * @return If me - 👽, if enemy - 😈, if both - 🗡️.
	 */
	public static String getPlayer(final EMyPlayerPositionState positionState) {
		return PLAYER.get(positionState);
	}
	
	/**
	 * @param fortState Every node has a EMyFortState that can have different ENUM values.
	 * @return If my castle is there - 🏛, or if enemys castle - 🎪.
	 */
	public static String getCastle(final EMyFortState fortState) {
		return CASTLE.get(fortState);
	}
	
	/**
	 * @param playerState Each player has 4 states - Won, Lost, Act and Wait. Depending on his current 
	 * @return Depending on his current state - Won : 👑, Lost : 😱, Act : 🏃‍, Wait : ⏳. 
	 */
	public static String getState(final EMyPlayerGameState playerState) {
		return STATE.get(playerState);
	}
	
	/**
	 * @param hasCollectedTreasure Shows if a player has already found his treasure and is looking for the enemy castle.
	 * @return If already collected - ✔, if still looking for it - ❌.
	 */
	public static String getTreasure(final boolean hasCollectedTreasure) {
		return TREASURE.get(hasCollectedTreasure);
	}
	
}