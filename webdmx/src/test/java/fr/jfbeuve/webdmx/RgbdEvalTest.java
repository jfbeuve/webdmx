package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.jfbeuve.webdmx.awt.DmxMonitor;
import fr.jfbeuve.webdmx.awt.RGB;
import fr.jfbeuve.webdmx.awt.RGBD;

public class RgbdEvalTest {
	
	@Test
	public void eval() throws Exception {
		RGBD c = new RGB(255, 255, 255).eval();
		assertEquals(255, c.r);
		assertEquals(255, c.g);
		assertEquals(255, c.b);
		assertEquals(100, c.d);
		
		c = new RGB(128, 128, 128).eval();
		assertEquals(255, c.r);
		assertEquals(255, c.g);
		assertEquals(255, c.b);
		assertEquals(50, c.d);
		
		c = new RGB(127, 255, 255).eval();
		assertEquals(127, c.r);
		assertEquals(255, c.g);
		assertEquals(255, c.b);
		assertEquals(100, c.d);
	}
	
}
