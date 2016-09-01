package fr.jfbeuve.webdmx.fixture;

import java.util.ArrayList;
import java.util.Collection;

public enum RGBFixture{
	PAR1(24,FixtureType.RGB3), PAR2(27,FixtureType.RGB3), 
	PAR3(30,FixtureType.RGB3), PAR4(33,FixtureType.RGB3), 
	LEFT(11,FixtureType.RGB7);
	
	private int red, green, blue, strob, dim;
	
	private FixtureType type;
	
	private RGBFixture(int channel, FixtureType _type){
		type = _type;
		
		if(type!=FixtureType.PAR){
			red = channel;
			green = channel+1;
			blue = channel+2;
		}
		
		switch(type){
			case RGB3:
				channels(new int[]{red,green, blue});	
				break;
			case RGB7:
				strob = channel+4;
				dim = channel+6;
				channels(new int[]{red,green,blue, strob, dim});
				break;
			case PAR:
				dim = channel;
				channels(new int[]{dim});
				break;
			default:
		}
	}
	
	public int red(){return red;}
	public int green(){return green;}
	public int blue(){return blue;}
	public int strob(){return strob;}
	public int dim(){return dim;}
	public FixtureType type(){return type;}
	
	Collection<Integer> channels = new ArrayList<Integer>();
	public Collection<Integer> channels(){return channels;}

	protected void channels(int[] in){
		for (int i = 0; i < in.length; i++) {
			channels.add(in[i]);
		}
	}
}
