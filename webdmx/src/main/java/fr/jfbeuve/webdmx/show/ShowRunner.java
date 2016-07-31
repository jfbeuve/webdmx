package fr.jfbeuve.webdmx.show;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxWrapper;

@Component
public class ShowRunner {
	private static final Log log = LogFactory.getLog(ShowRunner.class);	
	
	@Autowired
	private DmxWrapper dmx;
	
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
		log.info("#### nextAuto");
		Map<Integer,Integer> values = new HashMap<Integer,Integer>();
		for (IShow show : shows) {
			values.putAll(show.next());
		}
		dmx.set(values);
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
		nextAuto();
		if(auto!=null)auto.stop();
		auto = new Tempo(this, speed);
		new Thread(auto).start();
	}
}
