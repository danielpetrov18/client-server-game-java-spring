package client.view;

import client.player.Player;
import client.map.fullmap.EMyFortState;
import client.view.emojis.VisualizationEmojis;
import client.map.fullmap.EMyPlayerPositionState;

/**
 * <p>This class creates a String to display information about both players.</p>
 */
public class PlayerVisualization {	

	/**
	 * @param me MyPlayerState object containing all the data related to my player.
	 * @param enemy MyPlayerState object containing all the data related to the enemy player. 
	 * @return Returns a string representation of all the relevant data about each player - State, position name and so on.
	 */
	public static String playersInfo(final Player me, final Player enemy) {
		final var result = new StringBuilder();
		for(int i = 1; i <= 58; ++i)
			result.append("*");
		
		result.append("\n");
		
		result.append("     Me: " + VisualizationEmojis.getPlayer(EMyPlayerPositionState.MY_PLAYER) + "  My Fort: " + VisualizationEmojis.getCastle(EMyFortState.MY_FORT));
		result.append("      Enemy: " + VisualizationEmojis.getPlayer(EMyPlayerPositionState.ENEMY_PLAYER) + " Enemy Fort: " + VisualizationEmojis.getCastle(EMyFortState.ENEMY_FORT) + "\n");
		
		result.append("     Name: " + me.getFirstname() + " " + me.getLastname());
		result.append("      Name: " + enemy.getFirstname() + " " + enemy.getLastname() + "\n");
		
		result.append("     U:account: " + me.getUAccount());
		result.append("     U:account: " + enemy.getUAccount() + "\n");
		
		result.append("     State: " + VisualizationEmojis.getState(me.getPossiblePlayerState().get()));	
		result.append("                State: " + VisualizationEmojis.getState(enemy.getPossiblePlayerState().get()) + "\n");
		
		result.append("     Collected treasure: " + VisualizationEmojis.getTreasure(me.hasCollectedTreasure()));
		result.append("   Collected treasure: " + VisualizationEmojis.getTreasure(enemy.hasCollectedTreasure()) + "\n");
		
		result.append("         " + me.getPositon());
		result.append("               " + enemy.getPositon() + "\n");
		
		return result.toString();
	}
	
}