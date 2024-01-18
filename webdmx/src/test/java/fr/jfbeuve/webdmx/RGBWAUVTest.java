package fr.jfbeuve.webdmx;
import static org.junit.Assert.assertEquals;

import fr.jfbeuve.webdmx.fixture.RGBWAUV;
import fr.jfbeuve.webdmx.sc.RGBFixtureState;
import org.junit.Test;

public class RGBWAUVTest {
    @Test
    public void eval() throws Exception {
        RGBWAUV f = new RGBWAUV(1);
        assertEquals(0, f.amber(new RGBFixtureState(0,0,0,0,0,false)));
        assertEquals(0, f.amber(new RGBFixtureState(0,0,255,0,0,false)));
        assertEquals(0, f.amber(new RGBFixtureState(0,0,0,255,0,false)));
        assertEquals(0, f.amber(new RGBFixtureState(0,0,0,0,255,false)));
        assertEquals(0, f.amber(new RGBFixtureState(0,0,0,255,255,false)));
        assertEquals(0, f.amber(new RGBFixtureState(0,0,255,255,0,false)));
        assertEquals(0, f.amber(new RGBFixtureState(0,0,255,255,255,false)));
        assertEquals(0, f.amber(new RGBFixtureState(0,0,255,128,0,false)));
        assertEquals(255, f.amber(new RGBFixtureState(0,0,255,64,0,false)));
        assertEquals(127, f.amber(new RGBFixtureState(0,0,255,96,0,false)));
        assertEquals(127, f.amber(new RGBFixtureState(0,0,255,32,0,false)));
    }
}
