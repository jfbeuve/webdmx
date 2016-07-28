package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.juanjo.openDmx.OpenDmx;

@Component
public class DmxWrapper {
	private boolean open;
	private Map<Integer,DmxChannel> output = new HashMap<Integer, DmxChannel>();
	private Log logger = LogFactory.getLog(DmxWrapper.class);
	private Map<String,DmxDimmer> dimmers = new HashMap<String, DmxDimmer>();

	public DmxWrapper(){
		dimmers.put(DmxDimmer.MASTER, new DmxDimmer(this, new int[]{17}));
	}
	
	@Value("${offline:false}")
	private boolean offline;
	
	/**
	 * Apply DMX value
	 */
	public void set(int _channel, int value){
		
		DmxChannel channel = output.get(_channel);
		if(channel==null)channel=new DmxChannel(_channel);
		output.put(_channel, channel);
		
		int toSet = channel.dim(value);
		
		if(!open&&!offline) open = OpenDmx.connect(OpenDmx.OPENDMX_TX);
		if(!open&&!offline) logger.error("Open Dmx widget not detected!");
		if(!offline) OpenDmx.setValue(_channel-1,toSet);
		
		logger.info("OpenDmx.setValue("+_channel+","+toSet+")");
	}
	
	/**
	 * Set channel behavior
	 */
	public void set(DmxChannel channel){
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
}
