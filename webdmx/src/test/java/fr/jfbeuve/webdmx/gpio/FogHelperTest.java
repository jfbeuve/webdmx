package fr.jfbeuve.webdmx.gpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.closeTo;

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
	public void fog() throws Exception {
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
	public void autonotready() throws Exception {
		// set fog time
		ready=true; fog=false;
		f = new FogHelper(this);
		assertio(true,false);
		f.fog(true);
		assertio(true,true);
		Thread.sleep(50);
		f.fog(false);
		assertio(true,false);
		Thread.sleep(100);
		ready(false);
		
		// test auto with ready false
		f.auto(true);
		assertio(false,true);
		Thread.sleep(50);
		ready(true);
		assertio(true,true);
		Thread.sleep(30);
		assertio(true,true);
		Thread.sleep(30);
		assertio(true,false);
		
		// auto stop
		f.auto(false);
	}
	
	@Test
	public void thread() throws Exception {
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
		log.info("## ASSERT auto true !!!");
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
	
	@Test
	public void time() throws Exception {
		// init
		ready=true; fog=false; 
		f = new FogHelper(this);
		asserttime(30000,30000);
		
		// ** happy use case, fog ready
		f.fog(true);
		assertTrue(fog);
		Thread.sleep(100);
		f.fog(false);
		assertFalse(fog);
		Thread.sleep(200);
		f.auto(true);
		assertTrue(fog);
		asserttime(100,200);
		f.auto(false);
		assertFalse(fog);
		asserttime(100,200);
		
		// ** adjusting auto, fog ready
		f.auto(true);
		asserttime(100,200);
		assertTrue(fog);
		Thread.sleep(50);
		f.fog(false);
		asserttime(50,200);
		assertFalse(fog);
		Thread.sleep(150);
		f.fog(true);
		asserttime(50,150);
		assertTrue(fog);
		
		// auto stop
		f.auto(false);
		assertFalse(fog);
		
		// ** auto + fog not ready
		ready=false;
		f.auto(true);
		asserttime(50,150);
		assertTrue(fog);
		Thread.sleep(30);
		ready(true);
		asserttime(50,150);
		Thread.sleep(30);
		assertTrue(fog);
		Thread.sleep(30);
		assertFalse(fog);
		asserttime(50,150);
		ready(false);
		Thread.sleep(60);
		ready(true);
		assertFalse(fog);
		asserttime(50,150);
		Thread.sleep(100);
		assertTrue(fog);
		
		// auto stop
		f.auto(false);
		assertFalse(fog);
		
		// ** happy use case, fog becomes ready
		ready=false; fog=false; 
		f = new FogHelper(this);
		asserttime(30000,30000);
		assertFalse(fog);
		f.fog(true);
		assertTrue(fog);
		Thread.sleep(80);
		ready(true);
		Thread.sleep(80);
		f.fog(false);
		asserttime(80,30000);
		
		// ** happy use case, fog becomes not ready
		ready=true; fog=false; 
		f = new FogHelper(this);
		asserttime(30000,30000);
		assertFalse(fog);
		ready(true);
		f.fog(true);
		Thread.sleep(40);
		ready(false);
		asserttime(40,30000);
		
		// ** auto before fog, fog ready
		ready=true; fog=false; 
		f = new FogHelper(this);
		asserttime(30000,30000);
		assertFalse(fog);
		f.auto(true);
		assertTrue(fog);
		asserttime(30000,30000);
		Thread.sleep(100);
		f.fog(false);
		asserttime(100,30000);
		assertFalse(fog);
		
		// auto stop
		f.auto(false);
		assertFalse(fog);
		assertFalse(f.status().auto);
	}
	
	private void asserttime(long fog, long sleep){
		assertThat((double)f.status().fogtime, closeTo(fog, 10));
		assertThat((double)f.status().sleeptime, closeTo(sleep, 10));
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
