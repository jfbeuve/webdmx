package fr.jfbeuve.webdmx.show;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.fixture.RGBFixture;

@Component
public class RockShow implements IShow{
	private static final Log log = LogFactory.getLog(RockShow.class);
	
	RGBFixture[] fixtures = {RGBFixture.PAR1,RGBFixture.PAR2,RGBFixture.PAR3,RGBFixture.PAR4};
	
	@Autowired
	private DmxCue dmx;
	
	boolean[][] cues = {
			{false,false, false, false},
			{true,false,false,true},
			{false,true,true,false},
			{true, false, true, false},
			{false,true, false, true},
			{true,false, false, false},
			{false,true, false, false},
			{false,false, true, false},
			{false,false, false, true},
	};
	
	private Map<RGBColor,RGBColor> color = new HashMap<RGBColor,RGBColor>();
	
	private RGBColor bgColor;
	private int step=0;
	
	public RockShow(){
		//define default color
		bgColor = RGBColor.MAUVE;
		
		//define bgcolor matrix
		color.put(RGBColor.CYAN, RGBColor.MAUVE);
		color.put(RGBColor.MAUVE, RGBColor.CYAN);
		color.put(RGBColor.JAUNE, RGBColor.ROUGE);
		color.put(RGBColor.ROUGE, RGBColor.JAUNE);
		color.put(RGBColor.VERT, RGBColor.AMBRE);
		color.put(RGBColor.BLEU, RGBColor.AMBRE);
		color.put(RGBColor.BLACK, RGBColor.WHITE);
		color.put(RGBColor.WHITE, RGBColor.BLACK);
	}
	/**
	 * @return dmx values to apply for next step of the show
	 */
	public void next(){
		log.info("STEP "+step);
		for (int i=0;i<fixtures.length;i++) {
			boolean[] cue = cues[step];
			RGBColor toColor = (cue[i]?color.get(bgColor):bgColor);
			dmx.set(fixtures[i],toColor);
		}
		step++;
		if(step==cues.length)step=0;
	}
	/**
	 * set background color
	 **/
	public void setColor(RGBColor _color) {
		this.bgColor = _color;
		if(strob) strob(true);
	}
	
	
	private boolean strob = false; 
	public void strob(boolean fire) {
		strob = fire;
		if(fire){
			dmx.set(RGBFixture.PAR1,RGBColor.BLACK);
			dmx.set(RGBFixture.PAR2,RGBColor.BLACK);
			dmx.set(RGBFixture.PAR3,RGBColor.BLACK);
			dmx.set(RGBFixture.PAR4,RGBColor.BLACK);
		}
		//TODO? dmx.set(RGBFixture.LEFT,(fire?color.get(bgcolor):RGBColor.BLACK));
		dmx.set(RGBFixture.LEFT,(fire?RGBColor.WHITE:RGBColor.BLACK));
		dmx.set(RGBFixture.LEFT.strob(),(fire?255:0));
		dmx.set(RGBFixture.LEFT.dim(),(fire?255:0));
	}
	
}
