package fr.jfbeuve.webdmx.fixture;

import fr.jfbeuve.webdmx.dmx.DmxChannel;
import fr.jfbeuve.webdmx.sc.FixtureState;

public class RGBFixture {
	private DmxChannel red, green, blue;
	
	public RGBFixture(int channel){
		red = new DmxChannel(channel);
		green = new DmxChannel(channel+1);
		blue = new DmxChannel(channel+2);
	}
	/**
	 * set dmx values to this fixture
	 * @param DMX RED 0-255
	 * @param DMX GREEN 0-255
	 * @param DMX BLUE 0-255
	 * @param DIMMER 0-100
	 * @param FADE TIME IN MS
	 */
	public void set(FixtureState f, long fade){
		red.set(f.r,f.dim, f.strob, fade);
		green.set(f.g,f.dim, f.strob, fade);
		blue.set(f.b,f.dim, f.strob, fade);
	}
	
	/**
	 * set override for this fixture
	 */
	public void override(FixtureState f, long fade, int layer){
		red.override(f.r, f.dim, f.strob, fade, layer);
		green.override(f.g, f.dim, f.strob, fade, layer);
		blue.override(f.b, f.dim, f.strob, fade, layer);
	}
	
	/**
	 * reset override for this fixture
	 */
	public void reset(int layer){
		red.reset(layer);
		green.reset(layer);
		blue.reset(layer);
	}
		
	/**
	 * Applies dmx values
	 * @return true if all fading completed 
	 */
	public boolean apply(int[] output, boolean strob){
		boolean done = true;
		if(!red.apply(output, strob)) done = false;
		if(!green.apply(output, strob)) done = false;
		if(!blue.apply(output, strob)) done = false;
				
		return done;
	}
}
