package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.jfbeuve.webdmx.dmx.DmxDimmer;
import fr.jfbeuve.webdmx.dmx.DmxOverrideMgr;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.ShowRunner;
import fr.jfbeuve.webdmx.show.Solo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class SoloTest {

	@Autowired
	private DmxWrapper dmx;
	@Autowired
	private DmxOverrideMgr cue;
	@Autowired
	private ShowRunner run;
	
	@Test
	public void testSolo() throws Exception {
		System.out.println("###### testSolo");
		dmx.offline();
		DmxDimmer.MASTER.value(255);
		run.fade(100);
		
		// ON fade
		Solo s = new Solo(RGBFixture.PAR1,255,false);
		run.solo(s);
		Thread.sleep(70);
		assertTrue(dmx.get(24).value()<255);
		Thread.sleep(70);
		assertEquals(255,dmx.get(24).value());

		// SWITCH SNAP
		s = new Solo(RGBFixture.PAR2,255,false);
		run.solo(s);
		Thread.sleep(1);
		assertEquals(0,dmx.get(24).value());
		assertEquals(255,dmx.get(27).value());
		
		// OFF fade
		s = new Solo(RGBFixture.PAR2,-1,false);
		run.solo(s);
		Thread.sleep(70);
		assertTrue(dmx.get(27).value()>0);
		Thread.sleep(70);
		assertEquals(0,dmx.get(27).value());
		
		cue.blackout(0);
	}
}
