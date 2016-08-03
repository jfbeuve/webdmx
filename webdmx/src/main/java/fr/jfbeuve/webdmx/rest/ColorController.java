package fr.jfbeuve.webdmx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.RGB3Show;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Controller
public class ColorController {
	@Autowired
	private ShowRunner show;
	@Autowired
	private RGB3Show rgb;

	@RequestMapping("/color/{color}")
	@ResponseBody
	public String color(@PathVariable String color) {
		rgb.setColor(RGBColor.valueOf(color));
		return "OK";
	}
}
