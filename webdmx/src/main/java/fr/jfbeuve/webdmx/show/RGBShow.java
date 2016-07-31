package fr.jfbeuve.webdmx.show;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RGBShow implements IShow{

	RGB3Fixture[] fixtures = {RGB3Fixture.PAR1,RGB3Fixture.PAR2,RGB3Fixture.PAR3,RGB3Fixture.PAR4};
	
	boolean[][] cues = {
			{false,false, false, false},
			{true,false,false,true},
			{false,true,true,false},
			{true, false, true, false},
			{false,true, false, true},
			{true,false, false, false},
			{true,false, false, false},
			{false,true, false, false},
			{false,false, true, false},
			{false,false, false, true},
	};
	
	Map<RGBColor,RGBColor> color = new HashMap<RGBColor,RGBColor>();
	
	RGBColor bgColor;
	int step=0;
	
	public RGBShow(){
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
	public Map<Integer,Integer> next(){
		Map<Integer,Integer> values = new HashMap<Integer,Integer>();
		for (int i=0;i<fixtures.length;i++) {
			boolean[] cue = cues[step];
			RGBColor toColor = (cue[i]?color.get(bgColor):bgColor);
			values.putAll(fixtures[i].set(toColor));
		}
		step++;
		if(step==cues.length)step=0;
		return values;
	}
	/**
	 * set background color
	 **/
	public void setColor(RGBColor _color) {
		this.bgColor = _color;
	}
	
}
