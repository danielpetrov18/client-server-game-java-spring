package client.map.halfmap;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

public class MyPlayerHalfMap {

	public final static int ROWS = 5;
	public final static int COLS = 10;
	private final Set<MyPlayerHalfMapNode> nodes;
	
	public MyPlayerHalfMap(Collection<MyPlayerHalfMapNode> nodes) {
		// First initialize it otherwise NullPointerException.
		this.nodes = new HashSet<>();
		this.nodes.addAll(nodes);
	}
	
	public Set<MyPlayerHalfMapNode> getMapNodes() {
		return this.nodes;
	}
	
}