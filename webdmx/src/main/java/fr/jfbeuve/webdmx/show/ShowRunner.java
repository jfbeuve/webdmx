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
	
	@Autowired
	private RockShow rgb;
	
	private Tempo auto;
	
	private long speed=4000;
	
	private List<IShow> shows = new ArrayList<IShow>();
	
	/**
	 * adds a show to the scheduler
	 */
	public boolean start(IShow show){
		shows.add(show);
		return start();
	}
	/**
	 * removes a show from the scheduler
	 */
	public void stop(IShow show){
		shows.remove(show);
	}
	/**
	 * stops autorun
	 * @return true if stopped, false if already stopped
	 */
	public boolean stop(){
		if(auto!=null){
			auto.stop();
			auto=null;
			return true;
		}else
			return false;
	}
	/**
	 * starts/restarts autorun
	 * @return true if started, false if already running
	 */
	public boolean start(){
		if(auto==null){
			speed(speed);
			return true;
		}else
			return false;
	}
	
	/**
	 * applies next step
	 */
	void next(){
		log.info("#### next");
		for (IShow show : shows) {
			show.next();
		}
		long fade = 0;
		if(speed>2000) fade = speed/2;
		dmx.apply(fade);
	}
	public long speed(){
		return speed;
	}
	/**
	 * sets speed and starts auto run
	 */
	public void speed(long _speed){
		if(_speed==0){
			if(!stop()) next();
			return;
		}
		if(_speed<100) _speed = 100;
		speed = _speed;
		if(shows.isEmpty()) shows.add(rgb);
		if(auto!=null) auto.stop();
		next();
		auto = new Tempo(this, speed);
		new Thread(auto).start();
	}
	
	private boolean strob = false;
	private boolean running = false;
	public void strob(){
		if(shows.isEmpty()) shows.add(rgb);
		if(!strob){
			strob=true;
			running = stop();
			for (IShow show : shows) {
				show.strob(strob);
			}
		}else{
			strob=false;
			for (IShow show : shows) {
				show.strob(strob);
			}
			if(running) start();
		}
		dmx.apply(0);
	}
	
	/**
	 * set background color
	 **/
	public void color(RGBColor color) {
		if(shows.isEmpty()) shows.add(rgb);
		for (IShow show : shows) {
			show.color(color);
		}
		if(strob) dmx.apply(0);
	}
}
