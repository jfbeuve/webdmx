package fr.jfbeuve.webdmx.jetty.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.jetty.service.HelloWorldService;

@Controller
public class SampleController {
	
	@Autowired
	private HelloWorldService helloWorldService;

	@RequestMapping("/hello")
	@ResponseBody
	public String helloWorld() {
		return this.helloWorldService.getHelloMessage();
	}
	//TODO WebController html, js, css
	//TODO Reactjs GUI show.html
	//TODO Reactjs GUI disco.html
	//TODO Reactjs GUI theatre.html
}
