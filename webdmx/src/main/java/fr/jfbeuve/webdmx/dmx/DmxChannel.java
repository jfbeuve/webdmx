package fr.jfbeuve.webdmx.dmx;


public class DmxChannel {
	private int value;
	private int channel;
	
	public DmxChannel(int _channel){
		channel = _channel;
	}
	
	/**
	 * @return new value after dimmers
	 */
	public int value(int _value){
		value = _value;
		if(value==0) return 0;
		int toReturn = value;
		
		DmxDimmer[] dimmers = DmxDimmer.values();
		for(int i=0;i<dimmers.length;i++){
			if(dimmers[i].channels().contains(channel))
				toReturn = toReturn * dimmers[i].value() / 255;
		}
		
		return toReturn;
	}
	/**
	 * @return current value after dimmer
	 */
	public int value(){
		return value(true);
	}
	
	/**
	 * @return current value after dimmer
	 * @param dim after dimmer if true, before dimmer if false
	 */
	public int value(boolean dim){
		if(dim)
			return value(value);
		else 
			return value;
	}
	
	public int channel(){
		return channel;
	}
}
