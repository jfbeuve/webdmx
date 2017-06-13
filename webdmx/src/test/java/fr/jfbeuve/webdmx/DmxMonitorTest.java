package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.jfbeuve.webdmx.swing.DmxMonitor;
import fr.jfbeuve.webdmx.swing.RGB;
import fr.jfbeuve.webdmx.swing.RGBD;

public class DmxMonitorTest {
	private int[] data = new int[512];
	private DmxMonitor m =new DmxMonitor(new int[512]);
	
	@Test
	public void eval() throws Exception {
		RGBD c = m.eval(new RGB(255, 255, 255));
		assertEquals(255, c.r);
		assertEquals(255, c.g);
		assertEquals(255, c.b);
		assertEquals(100, c.d);
		
		c = m.eval(new RGB(128, 128, 128));
		assertEquals(255, c.r);
		assertEquals(255, c.g);
		assertEquals(255, c.b);
		assertEquals(50, c.d);
		
		c = m.eval(new RGB(127, 255, 255));
		assertEquals(127, c.r);
		assertEquals(255, c.g);
		assertEquals(255, c.b);
		assertEquals(100, c.d);
	}
	
}
