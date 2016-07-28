package fr.jfbeuve.webdmx.dmx;

import java.util.ArrayList;
import java.util.List;

public class DmxDimmer {
	public static final String MASTER = "master";
		
	private List<DmxChannel> channels=new ArrayList<DmxChannel>();
	private DmxWrapper dmx;
	private int value=255;
	
	public DmxDimmer(DmxWrapper _dmx, int[] _channels){
		dmx=_dmx;
		
		for (int i = 0; i < _channels.length; i++) {
			DmxChannel channel = new DmxChannel(_channels[i]);
			channel.setDimmer(this);
			channels.add(channel);
			dmx.set(channel);
		}
	}
	
	public int apply(int _value){
		return _value * value / 255; 
	}
	
	public void dim(int _value){
		value = _value;
		for (DmxChannel channel : channels) {
			channel.dim();
		}
	}
}
