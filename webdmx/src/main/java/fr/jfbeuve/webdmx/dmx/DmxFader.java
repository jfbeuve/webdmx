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
	private Map<Integer,Integer> diff;
	
	public DmxFader(DmxWrapper _dmx){
		dmx = _dmx;
	}
	
	public synchronized void fade(long _time, Map<Integer,Integer> _target){
		if(thread!=null) stop();
		
		end = _target;
		time = _time;
		diff = new HashMap<Integer,Integer>();
		
		for (Integer channel : end.keySet()) {
			int currentValue = dmx.get(channel).value();
			int targetValue = end.get(channel);
			int value = targetValue - currentValue;
			if(currentValue!=targetValue) diff.put(channel, value);
		}
		
		if(diff.isEmpty()) 
			log.info("NOTHING TO FADE "+time);
		else 
			begin();
	}
	
	private Thread thread=null;
	private boolean stop=false;
	
	public void run() {
		thread = Thread.currentThread();
		log.info("FADING START "+time+" "+thread);
		stop=false;
		long start = System.currentTimeMillis();
		while(!stop&&(System.currentTimeMillis()<start+time)){
			int rate = (int) ((System.currentTimeMillis() - start) * 255 / time);
			log.debug("FADING "+rate+ "/255");
			Map<Integer,Integer> values = new HashMap<Integer,Integer>();
			for (Integer channel : diff.keySet()) {
				int targetValue = end.get(channel);
				int gap = diff.get(channel);
				int newValue = targetValue - gap + gap*rate/255;
				values.put(channel,newValue);
			}
			dmx.set(values);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				log.debug(e,e);
				log.info("INTERRUPTED at "+(System.currentTimeMillis()-start)+" / "+time);
			}
		}
		
		//ends up forcing final values 
		dmx.set(end);
		log.info("FADING END");
		thread = null;
	}
	private void begin(){
		Thread t = new Thread(this);
		t.start();
		while(thread==null){
			try {
				Thread.sleep(1);
				log.debug("WAITING on "+thread+" TO START...");
			} catch (InterruptedException e) {
				log.debug(e, e);
			}
		}
	}
	private void stop(){
		log.info("INTERRUPT ASK "+thread);	
		stop=true;
		thread.interrupt();
		while(thread!=null){
			try {
				Thread.sleep(1);
				log.debug("WAITING on "+thread+" TO STOP...");
			} catch (InterruptedException e) {
				log.debug(e, e);
			}
		}
	}
	public boolean running(){
		return thread!=null;
	}
	
}
