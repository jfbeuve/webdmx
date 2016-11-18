package fr.jfbeuve.webdmx.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SceneSequencer implements Runnable{
	private SceneSequence seq;
	private Thread t;
	private boolean stop=false;
	private int i=0;
	private long speed = 300;
	private long timestamp=0;
	
	@Autowired
	private DmxWrapper dmx;
	
	@Override
	public void run() {
		try {
			while(!stop){
				next();
				Thread.sleep(speed);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 0 STOP
	 * > speed in ms
	 */
	public synchronized void speed(long s){
		stop();
		if(s==0) return;
		speed = s;
		start();
	}
	
	/**
	 * @deprecated prefer tap measure on client side
	 */
	public void tap(){
		if(timestamp>0){
			// adjust speed
			long newspeed =  System.currentTimeMillis() - timestamp;
			timestamp = System.currentTimeMillis();
			if(newspeed<20)newspeed=20; // dmx runs at 44hz
			speed(newspeed);
		} else {
			// record timestamp
			timestamp = System.currentTimeMillis();
		}
	}
	
	public void man(){
		speed(0);
		next();
	}
	private void next(){
		if(i>=seq.scenes.length) i=0;
		dmx.set(seq.scenes[i++]);
	}
	public void pause(){
		speed(0);
	}
	public void play (SceneSequence s){
		i=0;
		seq = s;
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
