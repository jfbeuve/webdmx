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
	
	private long speed=4000;
	
	private List<IShow> shows = new ArrayList<IShow>();
	
	/**
	 * speed threshold for snap/fade
	 */
	private long fadeThreshold=2000;
	
	public long fadeThreshold() {
		return fadeThreshold;
	}
	public void fadeThreshold(long fadeThreshold) {
		this.fadeThreshold = fadeThreshold;
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
			next();
			auto = new Tempo(this, speed);
			new Thread(auto).start();
			return true;
		}else
			return false;
	}
	public void set(IShow show){
		show.color(color);
		shows.add(show);
	}
	public void reset(IShow show){
		List<IShow> newShows = new ArrayList<IShow>();
		newShows.add(show);
		shows=newShows;
	}
	/**
	 * applies next step
	 */
	void next(){
		log.info("#### next");

		// AUTO COLOR
		if(colortime==0) colortime = System.currentTimeMillis();
		if(autocolor){
			if(System.currentTimeMillis()-colortime>autoColorTime){
				colortime = System.currentTimeMillis();
				RGBColor before = color;
				//next color
				for(int i=0;i<colorseq.length;i++){
					if(color==colorseq[i]){
						int a = i+1;
						if(a==colorseq.length) a=0;
						for (IShow show : shows) {
							show.color(colorseq[i+1]);
						}
						break;
					}
				}
				log.info("AUTO COLOR CHANGE "+before+ " => "+color);
			}
		}
		
		for (IShow show : shows) {
			show.next(dmx);
		}
		long fade = 0;
		if(speed>fadeThreshold) fade = speed/2;
		dmx.apply(fade);
	}
	public long speed(){
		return speed;
	}
	public boolean isEmpty(){
		return shows.isEmpty();
	}
	/**
	 * sets speed 
	 */
	public void speed(long _speed){
		if(_speed==0){
			if(!stop()) next();
			return;
		}
		if(_speed<100) _speed = 100;
		speed = _speed;
		if(auto!=null){ 
			auto.stop();
			auto=null;
			start();
		}
	}
	
	private RGBColor color=RGBColor.MAUVE;
	private RGBColor[] colorseq = {RGBColor.CYAN, RGBColor.MAUVE, RGBColor.JAUNE, RGBColor.ROUGE, RGBColor.AMBRE, RGBColor.VERT, RGBColor.BLEU};
	public long autoColorTime=180000;
	private boolean autocolor = true;
	private long colortime=0;
	/**
	 * set background color
	 **/
	public void color(RGBColor _color) {
		if(_color==RGBColor.AUTO){
			autocolor=true;
			return;
		}
		autocolor=false;
		colortime = System.currentTimeMillis();
		color = _color;
		for (IShow show : shows) {
			show.color(color);
		}
	}
}
