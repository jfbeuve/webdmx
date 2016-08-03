package fr.jfbeuve.webdmx.show;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxCue;

@Component
public class RGB3Show implements IShow{
	private static final Log log = LogFactory.getLog(RGB3Show.class);
	
	RGB3Fixture[] fixtures = {RGB3Fixture.PAR1,RGB3Fixture.PAR2,RGB3Fixture.PAR3,RGB3Fixture.PAR4};
	
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
	
	Map<RGBColor,RGBColor> color = new HashMap<RGBColor,RGBColor>();
	
	RGBColor bgColor;
	int step=0;
	
	public RGB3Show(){
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
	}
	
}
