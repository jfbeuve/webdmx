package fr.jfbeuve.webdmx.preset;

public enum PresetColor {
	RED(255,32,0),GREEN(0,255,32),BLUE(0,32,255),
	YELLOW(255,255,0),CYAN(0,255,255),VIOLET(255,0,255),ORANGE(255,127,0);
	public int r,g,b;
	private PresetColor(int _r, int _g, int _b){
		r=_r; g=_g; b=_b;
	}
	
}
