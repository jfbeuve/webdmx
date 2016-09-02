package fr.jfbeuve.webdmx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.show.FadeType;
import fr.jfbeuve.webdmx.show.Show;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Controller
public class TheatreController {
	@Autowired
	private DmxCue dmx;
	@Autowired
	private ShowRunner show;
	
	private boolean lightOn=false;
	
	@RequestMapping("/theatre/speed/{time}")
	@ResponseBody
	public String speed(@PathVariable("time") Long time) {
		show.speed(time);
		return "OK";
	}
	@RequestMapping("/theatre/fade/{time}")
	@ResponseBody
	public String fade(@PathVariable("time") Long time) {
		FadeType type = (lightOn?FadeType.OUT:FadeType.IN);
		//FIXME all.fade(type, time);
		lightOn=!lightOn;
		return "OK";
	}
	@RequestMapping("/theatre/chase")
	@ResponseBody
	public String chase() {
		show.start(Show.CHASE);
		return "OK";
	}
	@RequestMapping("/theatre/off")
	@ResponseBody
	public String off() {
		show.stop();
		//FIXME all.fade(FadeType.OUT, 0);
		return "OK";
	}
	@RequestMapping("/theatre/all")
	@ResponseBody
	public String all() {
		show.start(Show.STROBO);
		return "OK";
	}
}
