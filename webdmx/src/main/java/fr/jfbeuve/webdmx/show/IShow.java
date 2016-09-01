package fr.jfbeuve.webdmx.show;

import fr.jfbeuve.webdmx.dmx.DmxCue;

public interface IShow {
	public void next(DmxCue dmx);
	void color(RGBColor bgColor);
}
