package fr.jfbeuve.webdmx.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.show.RGB3Fixture;
import fr.jfbeuve.webdmx.show.RGB7Fixture;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.RGBShow;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Controller
public class MasterShowController {

	@Autowired
	private DmxWrapper dmx;
	@Autowired
	private ShowRunner show;
	@Autowired
	private RGBShow rgb;

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
			Map<Integer,Integer> values = new HashMap<Integer,Integer>();
			values.putAll(RGB3Fixture.PAR1.set(RGBColor.BLACK));
			values.putAll(RGB3Fixture.PAR2.set(RGBColor.BLACK));
			values.putAll(RGB3Fixture.PAR3.set(RGBColor.BLACK));
			values.putAll(RGB3Fixture.PAR4.set(RGBColor.BLACK));
			dmx.set(values);
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
		Map<Integer,Integer> values = new HashMap<Integer,Integer>();
		values.putAll(RGB3Fixture.PAR1.set(RGBColor.BLACK));
		values.putAll(RGB3Fixture.PAR2.set(RGBColor.BLACK));
		values.putAll(RGB3Fixture.PAR3.set(RGBColor.BLACK));
		values.putAll(RGB3Fixture.PAR4.set(RGBColor.BLACK));
		values.putAll(RGB7Fixture.LEFT.set(RGBColor.BLACK));
		dmx.set(values);
		return "OK";
	}
}
