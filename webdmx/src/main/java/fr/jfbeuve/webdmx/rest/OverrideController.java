package fr.jfbeuve.webdmx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.dmx.DmxOverride;

@RestController
public class OverrideController {
	//{"fixtures":["PAR1"],"color":"ROUGE","dimmer":255,"fade":0}
	
	@Autowired
	private DmxCue cue;
	
	@RequestMapping(value = "/override", method = RequestMethod.POST,consumes="application/json")
	public ResponseEntity<String> override(@RequestBody final DmxOverride o) {
		cue.override(o);
		return new ResponseEntity<String>("OK",HttpStatus.OK);
	}
}
