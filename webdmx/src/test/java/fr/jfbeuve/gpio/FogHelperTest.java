package fr.jfbeuve.gpio;

import static org.junit.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import fr.jfbeuve.webdmx.gpio.FogGpio;
import fr.jfbeuve.webdmx.gpio.FogHelper;

public class FogHelperTest implements FogGpio{
	private static final Log log = LogFactory.getLog(FogHelperTest.class);
	private FogHelper f;
	boolean ready,fog;
	
	public FogHelperTest(){
	}
	
	@Test
	public void manual() throws Exception {
		// init
		ready=false; fog=false;
		f = new FogHelper(this);
		assertio(false,false);
		
		// ready
		ready=true;
		assertio(true,false);
		
		// fog
		f.fog(true);
		assertio(true,true);
		
		// not ready
		ready=false;
		assertio(false,true);
		
		
		// no fog
		f.fog(false);
		assertio(false,false);
	}

	@Test
	public void auto() throws Exception {
		// init
		ready=false; fog=false;
		f = new FogHelper(this);
		assertio(false,false);
		
		// ready
		ready=true;
		assertio(true,false);
		
		// set fog time
		f.fog(true);
		assertio(true,true);
		Thread.sleep(200);
		f.fog(false);
		assertio(true,false);
		
		// set sleep time
		Thread.sleep(300);
		
		// auto start
		f.auto(true);
		log.info("## ASSERT auto true");
		assertio(true,true);
		Thread.sleep(210);
		log.info("## ASSERT auto false");
		assertio(true,false);
		Thread.sleep(310);
		log.info("## ASSERT auto true");
		assertio(true,true);
		
		// change fog time and sleep time
		Thread.sleep(100);
		f.fog(false);
		assertio(true,false);
		Thread.sleep(50);
		f.fog(true);
		assertio(true,true);
		
		// assert auto change
		Thread.sleep(130);
		log.info("## ASSERT auto false");
		assertio(true,false);
		Thread.sleep(60);
		log.info("## ASSERT auto true");
		assertio(true,true);
		
		// auto stop
		f.auto(false);
		assertio(true,false);
	}
	
	private void ready(boolean _ready){
		ready=_ready;
		f.ready(ready);
	}
	
	@Test
	public void ready() throws Exception {
		// init
		ready=false; fog=false;
		f = new FogHelper(this);
		assertio(false,false);
		
		// set fog time
		f.fog(true);
		assertio(false,true);
		Thread.sleep(100);
		ready(true);
		assertio(true,true);
		Thread.sleep(100);
		ready(false);
		assertio(false,false);
		
		// set sleep time
		Thread.sleep(100);
		ready(true);
		Thread.sleep(100);
		ready(false);
		Thread.sleep(100);
		
		// auto start
		f.auto(true);
		Thread.sleep(50);
		ready(true);
		ready(false);
		log.info("## ASSERT auto true");
		assertio(false,true);
		Thread.sleep(110);
		log.info("## ASSERT auto false");
		assertio(false,false);
		Thread.sleep(310);
		log.info("## ASSERT auto true");
		assertio(false,true);
		Thread.sleep(200);
		log.info("## ASSERT auto true");
		assertio(false,true);
		ready(true);
		Thread.sleep(110);
		log.info("## ASSERT auto false");
		assertio(true,false);
		
		// auto stop
		f.auto(false);
		assertio(true,false);
	}
	
	@Override
	public boolean led1() {
		return fog;
	}

	@Override
	public void led1(boolean on) {
		fog=on;
	}

	@Override
	public boolean btn1() {
		return ready;
	}
	
	private void assertio(boolean _ready, boolean _fog){
		assertEquals(_ready, ready);
		assertEquals(_fog, fog);
	}
}
