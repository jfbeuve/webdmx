/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.jfbeuve.webdmx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.RGBShow;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Controller
public class ColorController {
	@Autowired
	private ShowRunner show;
	@Autowired
	private RGBShow rgb;

	@RequestMapping("/color/rouge")
	@ResponseBody
	public String rouge() {
		rgb.setColor(RGBColor.ROUGE);
		return "OK";
	}
	@RequestMapping("/color/vert")
	@ResponseBody
	public String vert() {
		rgb.setColor(RGBColor.VERT);
		return "OK";
	}
	@RequestMapping("/color/bleu")
	@ResponseBody
	public String bleu() {
		rgb.setColor(RGBColor.BLEU);
		return "OK";
	}
	@RequestMapping("/color/mauve")
	@ResponseBody
	public String mauve() {
		rgb.setColor(RGBColor.MAUVE);
		return "OK";
	}
	@RequestMapping("/color/cyan")
	@ResponseBody
	public String cyan() {
		rgb.setColor(RGBColor.CYAN);
		return "OK";
	}	
	@RequestMapping("/color/jaune")
	@ResponseBody
	public String jaune() {
		rgb.setColor(RGBColor.JAUNE);
		return "OK";
	}	
	@RequestMapping("/color/ambre")
	@ResponseBody
	public String ambre() {
		rgb.setColor(RGBColor.AMBRE);
		return "OK";
	}
	@RequestMapping("/color/blanc")
	@ResponseBody
	public String blanc() {
		rgb.setColor(RGBColor.WHITE);
		return "OK";
	}
	@RequestMapping("/color/noir")
	@ResponseBody
	public String noir() {
		rgb.setColor(RGBColor.BLACK);
		return "OK";
	}
}
