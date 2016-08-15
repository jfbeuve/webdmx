package fr.jfbeuve.webdmx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGB3Show;
import fr.jfbeuve.webdmx.show.RGB7Show;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Controller
public class MasterController {

	@Autowired
	private DmxCue dmx;
	@Autowired
	private ShowRunner show;
	@Autowired
	private RGB3Show rgb;
	@Autowired
	private RGB7Show front;
	
	@RequestMapping("/show/blackout")
	@ResponseBody
	public String blackout() {
		show.stop();
		dmx.set(RGBFixture.PAR1,RGBColor.BLACK);
		dmx.set(RGBFixture.PAR2,RGBColor.BLACK);
		dmx.set(RGBFixture.PAR3,RGBColor.BLACK);
		dmx.set(RGBFixture.PAR4,RGBColor.BLACK);
		dmx.set(RGBFixture.LEFT,RGBColor.BLACK);
		dmx.apply(0);
		return "OK";
	}
	@RequestMapping("/color/{color}")
	@ResponseBody
	public String color(@PathVariable("color") String color) {
		//TODO auto color
		rgb.setColor(RGBColor.valueOf(color));
		return "OK";
	}
	private boolean strob = false;

	@RequestMapping("/front/strob")
	@ResponseBody
	public String strob() {
		if(!strob){
			strob=true;
			show.stop();
			dmx.set(RGBFixture.PAR1,RGBColor.BLACK);
			dmx.set(RGBFixture.PAR2,RGBColor.BLACK);
			dmx.set(RGBFixture.PAR3,RGBColor.BLACK);
			dmx.set(RGBFixture.PAR4,RGBColor.BLACK);
			front.strob(true,false);
		}else{
			strob=false;
			front.strob(false,false);
			show.start();
		}
		dmx.apply(0);
		return "OK";
	}
	@RequestMapping("/speed/{time}")
	@ResponseBody
	public String speed(@PathVariable("time") Long time) {
		show.speed(time);
		return "OK";
	}
}
