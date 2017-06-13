package fr.jfbeuve.webdmx.fixture;

import fr.jfbeuve.webdmx.dmx.DmxChannel;
import fr.jfbeuve.webdmx.dmx.DmxChannelStatus;
import fr.jfbeuve.webdmx.sc.RGBFixtureState;

public class RGBDSFixture implements Fixture {

	private DmxChannel red, green, blue, dim, strb;
	
	public RGBDSFixture(int channel){
		red = new DmxChannel(channel);
		green = new DmxChannel(channel+1);
		blue = new DmxChannel(channel+2);
		strb = new DmxChannel(channel+4);
		dim = new DmxChannel(channel+6);
	}
	/* (non-Javadoc)
	 * @see fr.jfbeuve.webdmx.fixture.Fixture#set(fr.jfbeuve.webdmx.sc.RGBFixtureState, long)
	 */
	@Override
	public void set(RGBFixtureState f, long fade){
		red.set(f.r,100, false, fade);
		green.set(f.g,100, false, fade);
		blue.set(f.b,100, false, fade);
		//dim.set(f.dim*255/100,100, false, fade);
		dim.set(255,100, false, fade);
		strb.set(f.strob?255:0,100, false, 0);
	}
	
	/* (non-Javadoc)
	 * @see fr.jfbeuve.webdmx.fixture.Fixture#override(fr.jfbeuve.webdmx.sc.RGBFixtureState, long, int)
	 */
	@Override
	public void override(RGBFixtureState f, long fade, int layer){
		red.override(f.r, 100, false, fade, layer);
		green.override(f.g, 100, false, fade, layer);
		blue.override(f.b, 100, false, fade, layer);
		dim.override(f.dim, 100, false, fade, layer);
		strb.override(f.strob?255:0, 100, false, 0, layer);
	}
	
	/* (non-Javadoc)
	 * @see fr.jfbeuve.webdmx.fixture.Fixture#reset(int, long)
	 */
	@Override
	public void reset(int layer, long time){
		red.reset(layer,time);
		green.reset(layer,time);
		blue.reset(layer,time);
		dim.reset(layer,time);
		//TODO time = 0 for strob?
		strb.reset(layer,time);
	}
		
	/* (non-Javadoc)
	 * @see fr.jfbeuve.webdmx.fixture.Fixture#apply(int[], boolean, long)
	 */
	@Override
	public DmxChannelStatus apply(int[] output, boolean strob, long timestamp){
		DmxChannelStatus status = DmxChannelStatus.DONE;
		
		DmxChannelStatus rs = red.apply(output, false, timestamp);
		status = status.merge(rs);
		
		DmxChannelStatus gs = green.apply(output, false, timestamp);
		status = status.merge(gs);
		
		DmxChannelStatus bs = blue.apply(output, false, timestamp);
		status = status.merge(bs);
		
		DmxChannelStatus ds = dim.apply(output, false, timestamp);
		status = status.merge(ds);
		
		DmxChannelStatus ss = strb.apply(output, false, timestamp);
		status = status.merge(ss);
		
		return status;
	}
}
