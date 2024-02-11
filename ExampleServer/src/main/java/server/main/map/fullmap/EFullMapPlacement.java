package server.main.map.fullmap;

/*
 * Depending on the shape of the map the half map can be placed differently.
 * 
 * For example: If one has rectangular map (5x20) one half map is placed on the left side and the other on the right side.
 */
public enum EFullMapPlacement {
	Left,
	Right,
	Top,
	Bottom,
	Undefined
}