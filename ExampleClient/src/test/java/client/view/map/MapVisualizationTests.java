package client.view.map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Set;

import org.junit.Test;

import client.player.Player;
import client.map.EMyTerrain;
import client.view.color.EColor;
import client.map.fullmap.MyFullMap;
import client.view.MapVisualization;
import client.map.fullmap.EMyFortState;
import client.map.fullmap.MyFullMapNode;
import client.map.fullmap.EMyTreasureState;
import client.movement.coordinate.Coordinate;
import client.view.emojis.VisualizationEmojis;
import client.map.fullmap.EMyPlayerPositionState;

public class MapVisualizationTests {

	@Test
	public void DataInTheModelUpdated_ViewNotifiedByModel_ViewDisplaysCorrectData() {
		final Player p = new Player();
		p.setPosition(new Coordinate(2,2));

		// Creating a smaller version of full map.
		final MyFullMapNode n1 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, EMyFortState.NO_OR_UNKNOWN, new Coordinate(0,0));
		final MyFullMapNode n2 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.TREASURE_PRESENT, EMyFortState.NO_OR_UNKNOWN, new Coordinate(0,1));
		final MyFullMapNode n3 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, EMyFortState.MY_FORT, new Coordinate(0,2));
		final MyFullMapNode n4 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, EMyFortState.ENEMY_FORT, new Coordinate(1,0));
		final MyFullMapNode n5 = new MyFullMapNode(EMyTerrain.MOUNTAIN, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, EMyFortState.NO_OR_UNKNOWN, new Coordinate(1,1));
		final MyFullMapNode n6 = new MyFullMapNode(EMyTerrain.WATER, EMyPlayerPositionState.NO_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, EMyFortState.NO_OR_UNKNOWN, new Coordinate(1,2));
		final MyFullMapNode n7 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.BOTH_PLAYERS, EMyTreasureState.NO_OR_UNKNOWN, EMyFortState.NO_OR_UNKNOWN, new Coordinate(2,0));
		final MyFullMapNode n8 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.ENEMY_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, EMyFortState.NO_OR_UNKNOWN, new Coordinate(2,1));
		final MyFullMapNode n9 = new MyFullMapNode(EMyTerrain.GRASS, EMyPlayerPositionState.MY_PLAYER, EMyTreasureState.NO_OR_UNKNOWN, EMyFortState.NO_OR_UNKNOWN, new Coordinate(2,2));
		final MyFullMap map = new MyFullMap(Set.of(n1,n2,n3,n4,n5,n6,n7,n8,n9));
		
		final String actualString = MapVisualization.mapString(map.getMapNodes(), p);
		
		final StringBuilder expectedString = new StringBuilder();
		expectedString.append(EColor.GREEN_BG + "" + EColor.WHITE + " # " + EColor.RESET);
		expectedString.append(VisualizationEmojis.getCastle(EMyFortState.ENEMY_FORT) + " ");
		expectedString.append(EColor.CYAN_BG + VisualizationEmojis.getPlayer(EMyPlayerPositionState.BOTH_PLAYERS) + " " + EColor.RESET + "\n");
		expectedString.append(EColor.YELLOW_BG + "" + EColor.WHITE + " $ " + EColor.RESET);
		expectedString.append(EColor.BLACK_BG + "" + EColor.WHITE + " # "  + EColor.RESET);
		expectedString.append(EColor.RED_BG + VisualizationEmojis.getPlayer(EMyPlayerPositionState.ENEMY_PLAYER) + " " + EColor.RESET + "\n");
		expectedString.append(VisualizationEmojis.getCastle(EMyFortState.MY_FORT) + " ");
		expectedString.append(EColor.BLUE_BG + "" + EColor.WHITE + " # " + EColor.RESET);
		expectedString.append(EColor.PURPLE_BG + VisualizationEmojis.getPlayer(EMyPlayerPositionState.MY_PLAYER) + " " + EColor.RESET + "\n");
		
		// Check if both the length and the String is completely equal.
		assertThat(expectedString.toString(), is(equalTo(actualString)));
		assertThat(expectedString.toString().length(), is(equalTo(actualString.length())));		
	}
	
}