package fr.jfbeuve.webdmx.show;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxCue;

@Component
public class ShowRunner {
	private static final Log log = LogFactory.getLog(ShowRunner.class);	
	
	@Autowired
	private DmxCue dmx;
	
	private Tempo auto;
	
	private long speed=1000, timestamp;
	
	private List<IShow> shows = new ArrayList<IShow>();
	
	/**
	 * adds a show to the scheduler
	 */
	public void start(IShow show){
		shows.add(show);
		if(auto==null) auto();
	}
	/**
	 * removes a show from the scheduler
	 */
	public void stop(IShow show){
		shows.remove(show);
	}
	/**
	 * stops autorun
	 */
	public void stop(){
		if(auto!=null){
			auto.stop();
			auto=null;
		}
	}
	/**
	 * restarts autorun
	 */
	public void start(){
		if(auto==null) auto();
	}
	/**
	 * applies next step and disables autorun
	 */
	public void next(){
		if(auto!=null){
			auto.stop();
			auto=null;
		}
		nextAuto();
	}
	
	/**
	 * applies next step
	 */
	void nextAuto(){
		log.info("#### next");
		for (IShow show : shows) {
			show.next();
		}
		dmx.apply();
	}
	/**
	 * starts autorun after to calls and sets tempo
	 */
	public void tap(){
		if(timestamp==0){
			timestamp = System.currentTimeMillis();
			nextAuto();			
			return;
		}
		
		long now = System.currentTimeMillis();
		speed = now-timestamp;
		
		auto();
		
		timestamp=0;
	}
	private void auto(){
		log.info("SPEED "+speed);
		nextAuto();
		if(auto!=null)auto.stop();
		auto = new Tempo(this, speed);
		new Thread(auto).start();
	}
}
