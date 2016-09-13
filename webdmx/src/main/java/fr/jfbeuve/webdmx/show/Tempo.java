package fr.jfbeuve.webdmx.show;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Tempo implements Runnable {
	private static final Log log = LogFactory.getLog(Tempo.class);
	
	private ShowRunner show;
	private long speed=-1;
	private Thread thread=null;
	private boolean stop = false;
	
	public Tempo(ShowRunner _show){
		show=_show;
	}
	
	@Override
	public void run() {
		thread = Thread.currentThread();
		log.info("STARTING "+show.show()+" "+speed+" "+thread);
		stop=false;
		while(!stop){
			show.next();
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				log.info("INTERRUPTED "+thread);
				log.debug(e, e);
			}
		}
		thread=null;
		log.info("STOPPED");
	}
	
	/**
	 * starts/stops/change speed
	 */
	public synchronized void go(long s){
		if(speed==s) return;
		if(thread!=null) stop();
		speed = s;
		if(speed>0) begin();
	}
	private void begin(){
		Thread t = new Thread(this);
		log.info("START "+show.show()+" "+speed+" "+t);
		t.start();
		while(thread==null){
			try {
				Thread.sleep(1);
				log.debug("WAITING on "+thread+" TO START...");
			} catch (InterruptedException e) {
				log.debug(e, e);
			}
		}
	}
	private void stop(){
		log.info("INTERRUPT ASK "+thread);	
		if(thread!=null) {
			stop=true;
			thread.interrupt();
			while(thread!=null){
				try {
					Thread.sleep(1);
					log.debug("WAITING on "+thread+" TO STOP...");
				} catch (InterruptedException e) {
					log.debug(e, e);
				}
			}
		} 
	}
	public boolean isRunning(){
		return thread!=null;
	}
}
