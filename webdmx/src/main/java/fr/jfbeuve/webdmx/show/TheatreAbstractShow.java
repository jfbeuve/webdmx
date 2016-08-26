package fr.jfbeuve.webdmx.show;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.fixture.PARFixture;

public abstract class TheatreAbstractShow implements IShow {
	protected static final Log log = LogFactory.getLog(TheatreAbstractShow.class);
	protected PARFixture[] fixtures = {PARFixture.PAR1,PARFixture.PAR2,PARFixture.PAR3,PARFixture.PAR4};
	
	@Autowired
	protected DmxCue dmx;
	
	protected int step=0;
	protected boolean[][] cues = {};
	
	@Override
	public void next() {
		log.info("STEP "+step);
		for (int i=0;i<fixtures.length;i++) {
			boolean[] cue = cues[step];
			dmx.set(fixtures[i].channel(),(cue[i]?255:0));
		}
		step++;
		if(step==cues.length)step=0;
	}

	@Override
	public void strob(boolean fire) {

	}

	@Override
	public void color(RGBColor bgColor) {

	}

}
