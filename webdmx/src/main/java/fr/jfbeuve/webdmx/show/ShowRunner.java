package fr.jfbeuve.webdmx.show;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.dmx.DmxOverride;

@Component
public class ShowRunner {
	private static final Log log = LogFactory.getLog(ShowRunner.class);	
	
	@Autowired
	private DmxCue dmx;
	
	private Tempo auto;
	
	private long speed=4000;
	private long fade=1000;
	
	private Show show=null;
	
	
	public void fade(long time) {
		this.fade = time;
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
	public boolean start(Show _show){
		show = _show;
		if(show==null) return false;
		show.color(color);
		if(auto==null){
			next();
			auto = new Tempo(this, show.strob()?100:speed);
			new Thread(auto).start();
			return true;
		}else
			return false;
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
						show.color(colorseq[i+1]);
						break;
					}
				}
				log.info("AUTO COLOR CHANGE "+before+ " => "+color);
			}
		}
		
		show.next(dmx);
		if(speed<fade) dmx.apply(0);
		else dmx.apply(fade);
	}
	public long speed(){
		return speed;
	}
	/**
	 * sets speed 
	 */
	public void speed(long _speed){
		//TODO next+tap if speed = 0
		//TODO stop+next+tap if speed = -1
		if(_speed==0){
			if(!stop()) next();
			return;
		}
		speed = _speed;
		if(auto!=null){ 
			auto.stop();
			auto=null;
			start(show);
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
		
		boolean strob = false;
		if(show!=null){
			show.color(color);
			strob = show.strob();
		}
		if(solo!=null){
			if(!strob) dmx.override(new DmxOverride(solo.f,color.solo(),solo.dim));
			else dmx.override(new DmxOverride(solo.f,color,solo.dim));
		}
	}
	private Solo solo=null;
	public void solo(Solo s){
		// cancel previous override
		if(solo!=null) dmx.override(new DmxOverride(solo.f));
		//set new override
		dmx.override(new DmxOverride(s.f,color.solo(),s.dim));
		solo = s;
	}
	public void blackout(){
		dmx.blackout(fade);
	}
}
