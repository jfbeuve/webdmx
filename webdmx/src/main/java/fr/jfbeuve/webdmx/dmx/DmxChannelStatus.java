package fr.jfbeuve.webdmx.dmx;

public enum DmxChannelStatus {
	DONE, RUNNING, STROB;
	public DmxChannelStatus merge(DmxChannelStatus s){
		if(s==STROB) return STROB;
		if(s==RUNNING) return RUNNING;
		return this;
	}
}
