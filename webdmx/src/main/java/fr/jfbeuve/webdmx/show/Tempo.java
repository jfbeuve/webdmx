package fr.jfbeuve.webdmx.show;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Tempo implements Runnable {
	private static final Log log = LogFactory.getLog(Tempo.class);
	
	private ShowRunner show;
	private long speed;
	private boolean stop=false;
	private Thread thread=null;
	
	public Tempo(ShowRunner _show, long _speed){
		speed=_speed;
		show=_show;
	}
	
	@Override
	public void run() {
		thread=Thread.currentThread();
		log.info("START "+show.show()+" "+thread);
		while(!stop){
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				log.debug(e, e);
				log.info("INTERRUPT SLEEP");
			}
			if(stop)return;
			if(!stop){
				log.info("NEXT");
				show.next();
			}else
				log.info("INTERRUPT NEXT");
		}
	}
	
	public void stop(){
		if(stop) return;
		log.info("INTERRUPT ASK "+thread);
		stop=true;
		thread.interrupt();
	}

}
