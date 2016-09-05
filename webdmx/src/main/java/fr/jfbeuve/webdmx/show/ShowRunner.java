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

	// DEFAULTS
	private long speed=4000;
	private long strobospeed=100;
	private long fade=2000;
	private Show show=null;
	
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
	 * stops autorun
	 * @return true if stopped, false if already stopped
	 */
	public synchronized void stop(){
		if(auto!=null) auto.stop();
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
		if(restart) restart();
	}
	
	private synchronized void restart(){
		if(show==null) return;
		stop();
		next();
		auto = new Tempo(this, show.strob()?strobospeed:speed);
		new Thread(auto).start();
	}
	/**
	 * applies next step
	 */
	void next(){
		if(show==null) return;
		log.info("#### next");

		// AUTO COLOR
		if(colortime==0) colortime = System.currentTimeMillis();
		if(autocolor){
			if(System.currentTimeMillis()-colortime>autoColorTime){
				colortime = System.currentTimeMillis();
				RGBColor before = color;
				//next color
				autoColorNext(coldcolorseq);
				autoColorNext(warmcolorseq);
				show.color(color);
				log.info("AUTO COLOR CHANGE "+before+ " => "+color);
			}
		}
		
		show.next(dmx,this);
		if(speed<fade||show.strob()) dmx.apply(0);
		else dmx.apply(fade);
	}
	private void autoColorNext(RGBColor[] colorseq){
		for(int i=0;i<colorseq.length;i++){
			if(color==colorseq[i]){
				int a = i+1;
				if(a==colorseq.length) a=0;
				color = colorseq[i+1];
				break;
			}
		}
	}
	public void strobospeed(long s){
		strobospeed = s;
		if(show!=null&&show.strob()) restart();
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
			stop();
			next();
			return;
		}
		if(_speed==0){
			// TAP
			if(timestamp>0){
				// adjust speed
				long newspeed =  System.currentTimeMillis() - timestamp;
				timestamp = System.currentTimeMillis();
				if(newspeed<10000) speed = newspeed;
			} else {
				// record timestamp
				timestamp = System.currentTimeMillis();
				next();
				return;
			}
		}else
			speed = _speed;
		// dmx runs at 44hz
		if(speed<20)speed=20;
		if(show!=null&&!show.strob()) restart();
	}
	
	private RGBColor color=RGBColor.MAUVE;
	private RGBColor[] coldcolorseq = {RGBColor.CYAN, RGBColor.MAUVE, RGBColor.VERT, RGBColor.BLEU};
	private RGBColor[] warmcolorseq = {RGBColor.JAUNE, RGBColor.ROUGE, RGBColor.AMBRE};
	public long autoColorTime=20000;
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
			if(!strob) dmx.override(new DmxOverride(solo.f,color.solo(),solo.dim, fade));
			else dmx.override(new DmxOverride(solo.f,color,solo.dim, fade));
		}
	}
	private Solo solo=null;
	public void solo(Solo s){
		if(solo!=null){
			// cancel previous override
			dmx.reset(solo.f);
			dmx.set(solo.f, RGBColor.BLACK);
			dmx.apply(0);
		}

		if(s.dim<0) {
			// cancel override only
			solo = null;
		} else {
			//set new override
			boolean strob = false;
			if(show!=null) strob = show.strob();
			dmx.override(new DmxOverride(s.f,strob||bgblack||blackout?color:color.solo(),s.dim, fade));
			solo = s;
		}
	}
	boolean blackout = true;
	public void blackout(){
		stop();
		dmx.blackout(fade);
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
}
