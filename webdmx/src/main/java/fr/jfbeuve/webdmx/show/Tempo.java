package fr.jfbeuve.webdmx.show;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Tempo implements Runnable {
	private static final Log log = LogFactory.getLog(Tempo.class);
	private ShowRunner show;
	private long speed;
	private boolean stop=false;
	
	public Tempo(ShowRunner _show, long _speed){
		speed=_speed;
		show=_show;
	}
	
	@Override
	public void run() {
		while(!stop){
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				log.warn(e, e);
			}
			if(!stop) show.nextAuto();
		}
	}
	
	public void stop(){
		stop=true;
	}

}
