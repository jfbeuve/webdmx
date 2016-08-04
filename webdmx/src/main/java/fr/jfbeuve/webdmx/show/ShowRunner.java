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
	
	private long speed=1000, timestamp,fade=2000;
	
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
	 * applies next step
	 */
	void next(){
		log.info("#### next");
		for (IShow show : shows) {
			show.next();
		}
		// FADE only if fade time < step duration
		if(fade<speed) dmx.apply(fade);
		else dmx.apply(0);
	}
	/**
	 * starts autorun after to calls and sets tempo
	 */
	public void tap(){
		if(timestamp==0){
			timestamp = System.currentTimeMillis();
			if(auto!=null){
				auto.stop();
				auto=null;
			}
			next();			
			return;
		}
		
		long now = System.currentTimeMillis();
		speed = now-timestamp;
		
		auto();
		
		timestamp=0;
	}
	private void auto(){
		log.info("SPEED "+speed);
		next();
		if(auto!=null)auto.stop();
		auto = new Tempo(this, speed);
		new Thread(auto).start();
	}
	/**
	 * sets speed
	 */
	public void speed(long _speed){
		speed = _speed;
		if(auto!=null){
			auto.stop();
			auto = new Tempo(this, speed);
			new Thread(auto).start();
		}
	}
	/**
	 * set fade time  in millis
	 */
	public void fade(long time){
		fade=time;
	}
	/**
	 * @return fade time in millis
	 */
	public long fade(){
		return fade;
	}
}
