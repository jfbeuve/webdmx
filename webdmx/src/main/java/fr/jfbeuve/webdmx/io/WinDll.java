package fr.jfbeuve.webdmx.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.juanjo.openDmx.OpenDmx;

public class WinDll implements IOWrapper {
	private static final Log log = LogFactory.getLog(WinDll.class);
	private boolean open = false;
	
	public WinDll(){}

	@Override
	public void disconnect() {
		OpenDmx.disconnect();
	}

	@Override
	synchronized public void send(int[] data) {
		if(!open) open = OpenDmx.connect(OpenDmx.OPENDMX_TX);
		if(!open) log.warn("Open Dmx widget not detected!");
		
		for(int channel = 0;channel<data.length;channel++){
			OpenDmx.setValue(channel,data[channel]);
		}
	}

}
