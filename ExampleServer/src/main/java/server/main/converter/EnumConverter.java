package server.main.converter;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EPlayerGameState;

import server.main.map.EMyTerrain;
import server.main.player.EPlayerState;
import server.exceptions.converter.ConversionInvalidTerrainEnumException;
import server.exceptions.converter.ConversionInvalidPlayerStateEnumException;

public class EnumConverter {

	public static EMyTerrain determineMyTerrain(final ETerrain networkTerrain) {
		switch (networkTerrain) {
			case Grass:
				return EMyTerrain.Grass;
			case Mountain:
				return EMyTerrain.Mountain;
			case Water:
				return EMyTerrain.Water;
			default:
				final String errMsg = "ETerrain with value: [" + networkTerrain + "] cannot be converted!";
				throw new ConversionInvalidTerrainEnumException("ConversionInvalidTerrainEnumException", errMsg);
		}
	}
	
	public static ETerrain determineNetworkTerrain(final EMyTerrain myTerrain) {
		switch (myTerrain) {
			case Grass:
				return ETerrain.Grass;
			case Mountain:
				return ETerrain.Mountain;
			case Water:
				return ETerrain.Water;
			default:
				final String errMsg = "EMyTerrain with value: [" + myTerrain + "] cannot be converted!";
				throw new ConversionInvalidTerrainEnumException("ConversionInvalidTerrainEnumException", errMsg);
		}
	}
	
	public static EPlayerGameState determineNetworkGameState(final EPlayerState myState) {
		switch(myState) {
			case MustWait:
				return EPlayerGameState.MustWait;
			case MustAct:
				return EPlayerGameState.MustAct;
			case Won:
				return EPlayerGameState.Won;
			case Lost:
				return EPlayerGameState.Lost;
			default:
				final String errMsg = "EPlayerState with value: [" + myState + "] cannot be converted!";
				throw new ConversionInvalidPlayerStateEnumException("ConversionInvalidPlayerStateEnumException", errMsg);
		}
	}
	
}