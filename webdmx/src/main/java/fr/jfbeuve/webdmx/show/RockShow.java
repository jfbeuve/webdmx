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
	
	@Autowired
	private ShowRunner show;
	
	RGBFixture[] fixtures = {RGBFixture.PAR1,RGBFixture.PAR2,RGBFixture.PAR3,RGBFixture.PAR4};
	
	@Autowired
	private DmxCue dmx;
	
	boolean[][] cues = {
			{false,false, false, false}, // skip if slow speed
			{true,false,false,true}, 
			{false,true,true,false},
			{true, false, true, false},
			{false,true, false, true},
			{true,false, false, false}, // skip if slow speed
			{false,true, false, false}, // skip if slow speed
			{false,false, true, false}, // skip if slow speed
			{false,false, false, true}, // skip if slow speed
	};
	
	private Map<RGBColor,RGBColor> color = new HashMap<RGBColor,RGBColor>();
	
	private RGBColor[] colorseq = {RGBColor.CYAN, RGBColor.MAUVE, RGBColor.JAUNE, RGBColor.ROUGE, RGBColor.AMBRE, RGBColor.VERT, RGBColor.BLEU};
	
	private int step=0;
	private RGBColor bgColor = RGBColor.MAUVE;
	
	public RockShow(){
		//define bgcolor matrix
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
	private long colortime=0;
	/**
	 * @return dmx values to apply for next step of the show
	 */
	public void next(){
		if(colortime==0) colortime = System.currentTimeMillis();
		
		if(autocolor){
			if(System.currentTimeMillis()-colortime>180000){
				colortime = System.currentTimeMillis();
				//next color
				for(int i=0;i<colorseq.length;i++){
					if(bgColor==colorseq[i]){
						int a = i+1;
						if(a==colorseq.length) a=0;
						bgColor = colorseq[i+1];
						break;
					}
				}
			}
		}
		
		if(show.speed()>2000){
			//smart show (skips solo steps in rock show if slow speed)
			if(step==0) step = 1;
			if(step>4) step = 1;
		}
		log.info("STEP "+step);
		for (int i=0;i<fixtures.length;i++) {
			boolean[] cue = cues[step];
			RGBColor toColor = (cue[i]?color.get(bgColor):bgColor);
			dmx.set(fixtures[i],toColor);
		}
		step++;
		if(step==cues.length)step=0;
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
		dmx.set(RGBFixture.LEFT,(fire?color.get(bgColor):RGBColor.BLACK));
		//dmx.set(RGBFixture.LEFT,(fire?RGBColor.WHITE:RGBColor.BLACK));
		dmx.set(RGBFixture.LEFT.strob(),(fire?255:0));
		dmx.set(RGBFixture.LEFT.dim(),(fire?255:0));
	}
	boolean autocolor = true;
	@Override
	public void color(RGBColor _color) {
		if(_color==RGBColor.AUTO){
			autocolor=true;
			return;
		}
		autocolor=false;
		colortime = System.currentTimeMillis();
		bgColor = _color;
		if(strob) strob(true);
	}
	
}
