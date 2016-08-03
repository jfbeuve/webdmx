package fr.jfbeuve.webdmx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.show.RGB3Fixture;
import fr.jfbeuve.webdmx.show.RGB3Show;
import fr.jfbeuve.webdmx.show.RGB7Fixture;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Controller
public class MasterShowController {

	@Autowired
	private DmxCue dmx;
	@Autowired
	private ShowRunner show;
	@Autowired
	private RGB3Show rgb;

	private boolean run = false;
	
	@RequestMapping("/show/run")
	@ResponseBody
	public String run() {
		if(!run){
			run=true;
			show.start(rgb);
		} else {
			run=false;
			show.stop();
			dmx.set(RGB3Fixture.PAR1,RGBColor.BLACK);
			dmx.set(RGB3Fixture.PAR2,RGBColor.BLACK);
			dmx.set(RGB3Fixture.PAR3,RGBColor.BLACK);
			dmx.set(RGB3Fixture.PAR4,RGBColor.BLACK);
			dmx.apply();
		}
		return "OK";
	}
	
	@RequestMapping("/show/next")
	@ResponseBody
	public String next() {
		show.next();
		return "OK";
	}
	
	@RequestMapping("/show/tap")
	@ResponseBody
	public String tap() {
		show.tap();
		return "OK";
	}
	
	@RequestMapping("/show/blackout")
	@ResponseBody
	public String blackout() {
		show.stop();
		dmx.set(RGB3Fixture.PAR1,RGBColor.BLACK);
		dmx.set(RGB3Fixture.PAR2,RGBColor.BLACK);
		dmx.set(RGB3Fixture.PAR3,RGBColor.BLACK);
		dmx.set(RGB3Fixture.PAR4,RGBColor.BLACK);
		dmx.set(RGB7Fixture.LEFT,RGBColor.BLACK);
		dmx.apply();
		return "OK";
	}
}
