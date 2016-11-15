package fr.jfbeuve.webdmx.v2.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.io.OlaWeb;

@Component
public class DmxWrapper {
	private static final Log log = LogFactory.getLog(DmxWrapper.class);
	
	private int[] data;
	private RgbFixture[] led = {new RgbFixture(24),new RgbFixture(27),new RgbFixture(30),new RgbFixture(33)};
	
	private DmxFader fader;
	
	public DmxWrapper(){
		data = new int[512];
		for(int i=0;i<512;i++) data[i]=0;
		
		fader=new DmxFader(this);
	}
	
	@Autowired
	public OlaWeb io;
	
	@Value("${offline:false}")
	private boolean offline;
	
	public void offline() {
		offline = true;
	}
	
	public void set(int fixture, int r, int g, int b, int dim, long fade){
		led[fixture].set(r,g,b,dim,fade);
		fader.start();
	}
	
	public void color(int fixture, int r, int g, int b, long fade){
		led[fixture].color(r,g,b,fade);
		fader.start();
	}
	
	public void dim(int fixture, int dim, long fade){
		led[fixture].dim(dim,fade);
		fader.start();
	}
	
	boolean fade(){
		boolean completed = true;
		for(int i=0;i<led.length;i++)
			if(led[i].apply(data)==false) completed=false;
		if(!offline) io.send(data);
		return completed; 
	}
}
