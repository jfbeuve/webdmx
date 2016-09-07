package fr.jfbeuve.webdmx.dmx;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.ShowRunner;

@Component
public class DmxStrob implements Runnable{
	//TODO Junit DmxStrob
	private Thread t=null;
	private Map<RGBFixture, Entry> fc = new Hashtable<RGBFixture, Entry>();
	private static final Log log = LogFactory.getLog(DmxStrob.class);
	private long speed=100;
	
	@Autowired
	private ShowRunner show;
	
	@Autowired
	private DmxOverrideMgr dmx;
	
	@Override
	public void run() {
		speed=show.strobospeed();
		
		DmxCue cue=new DmxCue(true);
		boolean on=true;
		while(!fc.isEmpty()){
			cue.reset();
			
			for(Entry e: fc.values()){ 
				if(on){ 
					cue.set(e.f.red(), e.r);
					cue.set(e.f.green(), e.g);
					cue.set(e.f.blue(), e.b);
				}else cue.set(e.f, RGBColor.BLACK);
			}
			dmx.apply(0, cue);
			
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				log.debug(e,e);
			}
			
			on=!on;
		}
		t=null;
	}
	
	public synchronized void start(RGBFixture f, RGBColor c, int d){
		remove(f);
		fc.put(f,new Entry(f,c,d));
		if(t==null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	public void stop(RGBFixture f){
		remove(f);
	}
	
	private void remove(RGBFixture f){
		fc.remove(f);
	}
	
	public class Entry {
		
		public Entry(RGBFixture f, RGBColor c, int d) {
			this.f = f;
			this.r = c.red() * d / 255;
			this.g = c.green() * d / 255;
			this.b = c.blue() * d / 255;
		}
		RGBFixture f;
		int r,g,b;
	}
	public void speed(long _speed){
		speed = _speed;
	}
}
