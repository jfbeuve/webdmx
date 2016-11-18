package fr.jfbeuve.webdmx.v2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.jfbeuve.webdmx.Start;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class DmxWrapperTest {

	@Autowired
	private DmxWrapper dmx;
	
	@Test
	public void test() throws Exception {
		dmx.offline();
		
		//assertEquals(dmx.get(17).value(), 255);
	}

}
