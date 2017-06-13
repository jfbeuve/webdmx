package fr.jfbeuve.webdmx.fixture;

import fr.jfbeuve.webdmx.dmx.DmxChannelStatus;
import fr.jfbeuve.webdmx.sc.RGBFixtureState;

public interface Fixture {

	/**
	 * set dmx values to this fixture
	 * @param DMX RED 0-255
	 * @param DMX GREEN 0-255
	 * @param DMX BLUE 0-255
	 * @param DIMMER 0-100
	 * @param FADE TIME IN MS
	 */
	void set(RGBFixtureState f, long fade);

	/**
	 * set override for this fixture
	 */
	void override(RGBFixtureState f, long fade, int layer);

	/**
	 * reset override for this fixture
	 */
	void reset(int layer, long time);

	DmxChannelStatus apply(int[] output, boolean strob, long timestamp);

}