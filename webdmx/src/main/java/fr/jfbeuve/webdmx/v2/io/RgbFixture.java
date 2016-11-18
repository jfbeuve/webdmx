package fr.jfbeuve.webdmx.v2.io;

public class RgbFixture {
	private DmxChannel red, green, blue;
	
	RgbFixture(int channel){
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
	void set(SceneFixture f, long fade){
		red.set(f.r,f.dim, fade);
		green.set(f.g,f.dim, fade);
		blue.set(f.b,f.dim, fade);
		//TODO implement f.strob
	}
	
	/**
	 * set override for this fixture
	 */
	void override(SceneFixture f, long fade, int layer){
		red.override(f.r, f.dim, fade, layer);
		green.override(f.g, f.dim, fade, layer);
		blue.override(f.b, f.dim, fade, layer);
	}
	
	/**
	 * reset override for this fixture
	 */
	void reset(int layer){
		red.reset(layer);
		green.reset(layer);
		blue.reset(layer);
	}
	
	/**
	 * Applies dmx values
	 * @return true if all fading completed 
	 */
	boolean apply(int[] output){
		boolean done = true;
		if(!red.apply(output)) done = false;
		if(!green.apply(output)) done = false;
		if(!blue.apply(output)) done = false;
				
		return done;
	}
}
