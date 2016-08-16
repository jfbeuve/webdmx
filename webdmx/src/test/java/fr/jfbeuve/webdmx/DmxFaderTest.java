package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.dmx.DmxDimmer;
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
		dmx.offline();
		
		ArrayList<Integer> channels = new ArrayList<Integer>();
		channels.add(17);
		dmx.init(DmxDimmer.MASTER, new DmxDimmer(dmx, channels));
		dmx.dim(DmxDimmer.MASTER, 127);
		
		//SNAP
		cue.set(11,255);
		cue.set(17,255);
		cue.apply(0);
		log.info("#### ASSERT 1 "+dmx.get(11).dim()+" / "+dmx.get(17).dim());
		assertEquals(255,dmx.get(11).dim());
		assertEquals(127,dmx.get(17).dim());

		//FADE
		cue.set(11,127);
		cue.set(17,127);
		cue.apply(2000);
		Thread.sleep(1000);
		log.info("#### ASSERT 2 "+dmx.get(11).dim()+" / "+dmx.get(17).dim());
		assertTrue(dmx.get(11).dim()>190&&dmx.get(11).dim()<210);
		Thread.sleep(2500);
		log.info("#### ASSERT 3 "+dmx.get(11).dim()+" / "+dmx.get(17).dim());
		assertEquals(127,dmx.get(11).dim());
		assertEquals(63,dmx.get(17).dim());
	}

}
