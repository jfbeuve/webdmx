package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.dmx.DmxDimmer;
import fr.jfbeuve.webdmx.dmx.DmxOverride;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class OverrideTest {

	@Autowired
	private DmxWrapper dmx;
	@Autowired
	private DmxCue cue;
	
	@Test
	public void testShowRunner() throws Exception {
		dmx.offline();
		DmxDimmer.MASTER.value(255);
		
		// SET INITIAL STATE 
		cue.set(11,255);
		cue.set(17,127);
		cue.set(24,127);
		cue.apply(0);
		assertEquals(255,dmx.get(11).value());
		assertEquals(127,dmx.get(17).value());
		assertEquals(127,dmx.get(24).value());
		
		// SET OVERRIDE
		cue.override(new DmxOverride(new String[]{"LEFT","PAR1"}, "VERT", 200));
		assertEquals(0,dmx.get(11).value());
		assertEquals(255,dmx.get(12).value());
		assertEquals(200,dmx.get(17).value());
		assertEquals(0,dmx.get(24).value());
		assertEquals(200,dmx.get(25).value());
		
		// REGULAR DMX UPDATE DO NOT INFLUENCE OVERRIDDEN CHANNELS
		cue.set(13,255);
		cue.set(26,255);
		cue.set(29,255); // the only one updated
		cue.apply(0);
		assertEquals(0,dmx.get(13).value());
		assertEquals(0,dmx.get(26).value());
		assertEquals(255,dmx.get(29).value()); // the only one updated
		assertEquals(255,dmx.get(12).value());
		assertEquals(200,dmx.get(17).value());
		assertEquals(200,dmx.get(25).value());
		
		// UPDATE OVERRIDES
		cue.override(new DmxOverride(new String[]{"LEFT","PAR1"}, "ROUGE", 150));
		assertEquals(255,dmx.get(11).value());
		assertEquals(0,dmx.get(12).value());
		assertEquals(150,dmx.get(17).value());
		assertEquals(150,dmx.get(24).value());
		assertEquals(0,dmx.get(25).value());
		assertEquals(255,dmx.get(29).value()); // not overridden
		
		// CANCEL OVERRIDES
		cue.reset();
		cue.set(17,100);
		cue.set(28,100);
		cue.set(29,100); 
		cue.apply(0);
		assertEquals(100,dmx.get(17).value());
		assertEquals(100,dmx.get(28).value());
		assertEquals(100,dmx.get(29).value()); // the only one updated
	
	}

}
