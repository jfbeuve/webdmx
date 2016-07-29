package fr.jfbeuve.webdmx.show;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.dmx.DmxWrapper;

@Component
public class RGBChase {

	@Autowired
	private DmxWrapper dmx;
	
	RGBFixture[] fixtures = {new RGBFixture(24, 25, 26),new RGBFixture(27, 28, 29),new RGBFixture(30, 31, 32),new RGBFixture(33, 34, 35)};
	
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
	
	Map<DmxColor,DmxColor> color = new HashMap<DmxColor,DmxColor>();
	
	DmxColor bgColor;
	int step=0;
	
	public RGBChase(){
		//define default color
		bgColor = DmxColor.MAUVE;
		
		//define bgcolor matrix
		color.put(DmxColor.CYAN, DmxColor.MAUVE);
		color.put(DmxColor.MAUVE, DmxColor.CYAN);
		color.put(DmxColor.JAUNE, DmxColor.ROUGE);
		color.put(DmxColor.ROUGE, DmxColor.JAUNE);
		color.put(DmxColor.VERT, DmxColor.AMBRE);
		color.put(DmxColor.BLEU, DmxColor.AMBRE);
	}
	public void next(){
		//TODO implement fade loop - in a thread?
		for (int i=0;i<fixtures.length;i++) {
			boolean[] cue = cues[step];
			if(cue[i]) dmx.set(fixtures[i],color.get(bgColor));
			else dmx.set(fixtures[i],bgColor);
		}
		step++;
		if(step==cues.length)step=0;
	}
	/**
	 * set background color
	 **/
	public void setColor(DmxColor _color) {
		this.bgColor = _color;
	}
	
}
