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
	public void reset(int _layer,long time){
		int val = dmx[layer].val(); 
		if(_layer==-1) {
			// reset all layers
			for(int i=1;i<dmx.length;i++)
				dmx[i]=null;
			layer=0;
		}else{
			// reset one layer
			if(_layer<1||_layer>dmx.length) return;
			if(dmx[_layer]==null) return;
			dmx[_layer]=null;
			while(dmx[layer]==null) layer--;
		}
		dmx[layer].reset(val,time);
	}
	
	public void override(int v, int d, boolean strob, long f, int _layer){
		if(_layer<1||_layer>dmx.length)return;
		if(dmx[_layer]==null) dmx[_layer] = new DmxLayer(dmx[0].val(),strob);
		dmx[_layer].set((v<0?value:v)*d/100, f);
		dmx[_layer].strob(strob);
		if(layer<_layer)layer=_layer;
	}
	
	public DmxChannelStatus apply(int[] output, boolean strob, long timestamp){
		int was = output[channel-1];
		output[channel-1]=dmx[layer].next(strob, timestamp);
		if(was!=output[channel-1])
			log.info(channel+"="+output[channel-1]);
		return dmx[layer].done();
	}

}
