package fr.jfbeuve.webdmx.v2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DmxFader implements Runnable{
	private static final Log log = LogFactory.getLog(DmxFader.class);
	private DmxWrapper dmx;
	boolean done = true;
	boolean update = false;
	
	DmxFader(DmxWrapper _dmx){
		dmx = _dmx;
	}
	
	@Override
	public void run() {
		done = false;
		while(!done&&!update){
			update = false;
			
			done = dmx.fade();
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				log.error(e,e);
				break;
			}
		}
		done=true;
	}

	synchronized void start(){
		if(!done) {
			update = true;
			return;
		}
		new Thread(this).start();
	}
}
