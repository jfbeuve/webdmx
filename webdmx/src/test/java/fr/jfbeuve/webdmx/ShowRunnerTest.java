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

import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.RockShow;
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
	@Autowired
	private RockShow rgb;
	
	@Test
	public void testShowRunner() throws Exception {
		dmx.offline();
		show.color(RGBColor.MAUVE);
		show.speed(1000);
		log.info("###### ASSERT 1");
		assertColors(RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE);
		Thread.sleep(1200);
		log.info("###### ASSERT 2");
		assertColors(RGBColor.CYAN, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.CYAN);
		show.speed(500);
		log.info("###### ASSERT 4");
		assertColors(RGBColor.MAUVE, RGBColor.CYAN, RGBColor.CYAN, RGBColor.MAUVE);
		Thread.sleep(600);
		log.info("###### ASSERT 5");
		assertColors(RGBColor.CYAN, RGBColor.MAUVE, RGBColor.CYAN, RGBColor.MAUVE);
		Thread.sleep(500);
		log.info("###### ASSERT 6");
		assertColors(RGBColor.MAUVE, RGBColor.CYAN, RGBColor.MAUVE, RGBColor.CYAN);
		
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
}
