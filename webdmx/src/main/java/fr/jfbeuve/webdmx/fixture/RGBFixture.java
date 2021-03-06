package fr.jfbeuve.webdmx.fixture;

import fr.jfbeuve.webdmx.dmx.DmxChannel;
import fr.jfbeuve.webdmx.dmx.DmxChannelStatus;
import fr.jfbeuve.webdmx.sc.RGBFixtureState;

public class RGBFixture implements Fixture {
	private DmxChannel red, green, blue;
	
	public RGBFixture(int channel){
		red = new DmxChannel(channel);
		green = new DmxChannel(channel+1);
		blue = new DmxChannel(channel+2);
	}
	/* (non-Javadoc)
	 * @see fr.jfbeuve.webdmx.fixture.Fixture#set(fr.jfbeuve.webdmx.sc.RGBFixtureState, long)
	 */
	@Override
	public void set(RGBFixtureState f, long fade){
		red.set(f.r,f.dim, f.strob, fade);
		green.set(f.g,f.dim, f.strob, fade);
		blue.set(f.b,f.dim, f.strob, fade);
	}
	
	/* (non-Javadoc)
	 * @see fr.jfbeuve.webdmx.fixture.Fixture#override(fr.jfbeuve.webdmx.sc.RGBFixtureState, long, int)
	 */
	@Override
	public void override(RGBFixtureState f, long fade, int layer){
		red.override(f.r, f.dim, f.strob, fade, layer);
		green.override(f.g, f.dim, f.strob, fade, layer);
		blue.override(f.b, f.dim, f.strob, fade, layer);
	}
	
	/* (non-Javadoc)
	 * @see fr.jfbeuve.webdmx.fixture.Fixture#reset(int, long)
	 */
	@Override
	public void reset(int layer, long time){
		red.reset(layer,time);
		green.reset(layer,time);
		blue.reset(layer,time);
	}
		
	/* (non-Javadoc)
	 * @see fr.jfbeuve.webdmx.fixture.Fixture#apply(int[], boolean, long)
	 */
	@Override
	public DmxChannelStatus apply(int[] output, boolean strob, long timestamp){
		DmxChannelStatus status = DmxChannelStatus.DONE;
		
		DmxChannelStatus rs = red.apply(output, strob, timestamp);
		status = status.merge(rs);
		
		DmxChannelStatus gs = green.apply(output, strob, timestamp);
		status = status.merge(gs);
		
		DmxChannelStatus bs = blue.apply(output, strob, timestamp);
		status = status.merge(bs);
				
		return status;
	}
}
