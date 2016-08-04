package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DmxFader implements Runnable{
	private static final Log log = LogFactory.getLog(DmxFader.class);
	
	private DmxWrapper dmx;
	private long time;
	private int step=1;
	
	private Map<Integer,Integer> end;
	private Map<Integer,Integer> diff = new HashMap<Integer,Integer>();
	
	public DmxFader(DmxWrapper _dmx, Map<Integer,Integer> _target){
		dmx = _dmx;
		end = _target;
		
		for (Integer channel : end.keySet()) {
			int currentValue = dmx.get(channel).value();
			int targetValue = end.get(channel);
			int value = targetValue - currentValue;
			diff.put(channel, value);
		}
	}
	public void fade(long _time){
		time = _time / 255;

		/*if(time<100){
			// system does not really sustain refresh more often than every 100ms
			step = 100/(int)time;
			time=100;
		}*/
		
		log.info("time="+time+"; step="+step);
		new Thread(this).start();
	}
	
	public void run() {
		for (int i = 1; i < 256; i=i+step) {
			log.info("FADING "+i);
			Map<Integer,Integer> values = new HashMap<Integer,Integer>();
			for (Integer channel : diff.keySet()) {
				int targetValue = end.get(channel);
				int gap = diff.get(channel);
				int newValue = targetValue - gap + gap*i/255;
				values.put(channel,newValue);
			}
			dmx.set(values);
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				log.warn(e,e);
			}
		}
		//ends up forcing final values in case step<>1 and i never ends up being 255
		if(time==100) dmx.set(end);
		log.info("FADING COMPLETED");
	}
}
