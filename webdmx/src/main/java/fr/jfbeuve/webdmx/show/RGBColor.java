package fr.jfbeuve.webdmx.show;

public enum RGBColor {
	ROUGE(255, 0, 0), VERT(0, 255, 0), BLEU(0, 0, 255),
	
	MAUVE(255, 0, 255), CYAN(0, 255, 255),
	
	JAUNE(255, 255, 0), AMBRE(255, 127, 0),
	
	BLACK(0, 0, 0), WHITE(255, 255, 255), AUTO(-1,-1,-1);
	
	int red, green, blue;

	private RGBColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public int red(){
		return red;
	}
	public int green(){
		return green;
	}
	public int blue(){
		return blue;
	}
}
