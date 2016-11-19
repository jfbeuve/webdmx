package fr.jfbeuve.webdmx.sc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxWrapper;

@Component
public class Sequencer implements Runnable{
	private static final Log log = LogFactory.getLog(Sequencer.class);
	
	private Scene[] scenes;
	private Thread t;
	private boolean stop=false;
	private int i=0;
	private long speed = 300;
	private int[] reset = new int[0];
	
	@Autowired
	private DmxWrapper dmx;
	
	public void run() {
		try {
			log.debug("SEQUENCE START");
			while(!stop){
				long time = System.currentTimeMillis();
				next();
				long sleep = speed-System.currentTimeMillis()+time;
				if(sleep>0) Thread.sleep(sleep);
			}
			log.debug("SEQUENCE END");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 0 STOP
	 * > speed in ms
	 */
	public synchronized void speed(long s){
		log.debug("SPEED "+s);
		stop();
		if(s==0) return;
		speed = s;
		start();
	}
	
	public void man(){
		speed(0);
		next();
	}
	private void next(){
		log.debug("SEQUENCE NEXT STEP");
		if(i>=scenes.length) i=0;
		dmx.override(new Override(scenes[i++], reset, 1));
		
		reset = new int[scenes[i-1].fixtures.length];
		for(int r=0;r<reset.length;r++){
			reset[r]=scenes[i-1].fixtures[r].id;
		}
	}
	public void pause(){
		speed(0);
		if(reset.length>0){
			dmx.override(new Override(new Scene(), reset, 1));
			reset = new int[0];
		}
	}
	public void play (Scene[] s){
		i=0;
		scenes = s;
		if(t==null) speed(speed);
	}
	
	private void stop(){
		if(t==null) return;
		
		stop=true;
		t.interrupt();
		
		try {
			while(t.isAlive())
				Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		t=null;
	}
	
	private void start(){
		if(t==null){
			stop = false;
			t = new Thread(this);
			t.start();
		}
	}
}
