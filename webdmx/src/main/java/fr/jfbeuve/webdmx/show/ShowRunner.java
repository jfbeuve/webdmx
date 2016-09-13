package fr.jfbeuve.webdmx.show;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.dmx.DmxOverride;
import fr.jfbeuve.webdmx.dmx.DmxOverrideMgr;
import fr.jfbeuve.webdmx.dmx.DmxStrob;

@Component
public class ShowRunner {
	private static final Log log = LogFactory.getLog(ShowRunner.class);	
	
	@Autowired
	private DmxOverrideMgr dmx;
	
	private Tempo auto;

	// DEFAULTS
	private long speed=4000;
	private long strobospeed=100;
	private long fade=2000;
	private Show show=null;
	
	public ShowRunner(){
		auto = new Tempo(this);
	}
	
	/**
	 * @return true if speed>fade time
	 */
	public boolean fade(){
		return speed>fade;
	}
	public void fade(long time) {
		this.fade = time;
	}
	
	/**
	 * starts/restarts autorun
	 */
	public synchronized void start(Show _show){
		if(show==_show) return;
		blackout=false;
		boolean restart = true;
		if(show!=null&&_show!=null&&show.strob()==_show.strob()) restart=false;
		show = _show;
		if(show!=null) show.color(color);
		if(restart) auto.go(show.strob()?strobospeed:speed);
	}
	
	/**
	 * applies next step
	 */
	void next(){
		if(show==null) return;
		log.info("#### next");

		// AUTO COLOR
		if(colortime==0) colortime = System.currentTimeMillis();
		if(autocolor>-1){
			if(System.currentTimeMillis()-colortime>autocolor){
				colortime = System.currentTimeMillis();
				RGBColor before = color;
				//next color
				autoColorNext(colorseq);
				show.color(color);
				log.info("AUTO COLOR CHANGE "+before+ " => "+color);
			}
		}
		
		DmxCue cue = show.next(this);
		if(speed<fade||show.strob()) dmx.apply(0,cue);
		else dmx.apply(fade,cue);
	}
	private void autoColorNext(RGBColor[] colorseq){
		for(int i=0;i<colorseq.length;i++){
			if(color==colorseq[i]){
				int a = i+1;
				if(a==colorseq.length) a=0;
				color = colorseq[a];
				break;
			}
		}
	}
	
	@Autowired
	private DmxStrob solostrob;
	
	public void strobospeed(long s){
		strobospeed = s;
		solostrob.speed(strobospeed);
		if(show!=null&&show.strob()) auto.go(strobospeed);
	}
	public long strobospeed(){
		return strobospeed;
	}
	private long timestamp=0;
	/**
	 * sets speed 
	 * -1 PAUSE
	 * 0 TAP
	 */
	public void speed(long _speed){
		if(_speed==-1){
			// PAUSE + TAP
			timestamp = System.currentTimeMillis();
			auto.go(-1);
			next();
			return;
		}
		if(_speed==0){
			// TAP
			if(timestamp>0){
				// adjust speed
				long newspeed =  System.currentTimeMillis() - timestamp;
				timestamp = System.currentTimeMillis();
				speed = newspeed;
			} else {
				// record timestamp
				timestamp = System.currentTimeMillis();
				return;
			}
		}else
			speed = _speed;
		// dmx runs at 44hz
		if(speed<20)speed=20;
		if(show!=null&&!show.strob()) auto.go(speed);
	}
	
	public long speed(){
		return speed;
	}
	
	private RGBColor color=RGBColor.MAUVE;
	private RGBColor[] colorseq = {RGBColor.CYAN, RGBColor.MAUVE, RGBColor.VERT, RGBColor.BLEU,RGBColor.JAUNE, RGBColor.ROUGE};
	private long autocolor=-1;
	public long autocolor(){
		return autocolor;
	}
	public void autocolor(long time){
		autocolor=time;
	}
	private long colortime=0;
	/**
	 * set background color
	 **/
	public void color(RGBColor _color) {
		colortime = System.currentTimeMillis();
		color = _color;
		
		boolean strob = false;
		if(show!=null){
			show.color(color);
			strob = show.strob();
		}
		if(solo!=null){
			if(!strob) dmx.override(new DmxOverride(solo.f,color.solo(),solo.dim, fade));
			else dmx.override(new DmxOverride(solo.f,color,solo.dim, fade));
		}
	}
	private Solo solo=null;
	public void solo(Solo s){
		boolean cancel = s.dim<0;
		boolean change = solo!=null&&solo.f!=s.f; 
		
		if(cancel||change){
			// cancel previous override
			dmx.reset(solo.f);
			/*
			 DmxCue cue = new DmxCue();
			cue.set(solo.f, RGBColor.BLACK);
			if(cancel&&blackout)
				dmx.apply(fade,cue); // allow fade out
			else 
				dmx.apply(0,cue);
			*/
			solo = null;
		}

		if(!cancel) {
			//set new override
			boolean strob = false;
			if(show!=null) strob = show.strob();
			dmx.override(new DmxOverride(s,strob||bgblack||blackout?color:color.solo(),blackout&&!change?fade:0)); // allow fade in
			solo = s;
		}
	}
	boolean blackout = true;
	public void blackout(){
		auto.go(-1);
		dmx.blackout(fade()?fade:0);
		blackout=true;
	}
	private boolean bgblack = false;
	public boolean bgblack(){
		return bgblack;
	}
	public void bgblack(boolean b){
		bgblack = b;
	}
	public Show show() {
		return show;
	}
	public void stop(){
		auto.go(-1);
	}
}
