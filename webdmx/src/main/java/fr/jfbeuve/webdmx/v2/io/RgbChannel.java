package fr.jfbeuve.webdmx.v2.io;

public class RgbChannel {
	private DmxChannel regular, override;
	private int channel, value;
	private long stroboSpeed=0;
	private long stroboTime=0;
	
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
		
		if(stroboSpeed>0){
			long time = System.currentTimeMillis()-stroboTime;
			if(time>stroboSpeed)
				output[channel]=0;
			if(time>stroboSpeed*2)
				stroboTime = time;
		}
		
		return isCompleted();
	}
	/**
	 * @return true if there is no more dmx change to propagate
	 */
	boolean isCompleted(){
		if(stroboSpeed>0) return false;
		return regular.isCompleted()&&(override==null||override.isCompleted());
	}
	void strob(long s){
		stroboSpeed = s;
	}
}
