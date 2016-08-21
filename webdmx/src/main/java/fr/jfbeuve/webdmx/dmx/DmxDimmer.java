package fr.jfbeuve.webdmx.dmx;

import java.util.ArrayList;
import java.util.List;

import fr.jfbeuve.webdmx.fixture.RGBFixture;

public enum DmxDimmer {
	MASTER(master());
	private List<Integer> channels;
	private int value = 255;
	private DmxDimmer(List<Integer> _channels){
		channels = _channels;
	}
	private static List<Integer> master(){
		ArrayList<Integer> toReturn = new ArrayList<Integer>();

		toReturn.add(17);
		toReturn.addAll(RGBFixture.PAR1.channels());
		toReturn.addAll(RGBFixture.PAR2.channels());
		toReturn.addAll(RGBFixture.PAR3.channels());
		toReturn.addAll(RGBFixture.PAR4.channels());
		
		return toReturn; 
	}
	public List<Integer> channels(){
		return channels;
	}
	public void value(int v){
		value = v;
	}
	public int value(){
		return value;
	}
}
