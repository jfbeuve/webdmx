package fr.jfbeuve.webdmx.dmx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.show.RGB3Fixture;
import fr.jfbeuve.webdmx.show.RGBColor;

@Component
public class DmxCue {

	@Autowired
	private DmxWrapper dmx;
	
	private Map<Integer,Integer> values = new HashMap<Integer,Integer>();
	private List<Integer> override = new ArrayList<Integer>();
	
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
	public void set(RGB3Fixture f, RGBColor c){
		values.put(f.red(), c.red());
		values.put(f.green(), c.green());
		values.put(f.blue(), c.blue());
	}
	/**
	 * set override
	 */
	public void override(int channel, int value){
		override.add(channel);
		values.put(channel, value);
	}
	/**
	 * set override
	 */
	public void override(RGB3Fixture f, RGBColor c){
		override.addAll(f.channels());
		values.put(f.red(), c.red());
		values.put(f.green(), c.green());
		values.put(f.blue(), c.blue());
	}
	/** 
	 * cancel override
	 */
	public void reset(){
		override = new ArrayList<Integer>();
	}
	/** 
	 * cancel override
	 */
	public void reset(int channel){
		override.remove(channel);
	}
	/** 
	 * cancel override
	 */
	public void reset(RGB3Fixture f){
		override.removeAll(f.channels());
	}
}
