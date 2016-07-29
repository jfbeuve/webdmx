package fr.jfbeuve.webdmx.jetty;

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

import fr.jfbeuve.webdmx.Start;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@WebAppConfiguration
@IntegrationTest({"offline=true", "titi=toto"})
@DirtiesContext
public class SampleJettyApplicationTests {
	
	@Autowired
	private DmxWrapper dmx;

	@Test
	public void testHome() throws Exception {
		//dmx.offline();
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost/hello", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals("Hello World", entity.getBody());
	}

}
