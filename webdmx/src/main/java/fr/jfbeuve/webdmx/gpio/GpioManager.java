package fr.jfbeuve.webdmx.gpio;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

@Component
public class GpioManager implements GpioPinListenerDigital, FogGpio, QuizzGpio{
		private static final Log log = LogFactory.getLog(GpioManager.class);
		
		private static GpioController gpio;
		private GpioPinDigitalInput btn1, btn2, btn3, btn4, btn5;
		private GpioPinDigitalOutput led1,led2,led3;
		public static final String BTN1 = "BTN1", BTN2 = "BTN2", BTN3 = "BTN3", BTN4="BTN4",BTN5="BTN5";
		private static final String LED1 = "LED1", LED2="LED2", LED3="LED3";
		
		private QuizzHelper quizz = new QuizzHelper(this);
		private FogHelper fog = new FogHelper(this);
		
		private boolean offline = false;
		private boolean offlineled1 = false;
		
	    public GpioManager(){
	    	try {
				gpio = GpioFactory.getInstance();
				
				btn1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05,BTN1,PinPullResistance.PULL_DOWN);
				led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,LED1,PinState.LOW);
				
				btn2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06,BTN2,PinPullResistance.PULL_DOWN);
				led2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02,LED2,PinState.LOW);
				
				btn3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_27,BTN3,PinPullResistance.PULL_DOWN);
				led3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03,LED3,PinState.LOW);
				
				btn4 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25,BTN4,PinPullResistance.PULL_DOWN);
				btn5 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,BTN5,PinPullResistance.PULL_DOWN);
				
				init();
			} catch (Error e) {
				offline=true;
			}
	    }
	    
	    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
	        GpioPin pin = event.getPin();
	        PinState state = event.getState();
	        log.debug("STATE CHANGE: " + event.getPin() + " = "+ event.getState());
	        toggle(pin,state);
	    }
	    
	    private void init(){
	        btn1.addListener(this);
	        btn1.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	        led1.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	        led1.low();
	        
	        btn2.addListener(this);
	        btn2.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	        led2.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	        led2.low();
	        
	        btn3.addListener(this);
	        btn3.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	        led3.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	        led3.low();
	        
	        btn4.addListener(this);
	        btn4.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	        btn5.addListener(this);
	        btn5.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	    }
	    @PreDestroy
	    public void shutdown(){
	    	gpio.shutdown();
	    }
	    private synchronized void toggle(GpioPin pin,PinState state){
	    	String nm = pin.getName();
	    	log.info(nm+" = "+state.getName());

	    	if(nm.equals(GpioManager.BTN4)||(!nm.equals(GpioManager.BTN4)&&btn4.isHigh())){
		    	if(state.isLow()) return;
	    		quizz.fire(nm);
	    	}else{
	    		fog.ready(btn1.isHigh());
	    	}
	    }
	    public void led1(boolean fire){
	    	offlineled1=fire;
	    	if(offline) return;
	    	if(fire)
	    		led1.high(); 
	    	else
	    		led1.low();
	    }
	    public void led2(boolean fire){
	    	if(fire)
	    		led2.high();
	    	else
	    		led2.low();
	    }
	    public void led3(boolean fire){
	    	if(fire)
	    		led3.high();
	    	else
	    		led3.low();
	    }
	    public boolean btn1(){
	    	if(offline) {
	    		return System.currentTimeMillis()%10000<5000;
	    	}
	    	return btn1.isHigh();
	    }
	    public boolean led1(){
	    	if(offline) return offlineled1;
	    	return led1.isHigh();
	    }
}