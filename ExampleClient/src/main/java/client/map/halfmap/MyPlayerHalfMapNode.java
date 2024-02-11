package client.map.halfmap;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.EMyTerrain;
import client.movement.coordinate.Coordinate;
import client.exception.unchecked.InvalidPlayerNodeCoordinateValuesException;

public class MyPlayerHalfMapNode {

	private final static int MIN_X = 0;
	private final static int MIN_Y = 0;
	private final EMyTerrain terrain;
	private final boolean fortPresent;
	private final Coordinate coordinate;
	private final static Logger logger = LoggerFactory.getLogger(MyPlayerHalfMapNode.class);
	
	/**
	 * @param coordinate X and Y value.
	 * @param terrain Type of field.
	 * @param fortPresent True or false based on if a castle is present on a node.
	 * @param maxX The maximum column value.
	 * @param maxY The maximum row value.
	 * @throws InvalidPlayerNodeCoordinateValuesException If an invalid coordinate value is passed over the ctor.
	 */
	public MyPlayerHalfMapNode(final Coordinate coordinate, final EMyTerrain terrain, final boolean fortPresent, final int maxX, final int maxY) {
		// Check to see if correct coordinates have been passed.
		if(coordinate.getX() >= MIN_X && coordinate.getX() <= maxX && coordinate.getY() >= MIN_Y && coordinate.getY() <= maxY) {
			this.coordinate  = coordinate;
			this.terrain = terrain;
			this.fortPresent = fortPresent;
		}
		else {
			logger.warn("Invalid coordinate was passed in ctor. X={}, Y={}", coordinate.getX(), coordinate.getY());
			final String msg = "Invalid coordinate: " + coordinate + " ...";
			throw new InvalidPlayerNodeCoordinateValuesException(msg);
		}
	}
	
	public int getX() {
		return this.coordinate.getX();
	}
	
	public int getY() {
		return this.coordinate.getY();
	}
	
	public EMyTerrain getTerrain() {
		return this.terrain;
	}
	
	public boolean isFortPresent() {
		return this.fortPresent;
	}

	public Coordinate getPosition() {
		return this.coordinate;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(coordinate, fortPresent, terrain);
	}

	/**
	 *@return Equality based on position && terrain type && presence of the fort. 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		final MyPlayerHalfMapNode other = (MyPlayerHalfMapNode) obj;
		return Objects.equals(coordinate, other.coordinate) && fortPresent == other.fortPresent && terrain == other.terrain;
	}

}