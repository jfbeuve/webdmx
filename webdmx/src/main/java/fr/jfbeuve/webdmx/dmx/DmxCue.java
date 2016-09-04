package fr.jfbeuve.webdmx.dmx;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.fixture.FixtureType;
import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Component
public class DmxCue {

	@Autowired
	private DmxWrapper dmx;
	
	@Autowired
	private ShowRunner show;
	
	private DmxFader fader;
	
	private Map<Integer,Integer> values = new Hashtable<Integer,Integer>();
	private Set<Integer> override = new HashSet<Integer>();
	
	/**
	 * apply dmx values
	 * @param fade time in milliseconds. 0 means SNAP.
	 */
	public synchronized void apply(long fade){
		Map<Integer,Integer> todo = values;
		values = new Hashtable<Integer,Integer>();
		
		if(fader!=null)fader.interupt();
		if(fade>0){ //FADE
			fader = new DmxFader(dmx, todo);
			fader.fade(fade);
		}else{ //SNAP
			dmx.set(todo);
		}
	}
	public void blackout(long time){
		if(fader!=null)fader.interupt();
		reset();
		dmx.blackout(this);
		apply(time);
	}
	/**
	 * store dmx value to apply if channel is not overridden
	 */
	public void set(int channel, int value){
		if(!override.contains(channel))	values.put(channel, value);
	}
	/**
	 * store dmx values to apply if channels are not overridden
	 */
	public void set(RGBFixture f, RGBColor c){
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
			
			int dim = o.dimmer();
			if(c==RGBColor.BLACK) dim = 0;
			
			
			if(f.type()==FixtureType.RGB7){
				int strob = (o.strob()?255:0);
				if(c==RGBColor.BLACK) strob = 0;
				values.put(f.dim(), dim);
				values.put(f.strob(), strob);
				dim = 255;
			}else{
				dim = o.dimmer();
			}
			
			values.put(f.red(), c.red()*dim/255);
			values.put(f.green(), c.green()*dim/255);
			values.put(f.blue(), c.blue()*dim/255);
		}
		apply(o.fade());
		
	}
	public void reset(DmxOverride o){
		for (RGBFixture f : o.fixtures()) {
			override.removeAll(f.channels());
			if(f==RGBFixture.LEFT){
				set(f,RGBColor.BLACK);
				apply(0);
			}
		}
	}
	
	public void override(DmxOverride o){
		if(o.color()==null&&o.dimmer()==null) reset(o);
		else set(o);
	}
	public boolean isOverridden(int channel){
		return override.contains(channel);
	}

}
