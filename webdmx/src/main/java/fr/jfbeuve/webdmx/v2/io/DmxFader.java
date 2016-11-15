package fr.jfbeuve.webdmx.v2.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DmxFader implements Runnable{
	private static final Log log = LogFactory.getLog(DmxFader.class);
	private DmxWrapper dmx;
	boolean completed = true;
	
	DmxFader(DmxWrapper _dmx){
		dmx = _dmx;
	}
	
	@Override
	public void run() {
		completed = false;
		while(!completed){
			try {
				completed = dmx.fade();
				Thread.sleep(20);
			} catch (InterruptedException e) {
				log.debug(e,e);
			}
		}
	}

	void start(){
		if(!completed) return;
		new Thread(this).start();
	}
}
