package client.converter;

import client.map.EMyTerrain;
import client.map.fullmap.EMyFortState;
import client.player.EMyPlayerGameState;
import client.movement.direction.EMyMove;
import client.map.fullmap.EMyTreasureState;
import client.map.fullmap.EMyPlayerPositionState;

import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;

public class NetworkDataEnumConverter {

	/**
	 * @param pState ENUM object as defined in the network protocol.
	 * @return Converted to ENUM defined by me.
	 */
	public static EMyPlayerGameState determineMyPlayerGameState(final EPlayerGameState pState) {
		switch(pState) {
			case MustAct:
				return EMyPlayerGameState.MUST_ACT;
			case MustWait:
				return EMyPlayerGameState.MUST_WAIT;
			case Won: 
				return EMyPlayerGameState.WON;
			case Lost:
				return EMyPlayerGameState.LOST;
			default:
				return null;
		}
	}
	
	/**
	 * @param treasureState TreasureState ENUM as received by the protocol.
	 * @return Converted to my own EMyTreasureState ENUM.
	 */
	public static EMyTreasureState determineTreasureState(final ETreasureState treasureState) {
		switch(treasureState) {
			case MyTreasureIsPresent:
				return EMyTreasureState.TREASURE_PRESENT;
			case NoOrUnknownTreasureState:
				return EMyTreasureState.NO_OR_UNKNOWN;
			default:
				return null;
		}
	}
	
	/**
	 * @param playerPositionState EPlayerPositionState related to a node received from the network.
	 * @return Converted to my custom EMyPlayerPositionState
	 */
	public static EMyPlayerPositionState determineMyPlayerPositionState(final EPlayerPositionState playerPositionState) {
		switch(playerPositionState) {
			case BothPlayerPosition: 
				return EMyPlayerPositionState.BOTH_PLAYERS;
			case EnemyPlayerPosition: 
				return EMyPlayerPositionState.ENEMY_PLAYER;
			case MyPlayerPosition:
				return EMyPlayerPositionState.MY_PLAYER;
			case NoPlayerPresent:
				return EMyPlayerPositionState.NO_PLAYER;
			default: 
				return null;
		}
	}
	
	/**
	 * @param fortState Fortstate ENUM received from a node by the network. 
	 * @return Converted to my EMyFortState.
	 */
	public static EMyFortState determineMyFortState(final EFortState fortState) {
		switch(fortState) {
			case EnemyFortPresent: 
				return EMyFortState.ENEMY_FORT;
			case MyFortPresent: 
				return EMyFortState.MY_FORT;
			case NoOrUnknownFortState:
				return EMyFortState.NO_OR_UNKNOWN;
			default: 
				return null;
		}
	}
	
	/**
	 * @param terrain Terrain received by a node from the network.
	 * @return Converted ETerrain to EMyTerrain.
	 */
	public static EMyTerrain determineMyTerrain(final ETerrain terrain) {
		switch(terrain) {
			case Grass:
				return EMyTerrain.GRASS;
			case Water:
				return EMyTerrain.WATER;
			case Mountain:
				return EMyTerrain.MOUNTAIN;
			default:
				return null;
		}
	} 
	
	/**
	 * @param terrain Custom defined ENUM.
	 * @return Returns a ETerrain as specified in the network protocol.
	 */
	public static ETerrain determineNetworkTerrain(final EMyTerrain terrain) {
		switch(terrain) {
			case GRASS:
				return ETerrain.Grass;
			case WATER:
				return ETerrain.Water;
			case MOUNTAIN:
				return ETerrain.Mountain;
			default:
				return null;
		}
	}
	
	/**
	 * @param move Custom defined ENUM.
	 * @return Returns an ENUM as specified in the network protocol.
	 */
	public static EMove determineNetworkMove(final EMyMove move) {
		switch(move) {
			case UP:
				return EMove.Up;
			case DOWN:
				return EMove.Down;
			case LEFT:
				return EMove.Left;
			case RIGHT:
				return EMove.Right;		
			default:
				return null;
		}
	}
	
}