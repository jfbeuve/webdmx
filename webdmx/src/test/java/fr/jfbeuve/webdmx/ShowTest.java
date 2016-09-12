package fr.jfbeuve.webdmx;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.jfbeuve.webdmx.dmx.DmxCue;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.Show;
import fr.jfbeuve.webdmx.show.ShowRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Start.class)
@DirtiesContext
public class ShowTest {

	@Autowired
	private ShowRunner run;
	
	@Test
	public void testShow() throws Exception {
		System.out.println("###### testShow");
		run.speed(100);
		run.fade(2000);	
		
		run.bgblack(false);
		Show.CHASEMIX.reset();
		Show.CHASEMIX.color(RGBColor.ROUGE);
		DmxCue cue = Show.CHASEMIX.next(run);
		assertColors(RGBColor.ROUGE, RGBColor.ROUGE, RGBColor.ROUGE, RGBColor.ROUGE, cue);
		
		run.bgblack(true);
		Show.CHASEMIX.reset();
		Show.CHASEMIX.color(RGBColor.ROUGE);
		cue = Show.CHASEMIX.next(run);
		assertColors(RGBColor.ROUGE, RGBColor.BLACK, RGBColor.BLACK, RGBColor.ROUGE, cue);
		
		run.bgblack(false);
		Show.CHASEMIX.reset();
		Show.CHASEMIX.color(RGBColor.BLACK);
		cue = Show.CHASEMIX.next(run);
		assertColors(RGBColor.WHITE, RGBColor.BLACK, RGBColor.BLACK, RGBColor.WHITE, cue);
	}
	
	private void assertColors(RGBColor a, RGBColor b, RGBColor c, RGBColor d, DmxCue cue){
		for(Map.Entry<Integer, Integer> v: cue.get().entrySet())
			System.out.print(v.getKey()+"="+v.getValue()+" ");
		System.out.println();
		
		assertEquals(a.red(), cue.get().get(24).intValue());
		assertEquals(a.green(), cue.get().get(25).intValue());
		assertEquals(a.blue(), cue.get().get(26).intValue());
		
		assertEquals(b.red(), cue.get().get(27).intValue());
		assertEquals(b.green(), cue.get().get(28).intValue());
		assertEquals(b.blue(), cue.get().get(29).intValue());
		
		assertEquals(c.red(), cue.get().get(30).intValue());
		assertEquals(c.green(), cue.get().get(31).intValue());
		assertEquals(c.blue(), cue.get().get(32).intValue());
		
		assertEquals(d.red(), cue.get().get(33).intValue());
		assertEquals(d.green(), cue.get().get(34).intValue());
		assertEquals(d.blue(), cue.get().get(35).intValue());
	}
}
