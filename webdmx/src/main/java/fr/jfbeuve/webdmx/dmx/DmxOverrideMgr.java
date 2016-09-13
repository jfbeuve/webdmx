package fr.jfbeuve.webdmx.dmx;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.fixture.FixtureType;
import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Component
public class DmxOverrideMgr {

	@Autowired
	private DmxWrapper dmx;
	
	@Autowired
	private ShowRunner show;
	
	private DmxFader fader;
	
	//private Set<Integer> override = new HashSet<Integer>();
	private Map<Integer,Integer> ov = new HashMap<Integer,Integer>();
	
	
	/**
	 * apply dmx values
	 * @param fade time in milliseconds. 0 means SNAP.
	 */
	public synchronized void apply(long fade, DmxCue cue){
		if(fader==null) fader = new DmxFader(dmx);
		
		Map<Integer,Integer> values = cue.get();
		if(!cue.override()) for(Integer ch:ov.keySet()){
			// removes overridden values and archives regular values for overridden channels
			ov.put(ch, values.get(ch));
			values.remove(ch);
		}
		
		if(fade==0||(fader.running()&&cue.override())) //SNAP
			dmx.set(values); 
		else //FADE
			fader.fade(fade,values);
	}
	
	public void blackout(long time){
		reset();
		DmxCue cue = dmx.blackout(this);
		apply(time,cue);
	}

	/** 
	 * cancel override
	 */
	public void reset(){
		//override = new HashSet<Integer>();
		ov = new HashMap<Integer,Integer>();
	}
	/** 
	 * cancel override and restores regular values
	 */
	public void reset(RGBFixture f){
		//override.removeAll(f.channels());
		strob.stop(f);
		DmxCue cue = new DmxCue();
		for(Integer ch:f.channels()){
			cue.set(ch, ov.get(ch));
			ov.remove(ch);
		}
		apply(0, cue);
	}
	
	@Autowired
	private DmxStrob strob;
	
	public void set(DmxOverride o){
		RGBColor c = o.color();
		DmxCue cue = new DmxCue(true);
		for (RGBFixture f : o.fixtures()) {
			//override.addAll(f.channels());
			for(Integer ch:f.channels()){
				// archives regular values
				ov.put(ch, dmx.get(ch).value(false));
			}
			int dim = o.dimmer();
			if(c==RGBColor.BLACK) dim = 0;
			
			if(f.type()==FixtureType.RGB7){
				int strob = (o.strob()?255:0);
				if(c==RGBColor.BLACK) strob = 0;
				cue.set(f.dim(), dim);
				cue.set(f.strob(), strob);
				dim = 255;
			}else{
				dim = o.dimmer();
			}
			
			cue.set(f.red(), c.red()*dim/255);
			cue.set(f.green(), c.green()*dim/255);
			cue.set(f.blue(), c.blue()*dim/255);
			
			if(f.type()==FixtureType.RGB3){
				if(o.strob())
					strob.start(f, c, dim);
				else
					strob.stop(f);
			}
		}
		apply(o.fade(), cue);
	}
	public void reset(DmxOverride o){
		for (RGBFixture f : o.fixtures()) 
			reset(f);
	}
	
	public void override(DmxOverride o){
		if(o.color()==null&&o.dimmer()==null) reset(o);
		else set(o);
	}
	public boolean isOverridden(int channel){
		//return override.contains(channel);
		return ov.containsKey(channel);
	}

}
