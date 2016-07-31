package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;

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

import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.show.RGBColor;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@WebAppConfiguration
@IntegrationTest({"offline=true", "titi=toto"})
@DirtiesContext
public class HttpShowControllerTest {
	
	@Autowired
	private DmxWrapper dmx;

	@Test
	public void testHttpControllers() throws Exception {
		//dmx.offline();
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity("http://localhost/show/start", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertColors(RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.MAUVE);
		Thread.sleep(1500);
		assertColors(RGBColor.CYAN, RGBColor.MAUVE, RGBColor.MAUVE, RGBColor.CYAN);
		entity = new TestRestTemplate().getForEntity("http://localhost/front/strob", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertStrob(true);
		entity = new TestRestTemplate().getForEntity("http://localhost/front/off", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertStrob(false);
		//TODO assert color change, tap/next
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
			assertEquals(255, dmx.get(11).value());
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
