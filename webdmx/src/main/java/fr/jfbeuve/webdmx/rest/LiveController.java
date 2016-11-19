package fr.jfbeuve.webdmx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.sc.Scene;
import fr.jfbeuve.webdmx.sc.Override;
import fr.jfbeuve.webdmx.sc.Sequence;
import fr.jfbeuve.webdmx.sc.Sequencer;

@RestController
public class LiveController {
	@Autowired
	private DmxWrapper dmx;
	@Autowired
	private Sequencer chase;
	
	@RequestMapping(value = "/live/scene", method = RequestMethod.POST,consumes="application/json")
	public Object scene(@RequestBody final Scene s) {
		dmx.set(s);
		return null;
	}
	@RequestMapping(value = "/live/override", method = RequestMethod.POST,consumes="application/json")
	public Object override(@RequestBody final Override o) {
		dmx.set(o);
		return null;
	}
	@RequestMapping(value = "/live/sequence", method = RequestMethod.POST,consumes="application/json")
	public Object override(@RequestBody final Sequence s) {
		chase.play(s);
		return null;
	}
	@RequestMapping("/live/speed/{time}")
	public Object speed(@PathVariable("time") Long time) {
		chase.speed(time);
		return null;
	}
	@RequestMapping("/live/blackout/{time}")
	public Object blackout(@PathVariable("time") Long time) {
		dmx.blackout(time);
		return null;
	}
}
