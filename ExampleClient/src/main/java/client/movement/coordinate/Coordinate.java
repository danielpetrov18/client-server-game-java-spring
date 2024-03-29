package client.movement.coordinate;

import java.util.Objects;

public class Coordinate {

	private final int x;
	private final int y;
	public final static int INVALID_COORDINATE = -99;
	
	/**
	 * @param x Column.
	 * @param y Row.
	 */ 
	public Coordinate(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	// hashCode + equals method are defined only for the sake of easier comparison of coordinates.
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "[X=" + x + ", Y=" + y + "]";
	}
	
}