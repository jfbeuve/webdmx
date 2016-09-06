package fr.jfbeuve.webdmx.dmx;

import java.util.Hashtable;
import java.util.Map;

import fr.jfbeuve.webdmx.fixture.FixtureType;
import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGBColor;

public class DmxCue {

	private Map<Integer,Integer> values;
	
	/**
	 * creates regular cue
	 */
	public DmxCue(){
		this(false);
	}
	/**
	 * @param true creates override cue
	 */
	public DmxCue(boolean _override){
		reset();
		override=_override;
	}
	/**
	 * store dmx value to apply if channel is not overridden
	 */
	public DmxCue set(int channel, int value){
		values.put(channel, value);
		return this;
	}
	/**
	 * store dmx values to apply if channels are not overridden
	 */
	public DmxCue set(RGBFixture f, RGBColor c){
		if(f.type()==FixtureType.PAR){
			int val = 0;
			if(c.red()>=c.green()&&c.red()>=c.blue()) val = c.red();
			if(c.green()>c.red()&&c.green()>c.blue()) val = c.green();
			if(c.blue()>c.green()&&c.blue()>c.red()) val = c.blue();
			set(f.dim(),val);
		}else{
			set(f.red(), c.red());
			set(f.green(), c.green());
			set(f.blue(), c.blue());
		}
		return this;
	}
	public Map<Integer,Integer> get(){
		return values;
	}
	public DmxCue reset(){
		values = new Hashtable<Integer,Integer>();
		return this;
	}
	/**
	 * @param true if this is an override command
	 */
	private boolean override;
	public boolean override(){
		return override;
	}
	
}
