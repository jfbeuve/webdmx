package fr.jfbeuve.webdmx.preset;

public enum PresetColor {
	//	RED(255,0,0),GREEN(0,255,0),BLUE(0,0,255),
	//	YELLOW(255,255,0),CYAN(0,255,255),VIOLET(255,0,255),
	//	ORANGE(255,127,0),WHITE(255,255,255),BLACK(0,0,0);
	RED(255,38,0),GREEN(0,255,0),BLUE(4,51,255),
	YELLOW(255,251,0),CYAN(0,253,255),VIOLET(255,64,255),
	ORANGE(255,147,0),WHITE(255,255,255),BLACK(0,0,0);
	public int r,g,b;
	private PresetColor(int _r, int _g, int _b){
		r=_r; g=_g; b=_b;
	}
	
}
