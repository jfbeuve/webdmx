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
import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.RGBShow;
import fr.jfbeuve.webdmx.show.ShowRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class ShowRunnerTest {

	private static final Log log = LogFactory.getLog(ShowRunnerTest.class);
	
	@Autowired
	private DmxWrapper dmx;
	@Autowired
	private ShowRunner show;
	
	@Test
	public void testShowRunner() throws Exception {
		
		//INIT
		dmx.offline();
		DmxDimmer.MASTER.value(255);
		show.fadeThreshold(2000);
		show.color(RGBColor.MAUVE);
		show.color(RGBColor.AUTO);
		show.autoColorTime=3200;
		show.speed(1000);
		show.set(RGBShow.ROCK);
		show.start();
		
		log.info("###### ASSERT 1");
		assertColors(RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE);
		Thread.sleep(1200);
		log.info("###### ASSERT 2");
		assertColors(RGBColor.CYAN, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.CYAN);
		show.speed(500);
		log.info("###### ASSERT 3");
		assertColors(RGBColor.MAUVE, RGBColor.CYAN, RGBColor.CYAN, RGBColor.MAUVE);
		Thread.sleep(600);
		log.info("###### ASSERT 4");
		assertColors(RGBColor.CYAN, RGBColor.MAUVE, RGBColor.CYAN, RGBColor.MAUVE);
		Thread.sleep(500);
		log.info("###### ASSERT 5");
		assertColors(RGBColor.MAUVE, RGBColor.CYAN, RGBColor.MAUVE, RGBColor.CYAN);
		
		// auto fade 250ms
		show.fadeThreshold(400);
		Thread.sleep(600);
		// assert fading in progress
		log.info("###### ASSERT 6 != step 1"); 
		assertTrue(!isColors(RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE));
		Thread.sleep(200);
		// assert step > 4 skipped 0 back to step 1 since we are in fade show
		log.info("###### ASSERT 6 = step 1"); 
		assertColors(RGBColor.CYAN, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.CYAN);
		
		// auto color change
		Thread.sleep(500);
		log.info("###### ASSERT 7 = step 2"); 
		assertColors(RGBColor.JAUNE, RGBColor.ROUGE, RGBColor.ROUGE, RGBColor.JAUNE);
	}
	private void assertColors(RGBColor a, RGBColor b, RGBColor c, RGBColor d){
		assertEquals(a.red(), dmx.get(24).value());
		assertEquals(a.green(), dmx.get(25).value());
		assertEquals(a.blue(), dmx.get(26).value());
		
		assertEquals(b.red(), dmx.get(27).value());
		assertEquals(b.green(), dmx.get(28).value());
		assertEquals(b.blue(), dmx.get(29).value());
		
		assertEquals(c.red(), dmx.get(30).value());
		assertEquals(c.green(), dmx.get(31).value());
		assertEquals(c.blue(), dmx.get(32).value());
		
		assertEquals(d.red(), dmx.get(33).value());
		assertEquals(d.green(), dmx.get(34).value());
		assertEquals(d.blue(), dmx.get(35).value());
	}
	private boolean isColors(RGBColor a, RGBColor b, RGBColor c, RGBColor d){
		if(a.red()!=dmx.get(24).value()) return false;
		if(a.green()!=dmx.get(25).value()) return false;
		if(a.blue()!=dmx.get(26).value()) return false;
		
		if(b.red()!=dmx.get(27).value()) return false;
		if(b.green()!=dmx.get(28).value()) return false;
		if(b.blue()!=dmx.get(29).value()) return false;
		
		if(c.red()!=dmx.get(30).value()) return false;
		if(c.green()!=dmx.get(31).value()) return false;
		if(c.blue()!=dmx.get(32).value()) return false;
		
		if(d.red()!=dmx.get(33).value()) return false;
		if(d.green()!=dmx.get(34).value()) return false;
		if(d.blue()!=dmx.get(35).value()) return false;
		
		return true;
	}
}
