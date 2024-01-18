package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class STROB extends Fixture{
	private static final int  SPEED=0,DIM=1;
	public STROB(int ch){
		super(ch);
		val = new int[]{0,0};
	}
	public STROB set(){
		val[DIM] = 255;
		val[SPEED] = 230; //was 225
		return this;
	}
	public String toString(){
		return "STROB DIM "+val[DIM]+" SPEED "+val[SPEED];
	}

	@Override
	public Fixture color(PresetColor c, int dim) {
		return this;
	}

	@Override
	public Fixture strob(boolean fire) {
		return this;
	}
}
