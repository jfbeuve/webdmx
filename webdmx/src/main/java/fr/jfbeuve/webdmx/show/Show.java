package fr.jfbeuve.webdmx.show;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.fixture.PARFixture;
import fr.jfbeuve.webdmx.fixture.RGBFixture;

public enum Show {
	CHASEMIX(new boolean[][]{
		{false,false,false,false}, 
		{true,false,false,true}, 
		{false,true,true,false},
		{true, false, true, false},
		{false,true, false, true},
		{true,false, false, false},
		{false,false, false, true},
		{false,true, false, false}, 
		{false,false, true, false} 
	},false),
	STROBO(new boolean[][]{
		{false,false, false, false},
		{true,true,true,true} 
	},true),
	CHASE(new boolean[][]{
			{true,false, false, false},
			{false,true, false, false},
			{false,false, true, false},
			{false,false, false, true},
			{false,false, true, false},
			{false,true, false, false}
	},false),
	STROBOCHASE(new boolean[][]{
			{true,false, false, false}, // 1
			{true,true,true,true},
			{true,false, false, false},
			{true,true,true,true},
			{false,true, false, false}, // 2
			{true,true,true,true},
			{false,true, false, false},
			{true,true,true,true},
			{false,false, true, false}, // 3
			{true,true,true,true},
			{false,false, true, false},
			{true,true,true,true},
			{false,false, false, true}, // 4
			{true,true,true,true},
			{false,false, false, true}
	},true),
	FADE(new boolean[][]{
			{false,false, false, false},
			{true, false, true, false},
			{false,true, false, true},
			{true,false,false,true}, 
			{false,true,true,false}
	},false),
	ON(new boolean[][]{
			{true,true, true, true}
	},false),
	OFF(new boolean[][]{
			{false,false, false, false}
	},false);
	
	private static final Log log = LogFactory.getLog(Show.class);
	public RGBFixture[] rgb = {RGBFixture.PAR1,RGBFixture.PAR2,RGBFixture.PAR3,RGBFixture.PAR4};
	public PARFixture[] par = {PARFixture.PAR1,PARFixture.PAR2,PARFixture.PAR3,PARFixture.PAR4};
	
	private boolean[][] cues;
	
	private int step=0;
	private RGBColor bgColor;
	private boolean strob = false;
	
	private Show(boolean[][] _cues, boolean _strob){
		cues = _cues;
		strob = _strob;
	}
	
	public void next(DmxCue dmx){
		log.info("STEP "+step);
		
		for (int i=0;i<rgb.length;i++) {
			boolean[] cue = cues[step];
			RGBColor toColor = cue[i]?bgColor.solo():bgColor;
			if(strob) toColor = cue[i]?bgColor:RGBColor.BLACK;
			dmx.set(rgb[i],toColor);
		}
		
		for (int i=0;i<par.length;i++) {
			boolean[] cue = cues[step];
			dmx.set(par[i].channel(),(cue[i]?255:0));
		}
		
		step++;
		if(step==cues.length)step=0;
	}
	
	public void color(RGBColor _color) {
		bgColor = _color;
	}
	public boolean strob(){
		return strob;
	}
}
