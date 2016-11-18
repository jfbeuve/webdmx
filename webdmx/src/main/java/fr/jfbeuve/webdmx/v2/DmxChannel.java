package fr.jfbeuve.webdmx.v2;

public class DmxChannel {
	private DmxLayer[] dmx = new DmxLayer[3];
	private int layer = 0;
	private int channel, value;
	
	DmxChannel(int ch){
		channel=ch;
		
		dmx[0] = new DmxLayer(0);
		for(int i=1;i<dmx.length;i++) dmx[i]=null;
				
		value = 0;
	}
	
	/**
	 * @param v dmx value
	 * @param d dimmer %
	 * @param f fading time in ms
	 */
	void set(int v, int d, long f){
		value = v;
		dmx[0].set(v*d/100, f);
	}
	
	void reset(int _layer){
		if(_layer<1||_layer>dmx.length)return;
		dmx[_layer]=null;
		while(dmx[layer]==null) layer--;
	}
	
	void override(int v, int d, long f, int _layer){
		if(_layer<1||_layer>dmx.length)return;
		if(dmx[_layer]==null) dmx[_layer] = new DmxLayer(dmx[0].get());
		dmx[_layer].set((v<0?value:v)*d/100, f);
		if(layer<_layer)layer=_layer;
	}
	
	boolean apply(int[] output){
		output[channel]=dmx[layer].get();		
		return dmx[layer].isCompleted();
	}

}
