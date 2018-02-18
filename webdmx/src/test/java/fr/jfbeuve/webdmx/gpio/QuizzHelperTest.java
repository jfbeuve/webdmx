package fr.jfbeuve.webdmx.gpio;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.jfbeuve.webdmx.gpio.GpioManager;
import fr.jfbeuve.webdmx.gpio.QuizzGpio;
import fr.jfbeuve.webdmx.gpio.QuizzHelper;

public class QuizzHelperTest implements QuizzGpio{
	boolean led1, led2, led3;
	
	@Test
	public void test() throws Exception {
		QuizzHelper quizz = new QuizzHelper(this);
		init();
		assertLeds(false,false,false);
		
		// BTN1
		quizz.fire(GpioManager.BTN1);
		quizz.fire(GpioManager.BTN2);
		quizz.fire(GpioManager.BTN3);
		assertLeds(true,false,false);
		
		// RAZ
		quizz.fire(GpioManager.BTN4);
		assertLeds(false,false,false);
		
		// BTN2
		quizz.fire(GpioManager.BTN2);
		quizz.fire(GpioManager.BTN1);
		quizz.fire(GpioManager.BTN3);
		assertLeds(false,true,false);
		
		// RAZ
		quizz.fire(GpioManager.BTN4);
		assertLeds(false,false,false);
		
		// BTN3
		quizz.fire(GpioManager.BTN3);
		quizz.fire(GpioManager.BTN2);
		quizz.fire(GpioManager.BTN1);
		assertLeds(false,false,true);
		
		// RAZ
		quizz.fire(GpioManager.BTN4);
		assertLeds(false,false,false);
}

	@Override
	public void led1(boolean on) {
		led1=on;
	}

	@Override
	public void led2(boolean on) {
		led2=on;
	}

	@Override
	public void led3(boolean on) {
		led3=on;
	}
	
	private void init(){
		led1=false; led2=false; led3=false;
	}
	
	private void assertLeds(boolean a, boolean b, boolean c){
		assertEquals(a, led1);
		assertEquals(b, led2);
		assertEquals(c, led3);
	}
}
