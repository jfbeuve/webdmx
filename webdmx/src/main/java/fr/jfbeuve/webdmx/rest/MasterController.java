package fr.jfbeuve.webdmx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.dmx.DmxDimmer;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.show.RockShow;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Controller
public class MasterController {
	//TODO auto color? master dimmer? override dimmer? override "apply" button, init service?
	@Autowired
	private DmxCue dmx;
	@Autowired
	private ShowRunner show;
	@Autowired
	private RockShow rgb;
	@Autowired
	private DmxWrapper io;
	
	@RequestMapping("/show/blackout")
	@ResponseBody
	public String blackout() {
		show.stop();
		dmx.blackout();
		return "OK";
	}
	@RequestMapping("/color/{color}")
	@ResponseBody
	public String color(@PathVariable("color") String color) {
		//TODO auto color
		rgb.setColor(RGBColor.valueOf(color));
		return "OK";
	}

	@RequestMapping("/front/strob")
	@ResponseBody
	public String strob() {
		show.strob();
		return "OK";
	}
	@RequestMapping("/speed/{time}")
	@ResponseBody
	public String speed(@PathVariable("time") Long time) {
		show.speed(time);
		return "OK";
	}
	/**
	 * set master dimmer value 0/255
	 **/
	@RequestMapping("/dim/{value}")
	@ResponseBody
	public String speed(@PathVariable("value") Integer value) {
		//TODO apply master dimmer only to ShowRunner = skip overrides ?
		io.dim(DmxDimmer.MASTER, value);
		return "OK";
	}
}
