package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import fr.jfbeuve.webdmx.dmx.DmxDimmer;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.ShowRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@WebAppConfiguration
@IntegrationTest({"offline=true", "titi=toto"})
@DirtiesContext
public class HttpShowControllerTest {
	private static final Log log = LogFactory.getLog(HttpShowControllerTest.class);
	
	@Autowired
	private DmxWrapper dmx;
	
	@Autowired
	private ShowRunner show;

	@Test
	public void testHttpControllers() throws Exception {
		//INIT
		DmxDimmer.MASTER.value(255);
		show.autoColorTime=180000;
		show.fade(2000);
		
		//SHOW
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity("http://localhost/speed/1000", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		log.info("###### ASSERT 1");
		assertColors(RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE); //1
		Thread.sleep(1500);
		log.info("###### ASSERT 2");
		assertColors(RGBColor.CYAN, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.CYAN); //2
		
		//STROB
		entity = new TestRestTemplate().getForEntity("http://localhost/front/strob", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertStrob(true);
		log.info("###### ASSERT STROB");
		assertColors(RGBColor.BLACK, RGBColor.BLACK, RGBColor.BLACK, RGBColor.BLACK);
		entity = new TestRestTemplate().getForEntity("http://localhost/front/strob", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertStrob(false);
		
		//COLOR
		log.info("###### ASSERT 3");
		assertColors(RGBColor.MAUVE, RGBColor.CYAN, RGBColor.CYAN, RGBColor.MAUVE); //3
		entity = new TestRestTemplate().getForEntity("http://localhost/color/ROUGE", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		
		//TAP
		entity = new TestRestTemplate().getForEntity("http://localhost/speed/400", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		log.info("###### ASSERT 4");
		assertColors(RGBColor.JAUNE, RGBColor.ROUGE, RGBColor.JAUNE, RGBColor.ROUGE); //4
		Thread.sleep(600);
		log.info("###### ASSERT 5");
		assertColors(RGBColor.ROUGE, RGBColor.JAUNE, RGBColor.ROUGE, RGBColor.JAUNE); //5
		
		//BLACKOUT
		entity = new TestRestTemplate().getForEntity("http://localhost/show/blackout", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertBlackout();
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
	private void assertStrob(boolean strob){
		if(strob){
			assertEquals(0, dmx.get(11).value());
			assertEquals(255, dmx.get(12).value());
			assertEquals(255, dmx.get(13).value());
			assertEquals(255, dmx.get(15).value());
			assertEquals(255, dmx.get(17).value());
		} else {
			assertEquals(0, dmx.get(15).value());
		}
	}
	private void assertBlackout(){
		assertEquals(0, dmx.get(17).value());
		assertColors(RGBColor.BLACK, RGBColor.BLACK, RGBColor.BLACK, RGBColor.BLACK);
	}
}
