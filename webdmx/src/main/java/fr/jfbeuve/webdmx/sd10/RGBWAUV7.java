package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class RGBWAUV7 extends Fixture {

	protected static int RED=1, GREEN=2, BLUE=3, STROB=7, DIM=0, WHITE=4, AMBER=5, UV=6;

	public RGBWAUV7(int ch){
		super(ch);
		val = new int[]{0,0,0,0,0,0,0,0,0,0};
	}

	public RGBWAUV7 color(PresetColor c, int dim){
		val[DIM] = dim*255/100;
		val[RED]=c.r>100?c.r:0;
		val[GREEN]=c.g>100?c.g:0;
		val[BLUE]=c.b>100?c.b:0;
		if(c.equals(PresetColor.ORANGE)) {
			val[AMBER] = 255;
			val[RED] = 0;
			val[GREEN] = 0;
		} else val[AMBER] = 0;
		if(c.equals(PresetColor.WHITE)){
			val[WHITE] = 255;
			val[RED] = 0;
			val[GREEN] = 0;
			val[BLUE] = 0;
		} else val[WHITE] = 0;
		return this;
	}
	public RGBWAUV7 strob(boolean fire){
		val[STROB]=(fire?255:0);
		return this;
	}
	public String toString(){
		String id = "RGBWAUV7";
		if(ch==70) id="RGBWAUV LEFT";
		if(ch==80) id="RGBWAUV DRUM LEFT";
		if(ch==90) id="RGBWAUV DRUM RIGHT";
		if(ch==100) id="RGBWAUV RIGHT ";
		return id+"["+val[0]+","+val[1]+","+val[2]+","+val[3]+","+val[4]+","+val[5]+","+val[6]+","+val[7]+","+val[8]+","+val[9]+"]";
	}
	private int val(int ch){
		return val[ch];
	}

}
