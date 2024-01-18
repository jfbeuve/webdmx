package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class RGB8W extends Fixture{
	private static final int RED=1,GREEN=2,BLUE=3,MACRO=0,SPEED=4;
	public RGB8W(int ch){
		super(ch);
		val = new int[]{0,0,0,0,0};
	}
	public RGB8W color(PresetColor c,int dim){
		val[RED]=c.r*dim/100;
		val[GREEN]=c.g*dim/100;
		val[BLUE]=c.b*dim/100;
		return this;
	}

	@Override
	public Fixture strob(boolean fire) {
		return this;
	}

	public RGB8W color(int red, int green, int blue){
		val[RED]=red;
		val[GREEN]=green;
		val[BLUE]=blue;
		return this;
	}
	public RGB8W music(){
		val[MACRO]=255;
		val[SPEED]=255;
		return this;
	}
	public String toString(){
		if(val[MACRO]==0&&val[SPEED]==0)
			return "RGB"+(1+ch/5)+" R "+val[RED]+" G "+val[GREEN]+" B "+val[BLUE];
		else if(val[MACRO]==0&&val[SPEED]==0)
			return "RGB"+(1+ch/5)+" MUSIC";
		else
			return "RGB"+(1+ch/5)+" "+super.toString();
	}
}
