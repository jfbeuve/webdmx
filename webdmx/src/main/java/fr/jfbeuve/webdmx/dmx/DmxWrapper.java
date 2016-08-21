package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.io.OlaWeb;

@Component
public class DmxWrapper {
	
	private Map<Integer,DmxChannel> output = new HashMap<Integer, DmxChannel>();
	private static final Log log = LogFactory.getLog(DmxWrapper.class);
	
	private int[] data;

	public DmxWrapper(){
		data = new int[512];
		for(int i=0;i<512;i++) data[i]=0;
	}
	
	@Autowired
	public OlaWeb io;
	
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
	public void set(Map<Integer,Integer> values){
		for (Integer channelId : values.keySet()) {
		    DmxChannel channel = get(channelId);
		    int dim = channel.value(values.get(channelId));
			data[channelId-1] = dim;
			log.info(channelId+" = "+dim);
		}
		if(!offline) io.send(data);
		log.info("DMX SEND");
	}
	
	/**
	 * get dimmer by name and apply new value
	 */
	public void dim(DmxDimmer dimmer, int value){
		dimmer.value(value);
		for (int channelId : dimmer.channels()) {
			if(!output.containsKey(channelId)) continue;
			DmxChannel channel = output.get(channelId);
			int dim = channel.value();
			data[channelId-1] = dim;
			log.info(channelId+" = "+dim);	
		}
		if(!offline) io.send(data);
		log.info("DMX SEND");
	}
	
	public DmxChannel get(int channelid){
		DmxChannel channel = output.get(channelid);
		if(channel==null) {
			channel=new DmxChannel(channelid);
			output.put(channelid, channel);
		}
		return channel;
	}
	/**
	 * Sets all channels to 0
	 */
	public void blackout(){
		for(Integer channelId:output.keySet()){
			DmxChannel channel = output.get(channelId);
			data[channelId-1] = channel.value(0);
			log.info(channelId+" = 0");	
		}
		if(!offline) io.send(data);
		log.info("DMX SEND");
	}
}
