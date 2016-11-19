package fr.jfbeuve.webdmx.dmx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DmxFader implements Runnable{
	private static final Log log = LogFactory.getLog(DmxFader.class);
	
	private DmxWrapper dmx;
	boolean done, stale;
	private Thread t;
	private int strob;
	
	DmxFader(DmxWrapper _dmx){
		dmx = _dmx;
		stale=false;
		done=true;
		strob = 0;
	}
	
	@Override
	public void run() {
		log.debug("NEW THREAD");
		done=false;
		while(true){
			log.debug("LOOP");
			if(strob>7)strob=0;

			stale = true;
			done = dmx.apply(strob<4);
			stale = false;
			if(done)break;
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				log.error(e,e);
				done=true;
			}
		}
		log.debug("END THREAD");
	}
	synchronized void start(){
		if(t==null){
			// first time
			t = new Thread(this);
			t.start();
			return;
		}
		try {
			if(stale){
				while(stale) Thread.sleep(1);
			}
			
			if(done){
				while(t.isAlive()) Thread.sleep(1);
				t = new Thread(this);
				t.start();
				return;
			}
		} catch (InterruptedException e) {
			log.error(e,e);
			return;
		}
	}
}
