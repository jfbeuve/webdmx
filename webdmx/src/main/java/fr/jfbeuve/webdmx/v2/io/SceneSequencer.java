package fr.jfbeuve.webdmx.v2.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SceneSequencer implements Runnable{
	private SceneSequence seq;
	private Thread t;
	private boolean stop=false;
	private int i=0;
	private long speed = 300;
	
	@Autowired
	private DmxWrapper dmx;
	
	@Override
	public void run() {
		try {
			while(!stop){
				if(i>=seq.scenes.length) i=0;
				dmx.set(seq.scenes[i++]);
				Thread.sleep(speed);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void speed(long s){
		stop();
		if(s==0) return;
		speed = s;
		start();
	}
	
	void set (SceneSequence s){
		i=0;
		seq = s;
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
