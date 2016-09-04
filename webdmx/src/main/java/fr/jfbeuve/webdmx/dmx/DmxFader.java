package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DmxFader implements Runnable{
	private static final Log log = LogFactory.getLog(DmxFader.class);
	
	private DmxWrapper dmx;
	private long time;
	
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
		time = _time;
		log.info("FADING ASK "+time);
		thread = new Thread(this);
		thread.start();
	}
	private Thread thread=null;
	private boolean done = false;
	
	public void run() {
		log.info("FADING START "+time+" "+thread);
		long start = System.currentTimeMillis();
		while(!done&&(System.currentTimeMillis()<start+time)){
			int rate = (int) ((System.currentTimeMillis() - start) * 255 / time);
			log.debug("FADING "+rate+ "/255 ("+done+")");
			Map<Integer,Integer> values = new HashMap<Integer,Integer>();
			for (Integer channel : diff.keySet()) {
				int targetValue = end.get(channel);
				int gap = diff.get(channel);
				int newValue = targetValue - gap + gap*rate/255;
				values.put(channel,newValue);
				if(done){
					log.info("INTERRUPT FADING at "+(System.currentTimeMillis()-start)+" / "+time);
					return;
				}
			}
			dmx.set(values);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				log.debug(e,e);
				log.info("INTERRUPT SLEEP at "+(System.currentTimeMillis()-start)+" / "+time);
				return;
			}
			if(done){
				log.info("INTERRUPT AFTER SLEEP at "+(System.currentTimeMillis()-start)+" / "+time);
				return;
			}
		}
		
		//ends up forcing final values in case step<>1 and i never ends up being 255
		if(!done){
			dmx.set(end);
		}else{
			log.info("INTERRUPT END");
		}
		log.info("FADING END");
		done=true;
	}
	public void interupt(){
		if(done)return;
		log.info("INTERRUPT ASK "+thread);
		done = true;
		thread.interrupt();
	}
}
