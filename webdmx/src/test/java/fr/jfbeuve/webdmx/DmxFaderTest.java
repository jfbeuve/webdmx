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

import fr.jfbeuve.webdmx.dmx.DmxDimmer;
import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class DmxFaderTest {
	private static final Log log = LogFactory.getLog(DmxFaderTest.class);

	@Autowired
	private DmxWrapper dmx;
	
	@Autowired
	private DmxCue cue;
	
	@Test
	public void testFader() throws Exception {
		System.out.println("###### testFader");
		dmx.offline();
		dmx.dim(DmxDimmer.MASTER, 127);
		
		//SNAP
		cue.set(11,255);
		cue.set(17,255);
		cue.apply(0);
		assertEquals(255,dmx.get(11).value());
		assertEquals(127,dmx.get(17).value());

		//FADE
		cue.set(11,127);
		cue.set(17,127);
		cue.apply(2000);
		Thread.sleep(1000);
		log.info("#### ASSERT 2 "+dmx.get(11).value()+" / "+dmx.get(17).value());
		assertTrue(dmx.get(11).value()>190&&dmx.get(11).value()<210);
		Thread.sleep(2500);
		log.info("#### ASSERT 3 "+dmx.get(11).value()+" / "+dmx.get(17).value());
		assertEquals(127,dmx.get(11).value());
		assertEquals(63,dmx.get(17).value());
	}

}
