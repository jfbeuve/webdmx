package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.fixture.FixtureType;
import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGBColor;

@Component
public class DmxCue {

	@Autowired
	private DmxWrapper dmx;
	
	private Map<Integer,Integer> values = new HashMap<Integer,Integer>();
	private Set<Integer> override = new HashSet<Integer>();
	
	/**
	 * apply dmx values
	 */
	public void apply(){
		dmx.set(values);
		values = new HashMap<Integer,Integer>();
	}
	/**
	 * store dmx value to apply if channel is not overridden
	 */
	public void set(int channel, int value){
		if(!override.contains(channel)) values.put(channel, value);
	}
	/**
	 * store dmx values to apply if channels are not overridden
	 */
	public void set(RGBFixture f, RGBColor c){
		values.put(f.red(), c.red());
		values.put(f.green(), c.green());
		values.put(f.blue(), c.blue());
	}
	/** 
	 * cancel override
	 */
	public void reset(){
		override = new HashSet<Integer>();
	}
	/** 
	 * cancel override
	 */
	public void reset(RGBFixture f){
		override.removeAll(f.channels());
	}
	public void set(DmxOverride o){
		RGBColor c = o.color();
		for (RGBFixture f : o.fixtures()) {
			override.addAll(f.channels());
			
			int dim = 255;
			
			if(f.type()==FixtureType.RGB7){
				values.put(f.dim(), o.dimmer());
				values.put(f.strob(), 0);
			}else{
				dim = o.dimmer();
			}
			
			values.put(f.red(), c.red()*dim/255);
			values.put(f.green(), c.green()*dim/255);
			values.put(f.blue(), c.blue()*dim/255);
		}
		apply();
		
	}
	public void reset(DmxOverride o){
		for (RGBFixture f : o.fixtures()) {
			override.removeAll(f.channels());
		}
	}
}
