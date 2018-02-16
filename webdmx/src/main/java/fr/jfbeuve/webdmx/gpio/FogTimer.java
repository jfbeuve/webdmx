package fr.jfbeuve.webdmx.gpio;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FogTimer implements Runnable{
	private static final Log log = LogFactory.getLog(FogTimer.class);
	private long time;
	private boolean action;
	private FogHelper fog;
	public Thread thread;
	
	public FogTimer(long _time, boolean _action, FogHelper _fog){
		fog=_fog;
		time=_time;
		action=_action;
	}

	@Override
	public void run() {
		log.debug("START TIMER "+action+" "+time);
		try {
			Thread.sleep(time);
			fog.timer(action);
		} catch (InterruptedException e) {
			log.debug(e,e);
		}
	}

}
