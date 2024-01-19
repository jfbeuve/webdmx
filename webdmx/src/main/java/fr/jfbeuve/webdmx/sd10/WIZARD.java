package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class WIZARD extends Fixture{
	private static final int SHUTTER=0,DIMMER=1,COLOR=2,GOBO=3,REFLECTOR=4,PAN=5,TILT=6,SETTINGS=7,FX=8,SPEED=9;
	public WIZARD(int ch){
		super(ch);
		val = new int[]{0,0,0,0,0,0,0,0,0,0};
	}
	public WIZARD disco(){
		val = new int[]{0,0,0,0,0,0,0,0,133,60};
		return this;
	}
	public WIZARD slow(){
		val = new int[]{255,255,75,30,0,0,0,0,137,11};
		return this;
	}
	public String toString(){
		return "WIZARD "+super.toString();
	}

}
