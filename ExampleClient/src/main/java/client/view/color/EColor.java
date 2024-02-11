package client.view.color;

public enum EColor {
	RESET ("\u001B[0m"),
	WHITE ("\u001B[37m"),
	RED_BG ("\u001B[41m"),
	BLUE_BG ("\u001B[44m"),
	CYAN_BG ("\u001B[46m"),
	BLACK_BG ("\u001B[40m"),
	GREEN_BG ("\u001B[42m"),
	YELLOW_BG ("\u001B[43m"),
	PURPLE_BG ("\u001B[45m");
	
	private final String colorUnicode;       

	private EColor(final String unicode) {
		this.colorUnicode = unicode;
	}
 
	@Override
	public String toString() {
		return this.colorUnicode;
	}
	
}