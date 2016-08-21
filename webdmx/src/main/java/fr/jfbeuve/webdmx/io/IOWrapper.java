package fr.jfbeuve.webdmx.io;

public interface IOWrapper {
	public void disconnect();
	public void send(int[] data);
}