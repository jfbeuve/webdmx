package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.juanjo.openDmx.OpenDmx;

@Component
public class DmxWrapper {
	private boolean open;
	private Map<Integer,DmxChannel> output = new HashMap<Integer, DmxChannel>();
	private static final Logger logger = Logger.getLogger(DmxWrapper.class.getCanonicalName());
	private Map<String,DmxDimmer> dimmers = new HashMap<String, DmxDimmer>();

	public DmxWrapper(){
		dimmers.put(DmxDimmer.MASTER, new DmxDimmer(this, new int[]{17}));
	}
	
	@Value("${offline:false}")
	private boolean offline;
	
	/**
	 * set offline mode
	 */
	public void offline() {
		offline = true;
	}

	/**
	 * Apply DMX value
	 */
	public void set(int _channel, int value){
		logger.fine("offline = "+offline);
		
		DmxChannel channel = get(_channel);
		int toSet = channel.dim(value);
		
		if(!open&&!offline) open = OpenDmx.connect(OpenDmx.OPENDMX_TX);
		if(!open&&!offline) logger.severe("Open Dmx widget not detected!");
		if(!offline) OpenDmx.setValue(_channel-1,toSet);
		
		logger.info("OpenDmx.setValue("+_channel+","+toSet+")");
	}
	
	public void refresh(DmxChannel channel){
		set(channel.channel(),channel.value());
	}
	
	/**
	 * Initialize channel with dimmer
	 */
	public void init(DmxChannel channel){
		output.put(channel.channel(), channel);
	}
		
	public void disconnect(){
		if(open&&!offline) OpenDmx.disconnect();
	}
	
	/**
	 * get dimmer by name and apply new value
	 */
	public void dim(String name, int value){
		dimmers.get(name).dim(value);
	}
	
	public DmxChannel get(int channelid){
		DmxChannel channel = output.get(channelid);
		if(channel==null)channel=new DmxChannel(channelid);
		output.put(channelid, channel); 
		return channel;
	}
}
