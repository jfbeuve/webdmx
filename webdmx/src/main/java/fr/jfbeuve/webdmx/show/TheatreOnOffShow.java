package fr.jfbeuve.webdmx.show;

import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.fixture.PARFixture;

@Component
public class TheatreOnOffShow extends TheatreAbstractShow{

	public TheatreOnOffShow(){
		super();
		fixtures = new PARFixture[] {PARFixture.PAR1,PARFixture.PAR2,PARFixture.PAR3,PARFixture.PAR4};
		cues = new boolean[][]{
				{true,true, true, true},
				{false,false, false, false}
		};
	}
	/**
	 * @param in fade in if true, fade out if false
	 */
	public void fade(FadeType type, long time){
		for (int i=0;i<fixtures.length;i++) {
			dmx.set(fixtures[i].channel(),(type==FadeType.IN?255:0));
		}
		dmx.apply(time);
	}
}
