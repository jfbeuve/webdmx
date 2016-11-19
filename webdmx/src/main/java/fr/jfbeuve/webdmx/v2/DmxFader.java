package fr.jfbeuve.webdmx.v2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DmxFader implements Runnable{
	private static final Log log = LogFactory.getLog(DmxFader.class);
	
	private DmxWrapper dmx;
	boolean done, update;
	private Thread t;
	
	DmxFader(DmxWrapper _dmx){
		dmx = _dmx;
		done=true;
	}
	
	@Override
	public void run() {
		done = false; update = false;
		while(!done&&!update){
			log.debug("LOOP done="+done+" update="+update);
			update = false;
			
			done = dmx.fade();
			
			try {
				if(!done) Thread.sleep(20);
			} catch (InterruptedException e) {
				log.error(e,e);
				done=true;
			}
		}
	}

	synchronized void start(){
		if(done){
			try {
				if(t!=null) while(t.isAlive()) Thread.sleep(1);
				t = new Thread(this);
				t.start();
			} catch (InterruptedException e) {
				log.error(e,e);
			}
		}else 
			update = true;
	}
}
