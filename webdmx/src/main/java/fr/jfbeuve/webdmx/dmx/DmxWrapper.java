package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.List;
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
	private static final Log log = LogFactory.getLog(DmxWrapper.class);
	private Map<String,DmxDimmer> dimmers = new HashMap<String, DmxDimmer>();

	public DmxWrapper(){
		//dimmers.put(DmxDimmer.MASTER, new DmxDimmer(this, new int[]{17}));
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
	 * Apply DMX IO
	 */
	private void set(DmxChannel channel, int value){
		log.debug("offline = "+offline);
		int target = channel.dim(value);
		
		if(!open&&!offline) open = OpenDmx.connect(OpenDmx.OPENDMX_TX);
		if(!open&&!offline) log.warn("Open Dmx widget not detected!");
		if(!offline) OpenDmx.setValue(channel.channel()-1,target);
		
		log.info("OpenDmx.setValue("+channel.channel()+","+target+")");
	}

	/**
	 * Set DMX values
	 */
	public void set(Map<Integer,Integer> values){
		set(values, false);
	}
	
	/**
	 * Set DMX values in bulk
	 */
	void set(Map<Integer,Integer> values, boolean dimmer){
		//TODO fading
		for (Integer channelId : values.keySet()) {
		    DmxChannel channel = get(channelId);
		    // remove unchanged values if !dimmer and old value = new value
		    if(!dimmer && values.get(channelId)==channel.value()) continue;
			set(channel,values.get(channelId));
		}
	}
	
	/**
	 * Force channels refresh with no value change (used by master/group dimmers)
	 */
	void refresh(List<DmxChannel> channels){
		Map<Integer,Integer> values = new HashMap<Integer,Integer>();
		for (DmxChannel channel : channels) {
			if(channel.value()==0) continue;
			values.put(channel.channel(),channel.value());
		}
		set(values,true);
	}
	
	/**
	 * Initialize channel with dimmer
	 */
	public void init(DmxChannel channel){
		output.put(channel.channel(), channel);
	}
	/**
	 * Add a dimmer
	 */
	public void init(String name, DmxDimmer dim){
		dimmers.put(name,dim);
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
