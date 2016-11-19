package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.jfbeuve.webdmx.Start;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.sc.Scene;
import fr.jfbeuve.webdmx.sc.FixtureState;
import fr.jfbeuve.webdmx.sc.Sequencer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class DmxWrapperTest {
	private static final Log log = LogFactory.getLog(DmxWrapperTest.class);
	
	@Autowired
	private DmxWrapper dmx;
	@Autowired
	private Sequencer seq;
	
	private void init() throws Exception{
		// INIT
		dmx.offline();
		dmx.blackout(0);

		// ASSERT BLACKOUT
		Thread.sleep(20);
		assertRGB(0,0,0,0,0,0,0,0,0,0,0,0);
	}
	
	@Test
	public void layer1snap() throws Exception {
		init();
		
		// ALL RED
		dmx.set(new Scene(RGBW,0));
		Thread.sleep(20);
		assertRGB(255,0,0,0,255,0,0,0,255,127,127,127);
	}
	
	@Test
	public void layer1fade() throws Exception {
		init();
		
		// FADE IN
		dmx.set(new Scene(RGBW,100));
		Thread.sleep(50);
		assertRGBlower(200,0,0,0,200,0,0,0,200,100,100,100);
		assertRGBhigher(100,0,0,0,100,0,0,0,100,50,50,50);
		Thread.sleep(50);
		assertRGB(255,0,0,0,255,0,0,0,255,127,127,127);
		
		// FADE OUT
		dmx.set(new Scene(BLACKOUT,100));
		Thread.sleep(50);
		assertRGBlower(200,0,0,0,200,0,0,0,200,100,100,100);
		assertRGBhigher(100,0,0,0,100,0,0,0,100,50,50,50);
		Thread.sleep(50);
		assertRGB(0,0,0,0,0,0,0,0,0,0,0,0);
	}
	static final FixtureState[] RGBW = {new FixtureState(0,100,255,0,0,false),new FixtureState(1,100,0,255,0,false),new FixtureState(2,100,0,0,255,false),new FixtureState(3,50,255,255,255,false)};
	static final FixtureState[] BLACKOUT = {new FixtureState(0,0,0,0,0,false),new FixtureState(1,0,0,0,0,false),new FixtureState(2,0,0,0,0,false),new FixtureState(3,0,0,0,0,false)};
	
	private void assertRGB(int r1, int g1, int b1, int r2, int g2, int b2,int r3, int g3, int b3,int r4, int g4, int b4){
		assertRGB(24,r1,g1,b1);
		assertRGB(27,r2,g2,b2);
		assertRGB(30,r3,g3,b3);
		assertRGB(33,r4,g4,b4);
	}
	private void assertRGB(int ch, int r, int g, int b){
		log.info("<ASSERT "+ch+" r="+r+" g="+g+" b="+b+">");
		assertEquals(r,dmx.read()[ch]);
		assertEquals(g,dmx.read()[ch+1]);
		assertEquals(b,dmx.read()[ch+2]);
	}
	private void assertRGBlower(int ch, int r, int g, int b){
		log.info("<ASSERT "+ch+" r<"+r+" g<"+g+" b<"+b+">");
		assertTrue(r>=dmx.read()[ch]);
		assertTrue(g>=dmx.read()[ch+1]);
		assertTrue(b>=dmx.read()[ch+2]);
	}
	private void assertRGBlower(int r1, int g1, int b1, int r2, int g2, int b2,int r3, int g3, int b3,int r4, int g4, int b4){
		assertRGBlower(24,r1,g1,b1);
		assertRGBlower(27,r2,g2,b2);
		assertRGBlower(30,r3,g3,b3);
		assertRGBlower(33,r4,g4,b4);
	}
	private void assertRGBhigher(int ch, int r, int g, int b){
		log.info("<ASSERT "+ch+" r>"+r+" g>"+g+" b>"+b+">");
		assertTrue(r<=dmx.read()[ch]);
		assertTrue(g<=dmx.read()[ch+1]);
		assertTrue(b<=dmx.read()[ch+2]);
	}
	private void assertRGBhigher(int r1, int g1, int b1, int r2, int g2, int b2,int r3, int g3, int b3,int r4, int g4, int b4){
		assertRGBhigher(24,r1,g1,b1);
		assertRGBhigher(27,r2,g2,b2);
		assertRGBhigher(30,r3,g3,b3);
		assertRGBhigher(33,r4,g4,b4);
	}
	
}
