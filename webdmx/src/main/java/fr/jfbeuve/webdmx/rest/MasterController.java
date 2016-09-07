package fr.jfbeuve.webdmx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.dmx.DmxDimmer;
import fr.jfbeuve.webdmx.dmx.DmxOverrideMgr;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.Show;
import fr.jfbeuve.webdmx.show.ShowRunner;
import fr.jfbeuve.webdmx.show.Solo;

@Controller
public class MasterController {
	@Autowired
	private DmxOverrideMgr dmx;
	@Autowired
	private ShowRunner show;
	@Autowired
	private DmxWrapper io;
	
	//TODO /show/{name}/{speed}/{fade}
	@RequestMapping("/show/{name}")
	@ResponseBody
	public String show(@PathVariable("name") String name) {
		if(name.equals("blackout")) show.blackout();
		else show.start(Show.valueOf(name));
		return "OK";
	}
	@RequestMapping("/color/{color}")
	@ResponseBody
	public String color(@PathVariable("color") RGBColor color) {
		show.color(color);
		return "OK";
	}
	@RequestMapping("/autocolor/{time}")
	@ResponseBody
	public String color(@PathVariable("time") long time) {
		show.autocolor(time);
		return "OK";
	}
	@RequestMapping("/speed/{time}")
	@ResponseBody
	public String speed(@PathVariable("time") Long time) {
		show.speed(time);
		return "OK";
	}
	@RequestMapping("/strobospeed/{time}")
	@ResponseBody
	public String strobospeed(@PathVariable("time") Long time) {
		show.strobospeed(time);
		return "OK";
	}
	@RequestMapping("/solo/{name}/{dim}/{strob}")
	@ResponseBody
	public String fixture(@PathVariable("name") RGBFixture name, @PathVariable("dim") int dim,@PathVariable("strob") boolean strob) {
		show.solo(new Solo(name,dim,strob));
		return "OK";
	}
	@RequestMapping("/fade/{time}")
	@ResponseBody
	public String fade(@PathVariable("time") Long time) {
		show.fade(time);
		return "OK";
	}
	@RequestMapping("/bgblack/{b}")
	@ResponseBody
	public String bgblack(@PathVariable("b") Boolean b) {
		show.bgblack(b);
		return "OK";
	}
	/**
	 * set master dimmer value 0/255
	 **/
	@RequestMapping("/dim/{value}")
	@ResponseBody
	public String speed(@PathVariable("value") Integer value) {
		io.dim(DmxDimmer.MASTER, value);
		return "OK";
	}
}
