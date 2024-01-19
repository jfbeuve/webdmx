package fr.jfbeuve.webdmx.dmx;

import java.util.Map;

import fr.jfbeuve.webdmx.fixture.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.awt.DmxMonitor;
import fr.jfbeuve.webdmx.io.OlaWeb;
import fr.jfbeuve.webdmx.sc.RGBFixtureState;
import fr.jfbeuve.webdmx.sc.ScOverride;
import fr.jfbeuve.webdmx.sc.Scene;
import fr.jfbeuve.webdmx.sc.Sequencer;

@Component
public class DmxWrapper {
	private static final Log log = LogFactory.getLog(DmxWrapper.class);
	private DmxMonitor frame = null;
	
	@Autowired
	private Sequencer chase;
	
	private int[] data;
	private Fixture[] fixture;
	
	private DmxThread thread;
	
	public DmxWrapper(){
		data = new int[512];
		for(int i=0;i<512;i++) data[i]=0;
		
		fixture=new Fixture[13];
		// RGB 8W
		fixture[0]=new RGBFixture(2);
		fixture[1]=new RGBFixture(7);
		fixture[2]=new RGBFixture(12);
		fixture[3]=new RGBFixture(17);
		// RGB 10 MM
		fixture[4]=new RGBDSFixture(31);
		fixture[5]=new RGBDSFixture(49);
		// RGB Eurolite Big Party
		fixture[6]=new EuroliteBigParty(56);
		fixture[7]=new EuroliteBigParty(61);
		fixture[8]=new EuroliteBigParty(66);
		// 6in1 LEFT 71 LEAD 81 DRUM 91 RIGHT 101
		fixture[9]=new RGBWAUV(71);
		fixture[10]=new RGBWAUV(81);
		fixture[11]=new RGBWAUV(91);
		fixture[12]=new RGBWAUV(101);

		thread=new DmxThread(this);
	}

	@Autowired
	public OlaWeb io;
	
	@Value("${offline:false}")
	private boolean offline;
	
	private boolean junit=false;
	
	public void offline() {
		junit=true; // explicitly invoked only through JUNIT
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
			fixture[o.reset[i]].reset(o.layer,o.fade);
		thread.apply();
	}

	DmxChannelStatus apply(boolean strob){
		long timestamp = System.currentTimeMillis();
		DmxChannelStatus status = DmxChannelStatus.DONE;
		for(int i=0;i<fixture.length;i++){
			DmxChannelStatus cs = fixture[i].apply(data, strob, timestamp);
			status = status.merge(cs);
		}
		if(!offline) io.send(data);
		if(offline&&!junit) monitor();
		return status; 
	}
	public void blackout(long fade){
		log.info("BLACKOUT");
		chase.speed(-1);
		for(int i=0;i<fixture.length;i++){
			fixture[i].reset(-1,fade);
			fixture[i].set(new RGBFixtureState(i), fade);
		}
		for(int i=0;i<512;i++) data[i]=0;
		thread.apply();
	}
	/**
	 * @return dmx values for JUnit purpose
	 */
	public int[] read(){
		return data;
	}
	/**
	 * Shows output state through JAVA SWING 
	 */
	private void monitor(){
		if(frame==null){
			frame = new DmxMonitor(data);
		}
		frame.refresh(data);
	}
}
