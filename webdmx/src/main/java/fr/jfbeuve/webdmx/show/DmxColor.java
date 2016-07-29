package fr.jfbeuve.webdmx.show;

public class DmxColor {
	static final DmxColor ROUGE = new DmxColor(255, 0, 0);
	static final DmxColor VERT = new DmxColor(0, 255, 0);
	static final DmxColor BLEU = new DmxColor(0, 0, 255);
	static final DmxColor JAUNE = new DmxColor(255, 255, 0);
	static final DmxColor MAUVE = new DmxColor(255, 0, 255);
	static final DmxColor CYAN = new DmxColor(0, 255, 255);
	static final DmxColor AMBRE = new DmxColor(255, 127, 0);
	
	int red, green, blue;

	public DmxColor(int red, int green, int blue) {
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
