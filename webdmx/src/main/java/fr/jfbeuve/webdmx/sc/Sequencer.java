package fr.jfbeuve.webdmx.sc;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxWrapper;

@Component
public class Sequencer implements Runnable{
	private static final Log log = LogFactory.getLog(Sequencer.class);
	
	private Scene[] scenes={};
	private Thread t;
	private boolean stop=false;
	private int i=0;
	private long speed = 200;
	private ArrayList<Integer> reset = new ArrayList<Integer>();
	
	@Autowired
	private DmxWrapper dmx;
	
	public void run() {
		try {
			log.debug("SEQUENCE START");
			while(!stop){
				//long time = System.currentTimeMillis();
				next();
				//long sleep = speed-System.currentTimeMillis()+time;
				//if(sleep>0) Thread.sleep(sleep);
				 Thread.sleep(speed);
			}
			log.debug("SEQUENCE END");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * -1 STOP (PAUSE + REST overrides)
	 * 0 PAUSE + NEXT
	 * 0+ speed in ms
	 */
	public synchronized void speed(long s){
		log.debug("SPEED "+s);
		stop();
		if(s<=0) {
			if(s==0&&scenes.length>0) next(); // MAN
			if(s==-1) cancel(); // STOP
		}else{
			// SPEED
			speed = s;
			if(scenes.length>0) start();
		}
	}
	private void next(){
		log.debug("SEQUENCE NEXT STEP");
		if(i>=scenes.length) i=0;
		
		for(RGBFixtureState f: scenes[i].fixtures){
			reset.remove(Integer.valueOf(f.id));
		}
		int[] toReset = new int[reset.size()];
		for(int i=0;i<reset.size();i++) toReset[i]=reset.get(i);
		
		dmx.override(new ScOverride(scenes[i],toReset, 1));
		
		reset = new ArrayList<Integer>();
		for(int r=0;r<scenes[i].fixtures.length;r++){
			reset.add(scenes[i].fixtures[r].id);
		}
		
		i++;
	}
	private void cancel(){
		if(!reset.isEmpty()){
			int[] toReset = new int[reset.size()];
			for(int i=0;i<reset.size();i++) toReset[i]=reset.get(i);
			
			dmx.override(new ScOverride(new Scene(), toReset, 1));
			reset = new ArrayList<Integer>();
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
