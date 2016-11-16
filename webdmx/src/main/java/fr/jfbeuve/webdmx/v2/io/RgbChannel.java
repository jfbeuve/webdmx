package fr.jfbeuve.webdmx.v2.io;

public class RgbChannel {
	private DmxChannel regular, override;
	private int channel, value;
	
	RgbChannel(int ch){
		channel=ch;
		regular = new DmxChannel(0);
		value = 0;
		override=null;
	}
	
	/**
	 * @param v dmx value
	 * @param d dimmer %
	 * @param f fading time in ms
	 */
	void set(int v, int d, long f){
		value = v;
		regular.set(v*d/100, f);
	}
	
	void reset(){
		override=null;
	}
	
	void override(int v, int d, long f){
		if(override==null) 
			override = new DmxChannel(regular.get()); 
		
		override.set((v<0?value:v)*d/100, f);
	}
	boolean apply(int[] output){
		output[channel]=regular.get();
		if(override!=null) output[channel]=override.get();
		return isCompleted();
	}
	boolean isCompleted(){
		return regular.isCompleted()&&(override==null||override.isCompleted());
	}
}
