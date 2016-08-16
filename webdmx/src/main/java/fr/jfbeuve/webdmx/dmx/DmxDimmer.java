package fr.jfbeuve.webdmx.dmx;

import java.util.ArrayList;
import java.util.List;

import fr.jfbeuve.webdmx.fixture.RGBFixture;

public class DmxDimmer {
	public static final String MASTER = "master";
		
	private List<DmxChannel> channels=new ArrayList<DmxChannel>();
	private DmxWrapper dmx;
	private int value=255;
	
	public DmxDimmer(DmxWrapper _dmx, ArrayList<Integer> _channels){
		dmx=_dmx;
		
		for (Integer i : _channels) {
			DmxChannel channel = new DmxChannel(i);
			channel.setDimmer(this);
			channels.add(channel);
			dmx.init(channel);
		}
	}
	
	public int apply(int _value){
		return _value * value / 255; 
	}
	
	public void dim(int _value){
		value = _value;
		dmx.refresh(channels);
	}
	public static DmxDimmer getMaster(DmxWrapper dmx){
		ArrayList<Integer> channels = new ArrayList<Integer>();
		channels.add(17);
		channels.addAll(RGBFixture.PAR1.channels());
		channels.addAll(RGBFixture.PAR2.channels());
		channels.addAll(RGBFixture.PAR3.channels());
		channels.addAll(RGBFixture.PAR4.channels());
		return new DmxDimmer(dmx, channels);
	}
}
