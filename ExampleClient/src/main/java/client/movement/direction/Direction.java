package client.movement.direction;

/**
 * <p>Encapsulates each movement the AI has to make and the needed rounds for that.</p>
 * <p>Used to know when to remove the direction from the movements and calculate a new one.</p>
 */
public class Direction {

	private int rounds;
	private EMyMove direction;
	
	/**
	 * @param rounds The rounds it takes to leave a node and enter a new one.
	 * @param direction The direction in which the AI is going to move.
	 */
	public Direction(final int rounds, final EMyMove direction) {
		this.rounds = rounds;
		this.direction = direction;
	}
	
	public EMyMove getDirection() {
		// Lower the round count at every access. 
		--(this.rounds);
		return this.direction;
	}
	
	public boolean isDirectionExhausted() {
		return this.rounds <= 0;
	}

	@Override
	public String toString() {
		return "[rounds=" + rounds + ", direction=" + direction + "]";
	}
	
}