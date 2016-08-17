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
		step = 1;
		time = _time / 255;
		
		// system does not really sustain refresh more often than every 100ms
		if(time<100){
			step = 100/(int)time;
			time=100;
		}
		
		log.info("time="+time+"; step="+step);
		new Thread(this).start();
	}
	private Thread thread=null;
	private boolean done = false;
	
	public void run() {
		thread=Thread.currentThread();
		for (int i = 1; i < 256; i=i+step) {
			log.info("FADING "+i+ " ("+done+")");
			Map<Integer,Integer> values = new HashMap<Integer,Integer>();
			for (Integer channel : diff.keySet()) {
				int targetValue = end.get(channel);
				int gap = diff.get(channel);
				int newValue = targetValue - gap + gap*i/255;
				values.put(channel,newValue);
				if(done)return;
			}
			dmx.set(values);
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				log.info(e.getMessage());
				return;
			}
			if(done)return;
		}
		
		//ends up forcing final values in case step<>1 and i never ends up being 255
		if(time==100) dmx.set(end);
		log.info("FADING COMPLETED");
		done=true;
	}
	public void interupt(){
		if(done)return;
		log.info("## INTERRUPT ##");
		done = true;
		thread.interrupt();
	}
}
