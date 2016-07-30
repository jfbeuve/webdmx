package fr.jfbeuve.webdmx.show;

public class RGBColor {
	static final public RGBColor ROUGE = new RGBColor(255, 0, 0);
	static final public RGBColor VERT = new RGBColor(0, 255, 0);
	static final public RGBColor BLEU = new RGBColor(0, 0, 255);
	static final public RGBColor JAUNE = new RGBColor(255, 255, 0);
	static final public RGBColor MAUVE = new RGBColor(255, 0, 255);
	static final public RGBColor CYAN = new RGBColor(0, 255, 255);
	static final public RGBColor AMBRE = new RGBColor(255, 127, 0);
	
	int red, green, blue;

	public RGBColor(int red, int green, int blue) {
		super();
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
