package fr.jfbeuve.webdmx.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OlaWeb {
	private static final Log log = LogFactory.getLog(OlaWeb.class);
	private HttpURLConnection con;
	
	@Value("${remote:localhost}")
	private String host;
	
	public OlaWeb(){}
	
	public OlaWeb(String _host){
		host = _host;
	}

	public void disconnect() {
		con.disconnect();	
	}

	synchronized public void send(int[] data) {
		StringBuffer param = new StringBuffer("u=0&d=");
		for (int i = 0; i < data.length; i++) {
			if(i!=0) param.append(",");
			param.append(data[i]);
		}
		try {
			URL url = new URL("http://"+host+":9090/set_dmx");
			con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(param.toString());
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			if(responseCode!=200) log.error("HTTP ERROR "+responseCode);
		} catch (IOException e) {
			log.error("http://"+host+":9090/set_dmx",e);
		}
	}

}
