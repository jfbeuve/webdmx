package fr.jfbeuve.webdmx.show;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.fixture.PARFixture;
import fr.jfbeuve.webdmx.fixture.RGBFixture;

public enum RGBShow implements IShow{
	ROCK(new boolean[][]{
		{true,false,false,true}, 
		{false,true,true,false},
		{true, false, true, false},
		{false,true, false, true},
		{true,false, false, false},
		{false,false, false, true},
		{false,true, false, false}, 
		{false,false, true, false} 
	},false),
	STROB(new boolean[][]{
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
	},false);
	
	private static final Log log = LogFactory.getLog(RGBShow.class);
	public RGBFixture[] rgb = {RGBFixture.PAR1,RGBFixture.PAR2,RGBFixture.PAR3,RGBFixture.PAR4};
	public PARFixture[] par = {PARFixture.PAR1,PARFixture.PAR2,PARFixture.PAR3,PARFixture.PAR4};
	
	private boolean[][] cues;
	
	private Map<RGBColor,RGBColor> color = new HashMap<RGBColor,RGBColor>();
	
	private int step=0;
	private RGBColor bgColor;
	private boolean strob = false;
	
	private RGBShow(boolean[][] _cues, boolean _strob){
		cues = _cues;
		strob = _strob;
		
		color.put(RGBColor.CYAN, RGBColor.MAUVE);
		color.put(RGBColor.MAUVE, RGBColor.CYAN);
		color.put(RGBColor.JAUNE, RGBColor.ROUGE);
		color.put(RGBColor.ROUGE, RGBColor.JAUNE);
		color.put(RGBColor.VERT, RGBColor.AMBRE);
		color.put(RGBColor.BLEU, RGBColor.AMBRE);
		color.put(RGBColor.AMBRE, RGBColor.JAUNE);
		color.put(RGBColor.BLACK, RGBColor.WHITE);
		color.put(RGBColor.WHITE, RGBColor.BLACK);
	}
	
	public void next(DmxCue dmx){
		log.info("STEP "+step);
		
		for (int i=0;i<rgb.length;i++) {
			boolean[] cue = cues[step];
			RGBColor toColor = (cue[i]?color.get(bgColor):strob?RGBColor.BLACK:bgColor);
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
	
}
