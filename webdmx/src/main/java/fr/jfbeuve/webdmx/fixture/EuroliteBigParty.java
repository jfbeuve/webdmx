package fr.jfbeuve.webdmx.fixture;

import fr.jfbeuve.webdmx.dmx.DmxChannel;

public class EuroliteBigParty extends RGBDSFixture {

	public EuroliteBigParty(int channel){
		red = new DmxChannel(channel);
		green = new DmxChannel(channel+1);
		blue = new DmxChannel(channel+2);
		strb = new DmxChannel(channel+4);
		dim = new DmxChannel(channel+3);
	}
}
