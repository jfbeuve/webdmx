package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class RGBEuroliteBP extends Fixture {

	protected static int RED=0, GREEN=1, BLUE=2, STROB=4, DIM=3;

	public RGBEuroliteBP(int ch){
		super(ch);
		val = new int[]{0,0,0,0,0};
	}

	public RGBEuroliteBP color(PresetColor c,int dim){
		val[DIM] = dim;
		val[RED]=c.r;
		val[GREEN]=c.g;
		val[BLUE]=c.b;
		return this;
	}
	public RGBEuroliteBP music(){
		//TODO implement
		return this;
	}
	public String toString(){
		String id = "EUR";
		if(ch==55) id="EUR1";
		if(ch==60) id="EUR2";
		if(ch==65) id="EUR3";
		//TODO if MUSIC return "RGB10MM = MUSIC";
		return id+" R "+val(RED)+" G "+val(GREEN)+" B "+val(BLUE)+(val[STROB]>0?" S":"");
	}
	private int val(int ch){
		return val[ch]*val[DIM]/100;
	}

}
