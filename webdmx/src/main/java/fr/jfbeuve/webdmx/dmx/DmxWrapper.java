package fr.jfbeuve.webdmx.dmx;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.io.OlaWeb;
import fr.jfbeuve.webdmx.sc.Scene;
import fr.jfbeuve.webdmx.sc.RGBFixtureState;
import fr.jfbeuve.webdmx.sc.ScOverride;
import fr.jfbeuve.webdmx.sc.Sequencer;

@Component
public class DmxWrapper {
	private static final Log log = LogFactory.getLog(DmxWrapper.class);
	
	@Autowired
	private Sequencer chase;
	
	private int[] data;
	private RGBFixture[] fixture;
	
	private DmxThread thread;
	
	public DmxWrapper(){
		data = new int[512];
		for(int i=0;i<512;i++) data[i]=0;
		
		fixture=new RGBFixture[4];
		fixture[0]=new RGBFixture(24);
		fixture[1]=new RGBFixture(27);
		fixture[2]=new RGBFixture(30);
		fixture[3]=new RGBFixture(33);
		
		thread=new DmxThread(this);
	}

	@Autowired
	public OlaWeb io;
	
	@Value("${offline:false}")
	private boolean offline;
	
	public void offline() {
		offline = true;
	}
	/**
	 * sets scene to layer 0
	 */
	public void set(Scene sc){
		log.info("SET "+sc);
		for(RGBFixtureState f:sc.fixtures)
			fixture[f.id].set(f,sc.fade);
		thread.apply();
	}
	/**
	 * sets directly values to dmx channels
	 */
	public void set(Map<Integer,Integer> values){
		for(Map.Entry<Integer,Integer> entry:values.entrySet())
			data[entry.getKey()]=entry.getValue();
		thread.apply();
	}
	/**
	 * overrides scene
	 */
	public void override(ScOverride o){
		log.info(o);
		for(RGBFixtureState f:o.override)
			fixture[f.id].override(f,o.fade, o.layer);
		for(int i=0;i<o.reset.length;i++)
			fixture[o.reset[i]].reset(o.layer);
		thread.apply();
	}

	boolean apply(boolean strob){
		long timestamp = System.currentTimeMillis();
		boolean completed = true;
		for(int i=0;i<fixture.length;i++)
			if(fixture[i].apply(data, strob, timestamp)==false) completed=false;
		if(!offline) io.send(data);
		return completed; 
	}
	public void blackout(long fade){
		log.info("BLACKOUT");
		chase.pause();
		for(int i=0;i<fixture.length;i++){
			fixture[i].reset(-1);
			fixture[i].set(new RGBFixtureState(i), fade);
		}
		thread.apply();
	}
	/**
	 * @return dmx values for JUnit purpose
	 */
	public int[] read(){
		return data;
	}
}
