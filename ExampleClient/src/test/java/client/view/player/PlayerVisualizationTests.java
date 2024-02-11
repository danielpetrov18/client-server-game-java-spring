package client.view.player;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import client.player.Player;
import client.view.PlayerVisualization;
import client.player.EMyPlayerGameState;
import client.movement.coordinate.Coordinate;


public class PlayerVisualizationTests {
	
	// Data driven test.
	
	@ParameterizedTest
	@MethodSource("dataProvider")
	public void ModelDataUpdated_ClientViewNotified_CorrectDataDisplay(String myFn, String myLn, String uAc, EMyPlayerGameState pGs, int x, int y, 
			String myFn2, String myLn2, EMyPlayerGameState pGs2, int x2, int y2) {	
		final Player me = new Player();
		final Player enemy = new Player();
		
		me.register(uAc, myFn, myLn);	
		me.setPlayerState(pGs);
		me.setPosition(new Coordinate(x,y));
		
		enemy.register(uAc, myFn2, myLn2);
		enemy.setPlayerState(pGs2);
		enemy.setPosition(new Coordinate(x2,y2));
		
		final String actualStringRepresentation = PlayerVisualization.playersInfo(me, enemy);
		final String expectedStringRepresentation = new String("**********************************************************\n"
				+ "     Me: 👽  My Fort: 🏛      Enemy: 😈 Enemy Fort: 🎪\n"
				+ "     Name: Daniel Petrov      Name: Dummy Dumber\n"
				+ "     U:account: danielp29     U:account: danielp29\n"
				+ "     State: 🏃‍                State: ⏳\n"
				+ "     Collected treasure: ❌   Collected treasure: ❌\n"
				+ "         [X=2, Y=4]               [X=7, Y=4]\n");
		
		Assertions.assertEquals(actualStringRepresentation, expectedStringRepresentation);
		Assertions.assertEquals(expectedStringRepresentation.length(), actualStringRepresentation.length());
		
	}
	
	private static Stream<Arguments> dataProvider() {
        return Stream.of(
            Arguments.of("Daniel", "Petrov", "danielp29", EMyPlayerGameState.MUST_ACT, 2, 4, "Dummy", "Dumber", EMyPlayerGameState.MUST_WAIT, 7, 4)
        );
    }
	
}