package fr.jfbeuve.webdmx.dmx;

import java.util.ArrayList;
import java.util.List;

public class DmxChannel {
	private List<DmxDimmer> dimmers = new ArrayList<DmxDimmer>();
	private int value;
	private int channel;
	
	public DmxChannel(int _channel){
		channel = _channel;
	}
	
	/**
	 * add dimmer to the channel
	 */
	public void setDimmer(DmxDimmer dim){
		dimmers.add(dim);
	}
	/**
	 * @return value after dimmers
	 */
	public int dim(int _value){
		value = _value;
		int toReturn = value;
		
		for (DmxDimmer dim : dimmers) {
			toReturn = dim.apply(toReturn);
		}
		
		return toReturn;
	}
	/**
	 * @return dmx value before dimmer
	 */
	public int value(){
		return value;
	}
	/**
	 * return dmx value after dimmmer
	 */
	public int dim(){
		return dim(value);
	}
	
	public int channel(){
		return channel;
	}
}
