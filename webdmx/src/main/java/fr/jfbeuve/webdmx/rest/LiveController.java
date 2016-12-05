package fr.jfbeuve.webdmx.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.preset.SequencePreset;
import fr.jfbeuve.webdmx.sc.ScOverride;
import fr.jfbeuve.webdmx.sc.Scene;
import fr.jfbeuve.webdmx.sc.Sequencer;

@RestController
public class LiveController {
	@Autowired
	private DmxWrapper dmx;
	@Autowired
	private Sequencer chase;
	
	@RequestMapping(value = "/live/scene", method = RequestMethod.POST,consumes="application/json")
	public Object scene(@RequestBody final Scene s) {
		//TODO get speed at the same time
		dmx.set(s);
		return null;
	}
	@RequestMapping(value = "/live/override", method = RequestMethod.POST,consumes="application/json")
	public Object override(@RequestBody final ScOverride o) {
		dmx.override(o);
		return null;
	}
	@RequestMapping(value = "/live/play", method = RequestMethod.POST,consumes="application/json")
	public Object play(@RequestBody final Scene[] s) {
		chase.play(s);
		return null;
	}
	@RequestMapping(value = "/live/data", method = RequestMethod.POST,consumes="application/json")
	public Object data(@RequestBody final Map<Integer,Integer> v) {
		dmx.set(v);
		return null;
	}
	/**
	 * speed in ms
	 * 0 =  pause + next
	 * -1 = stop
	 */
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
	@RequestMapping("/live/sequence.json")
	public Object sequence() {
		return new SequencePreset();
	}
}
