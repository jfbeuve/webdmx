package fr.jfbeuve.webdmx.gpio;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QuizzHelper {
	private static final Log log = LogFactory.getLog(QuizzHelper.class);
	private static final SoundManager sound = new SoundManager();
	
	private QuizzGpio gpio;
	
	private boolean frozen = false;
	
	public QuizzHelper(QuizzGpio _gpio){
		gpio=_gpio;
	}
	
	public void fire(String btn){

    	if(!frozen&&!btn.equals(GpioManager.BTN4)){
	    	if(btn.equals(GpioManager.BTN1)){
    			gpio.led1(true);
    			log.info(">> LED1");
	    	}else if(btn.equals(GpioManager.BTN2)){
	    		gpio.led2(true);
    			log.info(">> LED2");
    		}else if(btn.equals(GpioManager.BTN3)){
    			gpio.led3(true);
    			log.info(">> LED3");
    		}
	    	sound.play(SoundManager.BUZZER);
    		frozen=true;
    		log.info(">> +FREEZE");
    	}else if(frozen&&btn.equals(GpioManager.BTN4)){
    		log.info(">> -FREEZE");
    		frozen=false;
    		gpio.led1(false);
    		gpio.led2(false);
    		gpio.led3(false);
    	}
	}
}
