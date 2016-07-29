package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.juanjo.openDmx.OpenDmx;

import fr.jfbeuve.webdmx.show.DmxColor;
import fr.jfbeuve.webdmx.show.RGBFixture;

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
	 * Apply DMX value if different of the previous or if force=true
	 * force=true is designed to be used only by DmxDimmer
	 */
	public void set(int _channel, int value, boolean force){
		logger.fine("offline = "+offline);
		
		DmxChannel channel = get(_channel);
		if(channel.value()==value&&!force)return;
		int toSet = channel.dim(value);
		
		if(!open&&!offline) open = OpenDmx.connect(OpenDmx.OPENDMX_TX);
		if(!open&&!offline) logger.severe("Open Dmx widget not detected!");
		if(!offline) OpenDmx.setValue(_channel-1,toSet);
		
		logger.info("OpenDmx.setValue("+_channel+","+toSet+")");
	}
	/**
	 * Apply DMX value
	 */
	public void set(int _channel, int value){
		set(_channel, value, false);
	}
	public void set(RGBFixture fixture, DmxColor color){
		set(fixture.red(), color.red());
		set(fixture.green(), color.green());
		set(fixture.blue(), color.blue());
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
