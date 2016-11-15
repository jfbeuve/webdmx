package fr.jfbeuve.webdmx.v2.io;

public class RgbFixture {
	private DmxChannel redChannel, greenChannel, blueChannel;
	private int redChannelId, greenChannelId, blueChannelId;
	private int redVal,greenVal,blueVal,dimmer;
	
	RgbFixture(int channel){
		redChannelId = channel;
		greenChannelId = channel + 1;
		blueChannelId = channel + 2;
		redChannel = new DmxChannel();
		greenChannel = new DmxChannel();
		blueChannel = new DmxChannel();
		redVal = 0;
		greenVal = 0;
		blueVal = 0;
		dimmer = 0;
	}
	/**
	 * @param DMX RED 0-255
	 * @param DMX GREEN 0-255
	 * @param DMX BLUE 0-255
	 * @param DIMMER 0-100
	 * @param FADE TIME IN MS
	 */
	void set(int r,int g, int b, int dim, long time){
		redVal = r;
		greenVal = g;
		blueVal = b;
		dim(dim,time);
	}
	/**
	 * @param DMX RED 0-255
	 * @param DMX GREEN 0-255
	 * @param DMX BLUE 0-255
	 * @param FADE TIME IN MS
	 */
	void color(int r,int g, int b, long time){
		redVal = r;
		greenVal = g;
		blueVal = b;
		dim(dimmer,time);
	}
	/**
	 * @param DIMMER 0-100
	 * @param FADE TIME IN MS
	 */	
	void dim(int dim, long time){
		dimmer = dim;
		redChannel.set(redVal*dim/100, time);
		greenChannel.set(greenVal*dim/100, time);
		blueChannel.set(blueVal*dim/100, time);
		completed = false;
	}
	
	private boolean completed = true;
	/**
	 * Applies dmx values
	 * @return true if no change 
	 */
	boolean apply(int[] output){
		if(completed) return true;
		output[redChannelId]=redChannel.get();
		output[greenChannelId]=greenChannel.get();
		output[blueChannelId]=blueChannel.get();
		if(redChannel.isCompleted()&&greenChannel.isCompleted()&&blueChannel.isCompleted()) completed = true;
		return false;
	}
}
