/*
 * Copyright 2012-2014 the original author or authors.
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

package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.jfbeuve.webdmx.Start;
import fr.jfbeuve.webdmx.dmx.DmxDimmer;
import fr.jfbeuve.webdmx.dmx.DmxWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class DmxDimmerTest {

	@Autowired
	private DmxWrapper dmx;
	
	@Test
	public void testMasterDimmer() throws Exception {
		dmx.offline();
		dmx.init(DmxDimmer.MASTER, new DmxDimmer(dmx, new int[]{17}));
		Map<Integer,Integer> values;
		
		values = new HashMap<Integer,Integer>();
		values.put(11,255);
		values.put(17,255);
		dmx.set(values);
		
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(dmx.get(17).dim(), 255);
		dmx.dim(DmxDimmer.MASTER, 127);
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(127,dmx.get(17).dim());
		
		values = new HashMap<Integer,Integer>();
		values.put(17, 127);
		dmx.set(values);
		
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(63,dmx.get(17).dim());
		dmx.dim(DmxDimmer.MASTER, 255);
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(dmx.get(17).dim(), 127);
		
		values = new HashMap<Integer,Integer>();
		values.put(17,255);
		dmx.set(values);
		
		assertEquals(dmx.get(11).dim(), 255);
		assertEquals(dmx.get(17).dim(), 255);
	}

}
