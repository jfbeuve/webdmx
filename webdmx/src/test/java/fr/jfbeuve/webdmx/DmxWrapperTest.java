package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;

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
	
	@Test
	public void test() throws Exception {
		
		// INIT
		dmx.offline();
		seq.pause();
		dmx.blackout(0);

		// ASSERT BLACKOUT
		assertscene(0,0,0,0,0,0,0,0,0,0,0,0);
		
		// ALL RED
		dmx.set(new Scene(ALLRED,0));
		assertscene(255,0,0,255,0,0,255,0,0,255,0,0);
	}
	static final FixtureState[] ALLRED = {new FixtureState(0,100,255,0,0,false),new FixtureState(1,100,255,0,0,false),new FixtureState(2,100,255,0,0,false),new FixtureState(3,100,255,0,0,false)};
	
	private void assertscene(int r1, int g1, int b1, int r2, int g2, int b2,int r3, int g3, int b3,int r4, int g4, int b4) throws Exception{
		Thread.sleep(20);
		log.info("<ASSERT>");
		
		assertEquals(r1,dmx.read()[24]);
		assertEquals(g1,dmx.read()[25]);
		assertEquals(b1,dmx.read()[26]);
		
		assertEquals(r2,dmx.read()[27]);
		assertEquals(g2,dmx.read()[28]);
		assertEquals(b2,dmx.read()[29]);
		
		assertEquals(r3,dmx.read()[30]);
		assertEquals(g3,dmx.read()[31]);
		assertEquals(b3,dmx.read()[32]);
		
		assertEquals(r4,dmx.read()[33]);
		assertEquals(g4,dmx.read()[34]);
		assertEquals(b4,dmx.read()[35]);
	}
}
