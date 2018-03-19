package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class RGB10MM extends Fixture{
	protected static int RED=0, GREEN=1, BLUE=2, MACRO=3, STROB=4, MODE=5, DIM=6;

	public RGB10MM(int ch){
		super(ch);
		val = new int[]{0,0,0,0,0,0,0};
	}

	public RGB10MM color(PresetColor c,int dim){
		val[DIM] = dim;
		val[RED]=c.r;
		val[GREEN]=c.g;
		val[BLUE]=c.b;
		return this;
	}
	public RGB10MM music(){
		//TODO implement
		return this;
	}
	public String toString(){
		//TODO if MUSIC return "RGB10MM = MUSIC";
		if(val[MACRO]==0&&val[STROB]==0&&val[MODE]==0)
			return "RGB10MM R "+val(RED)+" G "+val(GREEN)+" B "+val(BLUE);
		else
			return "RGB10MM "+super.toString();
	}
	private int val(int ch){
		return val[ch]*val[DIM]/100;
	}
}
