package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class DICRO extends Fixture{
	private static final int BLUE=0,RED=1,GREEN=2,AMBER=3;
	public DICRO(int ch){
		super(ch);
		val = new int[]{0,0,0,0};
	}
	public DICRO color(PresetColor c, int dim){
		if(c==PresetColor.YELLOW){
			val[RED]=0;
			val[GREEN]=0;
			val[BLUE]=0;
			val[AMBER]=255*dim/100;
		}else if(c==PresetColor.ORANGE){
			val[RED]=127*dim/100;
			val[GREEN]=0;
			val[BLUE]=0;
			val[AMBER]=255*dim/100;
		}else{
			val[RED]=c.r*dim/100;
			val[GREEN]=c.g*dim/100;
			val[BLUE]=c.b*dim/100;
			val[AMBER]=0;
		}
		if(c==PresetColor.RED){
			val[GREEN]=0;
		}
		if(c==PresetColor.GREEN){
			val[BLUE]=0;
		}
		if(c==PresetColor.BLUE){
			val[GREEN]=0;
		}
		return this;
	}

	public String toString(){
		return "DICRO R "+val[RED]+" G "+val[GREEN]+" B "+val[BLUE]+" A "+val[AMBER];
	}
}
