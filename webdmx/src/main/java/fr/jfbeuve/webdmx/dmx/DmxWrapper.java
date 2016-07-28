package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.juanjo.openDmx.OpenDmx;

@Component
public class DmxWrapper {
	private boolean open;
	private int master=255;
	private Map<Integer,Integer> output = new HashMap<Integer, Integer>();
	private Logger logger = Logger.getLogger("aname");
	
	@Value("${offline:false}")
	private boolean offline;
	
	public void set(int channel, int _value){
		int value = _value * master/ 255;
		if(!open&&!offline) open = OpenDmx.connect(OpenDmx.OPENDMX_TX);
		if(!open&&!offline) logger.severe("Open Dmx widget not detected!");
		if(!offline) OpenDmx.setValue(channel-1,value);
		output.put(channel,_value);
		logger.info("OpenDmx.setValue("+channel+","+value+")");
	}
	public int get(int channel){
		return output.get(channel);
	}
	public void disconnect(){
		if(open&&!offline) OpenDmx.disconnect();
	}
	public void master(int value){
		master=value;
		for (Iterator<Integer> iterator = output.keySet().iterator(); iterator.hasNext();) {
			int channel = iterator.next();
			set(channel, get(channel));
		}
	}
}
