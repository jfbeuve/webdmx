package fr.jfbeuve.webdmx.show;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.fixture.PARFixture;
import fr.jfbeuve.webdmx.fixture.RGBFixture;

public enum Show {
	CHASEMIX(new boolean[][]{
		{false,false,false,false}, // 1
		{true,false,false,true}, // 2
		{false,true,true,false}, // 3
		{true, false, true, false}, // 4
		{false,true, false, true}, // 5
		{true,false, false, false}, // 6
		{false,false, false, true}, // 7
		{false,true, false, false}, // 8
		{false,false, true, false} //9
	},false),
	STROBO(new boolean[][]{
		{false,false, false, false},
		{true,true,true,true} 
	},true),
	CHASE(new boolean[][]{
			{false,false,false,false},
			{false,true, false, false},
			{false,false, false, true},
			{true,false, false, false},
			{false,false, true, false}
	},false),
	STROBOCHASE(new boolean[][]{
			{false,false, false, false},
			{true,true,true,true} 
	},true),
	FLASH(new boolean[][]{
			{false,false, false, false},
			{false,true, false, false},
			{false,false, false, false},
			{false,false, false, true},
			{false,false, false, false},
			{true,false, false, false},
			{false,false, false, false},
			{false,false, true, false}
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
	private static final RGBFixture[] rgb = {RGBFixture.PAR1,RGBFixture.PAR2,RGBFixture.PAR3,RGBFixture.PAR4};
	private static final PARFixture[] par = {PARFixture.PAR1,PARFixture.PAR2,PARFixture.PAR3,PARFixture.PAR4};
	
	private boolean[][] cues;
	
	private int step=0;
	private RGBColor bgColor;
	private boolean strob = false;
	
	private Show(boolean[][] _cues, boolean _strob){
		cues = _cues;
		strob = _strob;
	}
	
	private long timestamp = 0;
	private int solo = 0;
	
	public DmxCue next(ShowRunner run){
		
		if(this==FADE||this==CHASE||this==CHASEMIX){
			if((run.bgblack()||bgColor==RGBColor.BLACK)&&step==0) step =1;
		}
		
		if(this==CHASEMIX&&run.fade()){
			// smart show (skip solo steps if fade mode)
			if(step>5) step=0;
		}
		
		if(this==STROBOCHASE){
			log.info("STEP "+step+ " SOLO "+solo);
			if(timestamp==0) System.currentTimeMillis();
			if(System.currentTimeMillis()-timestamp>4000){
				timestamp = System.currentTimeMillis();
				solo++;
				if(solo>3) solo=0;
			}

		}else {
			log.info("STEP "+step);
		}
		
		DmxCue dmx = new DmxCue();
		for (int i=0;i<rgb.length;i++) {
			boolean[] cue = cues[step];
			RGBColor toColor = cue[i]?bgColor.solo():bgColor;
			if(strob||run.bgblack()) toColor = cue[i]?bgColor:RGBColor.BLACK;
			if(this==STROBOCHASE&&i==solo) toColor = bgColor;
			dmx.set(rgb[i],toColor);
		}
		
		for (int i=0;i<par.length;i++) {
			boolean[] cue = cues[step];
			dmx.set(par[i].channel(),(cue[i]?255:0));
		}
		
		step++;
		if(step==cues.length)step=0;
		if(this==ON||this==OFF) run.stop();
		
		return dmx;
	}
	
	public void color(RGBColor _color) {
		bgColor = _color;
	}
	public boolean strob(){
		return strob;
	}
	public void reset(){
		step = 0;
	}
}
