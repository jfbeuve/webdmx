package fr.jfbeuve.webdmx.show;

import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.fixture.PARFixture;

@Component
public class TheatreChaseShow extends TheatreAbstractShow{

	public TheatreChaseShow(){
		super();
		fixtures = new PARFixture[] {PARFixture.PAR1,PARFixture.PAR2,PARFixture.PAR3,PARFixture.PAR4};
		cues = new boolean[][]{
				{true,false, false, false},
				{false,true, false, false},
				{false,false, true, false},
				{false,false, false, true}
		};
	}
	
}
