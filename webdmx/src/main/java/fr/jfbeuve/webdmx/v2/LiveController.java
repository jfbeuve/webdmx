package fr.jfbeuve.webdmx.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LiveController {
	@Autowired
	private DmxWrapper dmx;
	@Autowired
	private SceneSequencer chase;
	
	@RequestMapping(value = "/live/scene", method = RequestMethod.POST,consumes="application/json")
	public Object scene(@RequestBody final Scene s) {
		dmx.set(s);
		return null;
	}
	@RequestMapping(value = "/live/override", method = RequestMethod.POST,consumes="application/json")
	public Object override(@RequestBody final SceneOverride o) {
		dmx.set(o);
		return null;
	}
	@RequestMapping(value = "/live/sequence", method = RequestMethod.POST,consumes="application/json")
	public Object override(@RequestBody final SceneSequence s) {
		chase.play(s);
		return null;
	}
	@RequestMapping("/live/speed/{time}")
	public Object speed(@PathVariable("time") Long time) {
		chase.speed(time);
		return null;
	}
}
