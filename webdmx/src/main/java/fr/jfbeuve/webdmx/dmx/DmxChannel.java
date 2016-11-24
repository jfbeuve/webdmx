package fr.jfbeuve.webdmx.dmx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DmxChannel {
	private static final Log log = LogFactory.getLog(DmxChannel.class);
	
	private DmxLayer[] dmx = new DmxLayer[3];
	private int layer = 0;
	private int channel, value;
	
	public DmxChannel(int ch){
		channel=ch;
		
		dmx[0] = new DmxLayer(0,false);
		for(int i=1;i<dmx.length;i++) dmx[i]=null;
				
		value = 0;
	}
	
	/**
	 * @param v dmx value
	 * @param d dimmer %
	 * @param f fading time in ms
	 */
	public void set(int v, int d, boolean strob, long f){
		value = v;
		dmx[0].set(v*d/100, f);
		dmx[0].strob(strob);
	}
	/**
	 * reset a layer of override
	 * reset ALL override layers
	 */
	public void reset(int _layer){
		if(_layer==-1) {
			reset();
			return;
		}
		
		if(_layer<1||_layer>dmx.length) return;
		
		dmx[_layer]=null;
		while(dmx[layer]==null) layer--;
	}
	/**
	 * reset all layers of overrides
	 */
	private void reset(){
		for(int i=1;i<dmx.length;i++)
			dmx[i]=null;
		layer=0;
	}
	
	public void override(int v, int d, boolean strob, long f, int _layer){
		if(_layer<1||_layer>dmx.length)return;
		if(dmx[_layer]==null) dmx[_layer] = new DmxLayer(dmx[0].val(),strob);
		dmx[_layer].set((v<0?value:v)*d/100, f);
		if(layer<_layer)layer=_layer;
	}
	
	public boolean apply(int[] output, boolean strob, long timestamp){
		int was = output[channel];
		output[channel]=dmx[layer].next(strob, timestamp);
		if(was!=output[channel])
			log.info(channel+"="+output[channel]);
		return dmx[layer].done();
	}

}
