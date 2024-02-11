package server.main.map;

import java.util.Objects;

public class Coordinate {

	private final int x;
	private final int y;
	
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
		final Coordinate other = (Coordinate) obj;
		return this.x == other.x && this.y == other.y;
	}

	@Override
	public String toString() {
		return "[X=" + x + ", Y=" + y + "]";
	}
	
}