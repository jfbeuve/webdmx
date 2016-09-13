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
import fr.jfbeuve.webdmx.dmx.DmxOverrideMgr;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.ShowRunner;
import fr.jfbeuve.webdmx.show.Solo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class OverrideTest {

	@Autowired
	private DmxWrapper dmx;
	@Autowired
	private DmxOverrideMgr cue;
	@Autowired
	private ShowRunner run;
	
	@Test
	public void testOverride() throws Exception {
		System.out.println("###### testOverride");
		dmx.offline();
		DmxDimmer.MASTER.value(255);
		DmxCue values = new DmxCue();
		
		// SET INITIAL STATE 
		values.set(11,255).set(17,127).set(24,127);
		cue.apply(0,values);
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
		values.reset().set(13,255).set(26,255);
		values.set(29,255); // the only one updated
		cue.apply(0,values);
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
		values.reset().set(17,100).set(28,100).set(29,100); 
		cue.apply(0,values);
		assertEquals(100,dmx.get(17).value());
		assertEquals(100,dmx.get(28).value());
		assertEquals(100,dmx.get(29).value()); // the only one updated
		
		// SOLO STROB
		Solo s = new Solo(RGBFixture.PAR1,255,true);
		cue.override(new DmxOverride(s,RGBColor.ROUGE,0));
		System.out.println("SOLO STROB ASSERT 1");
		assertEquals(255,dmx.get(24).value());
		Thread.sleep(101);
		System.out.println("SOLO STROB ASSERT 2");
		assertEquals(0,dmx.get(24).value());
		Thread.sleep(101);
		System.out.println("SOLO STROB ASSERT 3");
		assertEquals(255,dmx.get(24).value());
		Thread.sleep(101);
		System.out.println("SOLO STROB ASSERT 4");
		assertEquals(0,dmx.get(24).value());
		
		// TEST END
		cue.blackout(0);
	}
}
