package fr.jfbeuve.webdmx.gpio;

public class FogStatus {
	
	public FogStatus(boolean fog, boolean auto, long fogtime, long sleeptime, boolean ready) {
		super();
		this.fog = fog;
		this.auto = auto;
		this.fogtime = fogtime;
		this.sleeptime = sleeptime;
		this.ready = ready;
	}
	public boolean fog, auto, ready;
	public long fogtime, sleeptime;
}
