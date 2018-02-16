package fr.jfbeuve.webdmx.gpio;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FogHelper {
	private static final Log log = LogFactory.getLog(FogHelper.class);
	
	public FogHelper(){}
	public FogHelper(FogGpio _gpio){
		gpio=_gpio;
	}
	
	@Autowired
	@Qualifier("gpioManager")
	private FogGpio gpio;
	
	private long fogtime=30000, sleeptime=30000, fogstart=0, sleepstart=0;
	private boolean auto=false;
	
	private FogTimer timer;
	
	/** get fog status for HTTP controller**/
	public FogStatus status(){
		return new FogStatus(fog(),auto, fogtime,sleeptime, ready());
	}
	
	/** is gpio ready? **/
	private boolean ready(){
		return gpio.btn1();
	}
	/** is gpio fog on? **/
	private boolean fog(){
		return gpio.led1();
	}
	
	/** gpio og ready listener **/
	public void ready(boolean ready){
		if(ready){
			if(fog()){
				// if fog, init fogstart
				fogstart=System.currentTimeMillis();
				// if autofog, start fog timer
				if(auto) timer(fogtime,false);
			}
		}else{
			// if fog, set fogtime
			if(!auto&&fog()) {
				fogtime = System.currentTimeMillis() - fogstart;
				gpio.led1(false);
			}
			sleepstart = System.currentTimeMillis();
			// if auto, start sleep timer?
		}
	}
	
	/** set manual fog status from HTTP controller **/
	public void fog(boolean fire){
		if(fog()==fire) return;
		log.info("FOG "+fire);
		if(fire){
			// start fog + init fogstart if ready
			gpio.led1(true);
			if(sleepstart>0) sleeptime=System.currentTimeMillis()-sleepstart;
			if(ready()) {
				fogstart=System.currentTimeMillis();
				// if auto, start fog timer
				if(auto) timer(fogtime,false);
			}
		} else {
			// stop fog + update fogtime
			gpio.led1(false);
			fogtime=System.currentTimeMillis()-fogstart;
			sleepstart = System.currentTimeMillis();
			// if auto : start sleep timer
			if(auto) timer(sleeptime, true);
		}
	}
	
	/** set auto fog status from HTTP controller **/
	public void auto(boolean fire){
		if(auto==fire) return;
		log.info("AUTO "+fire);
		if(fire){
			// start fog + start fog timer if ready
			gpio.led1(true);
			if(ready()) {
				timer(fogtime,false);
				fogstart=System.currentTimeMillis();
			}
			sleeptime=System.currentTimeMillis()-sleepstart;
		} else {
			// stop fog + stop timer
			gpio.led1(false);
			timer(0,false);
		}
		auto=fire;
	}
	
	/** set fog status from auto timer **/
	public void timer(boolean fire){
		log.debug("AUTO FOG "+fire);
		if(fire){
			// start fog + start fog timer if ready
			gpio.led1(true);
			if(ready()) {
				timer(fogtime,false);
				fogstart=System.currentTimeMillis();
			}
		} else {
			// stop fog + start sleep timer
			gpio.led1(false);
			timer(sleeptime,true);
			sleepstart=System.currentTimeMillis();
		}
	}
	/** 
	 * starts/stops timer 
	 * set time =-1 to stop timer
	 **/
	private synchronized void timer(long time, boolean action){
		if(timer!=null){
			if(timer.thread!=null) timer.thread.interrupt();
			timer=null;
		}
		if(time>0){
			timer = new FogTimer(time, action, this);
			timer.thread = new Thread(timer);
			timer.thread.start();
		}
	}
}
