package fr.jfbeuve.webdmx.dmx;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	
	private Set<Integer> override = new HashSet<Integer>();
	
	/**
	 * apply dmx values
	 * @param fade time in milliseconds. 0 means SNAP.
	 */
	public synchronized void apply(long fade, DmxCue cue){
		
		Map<Integer,Integer> values = cue.get();
		if(!cue.override()) for(Integer ch:override) values.remove(ch);
		
		if(fader!=null)fader.interupt();
		if(fade>0){ //FADE
			fader = new DmxFader(dmx, values);
			fader.fade(fade);
		}else{ //SNAP
			dmx.set(values);
		}
	}
	public void blackout(long time){
		if(fader!=null)fader.interupt();
		reset();
		DmxCue cue = dmx.blackout(this);
		apply(time,cue);
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
		DmxCue cue = new DmxCue(true);
		for (RGBFixture f : o.fixtures()) {
			override.addAll(f.channels());
			
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
		}
		apply(o.fade(), cue);
	}
	public void reset(DmxOverride o){
		DmxCue cue = new DmxCue();
		for (RGBFixture f : o.fixtures()) {
			override.removeAll(f.channels());
			if(f==RGBFixture.LEFT){
				cue.set(f,RGBColor.BLACK);
				apply(0, cue);
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
