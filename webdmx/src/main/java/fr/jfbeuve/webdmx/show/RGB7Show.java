package fr.jfbeuve.webdmx.show;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxCue;

@Component
public class RGB7Show {
	@Autowired
	private DmxCue dmx;
	
	/**
	 * turn on left strob if left = true
	 * turn off left strob if left = false
	 * turn on right strob if right = true
	 * turn off right strob if right = false
	 */
	public void strob(boolean left, boolean right){
		dmx.set(RGB7Fixture.LEFT,(left?RGBColor.WHITE:RGBColor.BLACK));
		dmx.set(RGB7Fixture.LEFT.strob(),(left?255:0));
		dmx.set(RGB7Fixture.LEFT.dim(),(left?255:0));
	}
}
