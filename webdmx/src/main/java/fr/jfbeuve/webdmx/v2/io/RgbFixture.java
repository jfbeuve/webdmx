package fr.jfbeuve.webdmx.v2.io;

public class RgbFixture {
	private RgbChannel red, green, blue;
	
	RgbFixture(int channel){
		red = new RgbChannel(channel);
		green = new RgbChannel(channel+1);
		blue = new RgbChannel(channel+2);
	}
	/**
	 * set dmx values to this fixture
	 * @param DMX RED 0-255
	 * @param DMX GREEN 0-255
	 * @param DMX BLUE 0-255
	 * @param DIMMER 0-100
	 * @param FADE TIME IN MS
	 */
	void set(SceneFixture f){
		red.set(f.r,f.dim, f.fade);
		green.set(f.g,f.dim, f.fade);
		blue.set(f.b,f.dim, f.fade);
		//TODO implement f.strob
	}
	
	/**
	 * set override for this fixture
	 */
	void override(SceneFixture f){
		red.override(f.r, f.dim, f.fade);
		green.override(f.g, f.dim, f.fade);
		blue.override(f.b, f.dim, f.fade);
	}
	
	/**
	 * reset override for this fixture
	 */
	void reset(){
		red.reset();
		green.reset();
		blue.reset();
	}
	
	/**
	 * Applies dmx values
	 * @return true if all fading completed 
	 */
	boolean apply(int[] output){
		red.apply(output);
		green.apply(output);
		blue.apply(output);
				
		if(	red.isCompleted()&&green.isCompleted()&&blue.isCompleted()) 
			return true;
		else 
			return false;
	}
}
