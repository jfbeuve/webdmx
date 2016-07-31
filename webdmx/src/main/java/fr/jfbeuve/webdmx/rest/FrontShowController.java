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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jfbeuve.webdmx.dmx.DmxWrapper;
import fr.jfbeuve.webdmx.show.RGB3Fixture;
import fr.jfbeuve.webdmx.show.RGB7Fixture;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Controller
public class FrontShowController {
	
	@Autowired
	private ShowRunner show;
	
	@Autowired
	private DmxWrapper dmx;
	
	private boolean strob = false;

	@RequestMapping("/front/strob")
	@ResponseBody
	public String strob() {
		Map<Integer,Integer> values = new HashMap<Integer,Integer>();
		if(!strob){
			strob=true;
			show.stop();
			values.putAll(RGB3Fixture.PAR1.set(RGBColor.BLACK));
			values.putAll(RGB3Fixture.PAR2.set(RGBColor.BLACK));
			values.putAll(RGB3Fixture.PAR3.set(RGBColor.BLACK));
			values.putAll(RGB3Fixture.PAR4.set(RGBColor.BLACK));
			values.putAll(RGB7Fixture.LEFT.set(RGBColor.WHITE));
			values.putAll(RGB7Fixture.LEFT.strob(true));
		}else{
			strob=false;
			values.putAll(RGB7Fixture.LEFT.set(RGBColor.BLACK));
			values.putAll(RGB7Fixture.LEFT.strob(false));
			show.start();
		}
		dmx.set(values);
		return "OK";
	}
}
