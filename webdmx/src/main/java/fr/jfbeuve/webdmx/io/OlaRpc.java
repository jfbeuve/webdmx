package fr.jfbeuve.webdmx.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OlaRpc implements IOWrapper {
	//https://developers.google.com/protocol-buffers/docs/javatutorial
	//https://docs.openlighting.org/ola/doc/latest/rpc_system.html#rpc_RPCLayer
	//https://github.com/google/protobuf
	//https://docs.openlighting.org/ola/doc/latest/rpc_system.html
	private static final Log log = LogFactory.getLog(OlaRpc.class);
	
	//private OlaClient ola;
	
	@Override
	public void disconnect() {
	}

	@Override
	synchronized public void send(int[] data) {
		try {
			//TODO if(ola==null) ola = new OlaClient();
		} catch (Exception e) {
			log.error(e,e);
		}
		//TODO ola.sendDmx(0, data);
	}

}
