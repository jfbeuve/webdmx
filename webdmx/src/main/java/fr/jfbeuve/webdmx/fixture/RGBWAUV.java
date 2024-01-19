package fr.jfbeuve.webdmx.fixture;

import fr.jfbeuve.webdmx.dmx.DmxChannel;
import fr.jfbeuve.webdmx.dmx.DmxChannelStatus;
import fr.jfbeuve.webdmx.sc.RGBFixtureState;

public class RGBWAUV implements Fixture {

	protected DmxChannel dim, red, green, blue, white, amber, uv, strb;

	protected RGBWAUV(){};

	public RGBWAUV(int channel){
		dim = new DmxChannel(channel);
		red = new DmxChannel(channel+1);
		green = new DmxChannel(channel+2);
		blue = new DmxChannel(channel+3);
		white = new DmxChannel(channel+4);
		amber = new DmxChannel(channel+5);
		uv = new DmxChannel(channel+6);
		strb = new DmxChannel(channel+7);

	}

	@Override
	public void set(RGBFixtureState f, long fade){
		
		red.set(f.dim==0?0:f.r,100, false, fade);
		green.set(f.dim==0?0:f.g,100, false, fade);
		blue.set(f.dim==0?0:f.b,100, false, fade);
		
		dim.set(f.dim*255/100,100, false, fade);
		
		strb.set(f.strob?220:0,100, false, 0);

		white.set(f.dim==0?0:min(f),100, false, fade);
		amber.set(f.dim==0?0:amber(f),100, false, fade);
		uv.set(f.dim==0?0:uv(f),100, false, fade);
	}

	public int amber(RGBFixtureState f){
		int max = max(f);
		int min = min(f);

		if(f.r<max) return 0;
		if(f.b>min) return 0;
		if(f.g<=min) return 0;

		if(f.g>128) {
			return (255-f.g)*255/127;
		}
		if(f.g<128) {
			return f.g*255/128;
		}
		return 255;
	}
	private int uv(RGBFixtureState f){
		if(f.r==0&&f.g==0&&f.b==0) return 255;
		return 0;
	}
	private int max(RGBFixtureState f){
		if(f.r>=f.g&&f.r>=f.b) return f.r;
		if(f.g>=f.r&&f.g>=f.b) return f.g;
		if(f.b>=f.g&&f.b>=f.r) return f.b;
		return 255;
	}
	private int min(RGBFixtureState f){
		if(f.r<=f.g&&f.r<=f.b) return f.r;
		if(f.g<=f.r&&f.g<=f.b) return f.g;
		if(f.b<=f.g&&f.b<=f.r) return f.b;
		return 0;
	}

	@Override
	public void override(RGBFixtureState f, long fade, int layer){
		red.override(f.dim==0?0:f.r, 100, false, fade, layer);
		green.override(f.dim==0?0:f.g, 100, false, fade, layer);
		blue.override(f.dim==0?0:f.b, 100, false, fade, layer);
		dim.override(f.dim*255/100, 100, false, fade, layer);
		strb.override(f.strob?220:0, 100, false, 0, layer);
		white.override(f.dim==0?0:min(f),100, false, fade, layer);
		amber.override(f.dim==0?0:amber(f),100, false, fade, layer);
		uv.override(f.dim==0?0:uv(f),100, false, fade, layer);
	}

	@Override
	public void reset(int layer, long time){
		red.reset(layer,time);
		green.reset(layer,time);
		blue.reset(layer,time);
		dim.reset(layer,time);
		strb.reset(layer,0);
		white.reset(layer,time);
		amber.reset(layer,time);
		uv.reset(layer,time);
	}

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

		DmxChannelStatus ws = white.apply(output, false, timestamp);
		status = status.merge(ws);

		DmxChannelStatus as = amber.apply(output, false, timestamp);
		status = status.merge(as);

		DmxChannelStatus us = uv.apply(output, false, timestamp);
		status = status.merge(us);
		
		return status;
	}
}
