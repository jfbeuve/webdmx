package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.jfbeuve.webdmx.Start;
import fr.jfbeuve.webdmx.dmx.DmxDimmer;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class DmxDimmerTest {

	@Autowired
	private DmxWrapper dmx;
	
	@Test
	public void testMasterDimmer() throws Exception {
		dmx.offline();
		ArrayList<Integer> channels = new ArrayList<Integer>();
		channels.add(17);
		dmx.init(DmxDimmer.MASTER, new DmxDimmer(dmx, channels));
		Map<Integer,Integer> values;
		
		values = new HashMap<Integer,Integer>();
		values.put(11,255);
		values.put(17,255);
		dmx.set(values);
		
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(dmx.get(17).dim(), 255);
		dmx.dim(DmxDimmer.MASTER, 127);
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(127,dmx.get(17).dim());
		
		values = new HashMap<Integer,Integer>();
		values.put(17, 127);
		dmx.set(values);
		
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(63,dmx.get(17).dim());
		dmx.dim(DmxDimmer.MASTER, 255);
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(dmx.get(17).dim(), 127);
		
		values = new HashMap<Integer,Integer>();
		values.put(17,255);
		dmx.set(values);
		
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(dmx.get(17).dim(), 255);
	}

}
