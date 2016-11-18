package fr.jfbeuve.webdmx.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.jfbeuve.webdmx.io.OlaWeb;

@Component
public class DmxWrapper {
	//private static final Log log = LogFactory.getLog(DmxWrapper.class);
	
	private int[] data;
	private RGBFixture[] scene;
	
	private DmxFader fader;
	
	public DmxWrapper(){
		data = new int[512];
		for(int i=0;i<512;i++) data[i]=0;
		
		scene[0]=new RGBFixture(24);
		scene[1]=new RGBFixture(27);
		scene[2]=new RGBFixture(30);
		scene[3]=new RGBFixture(33);
		
		fader=new DmxFader(this);
	}

	@Autowired
	public OlaWeb io;
	
	@Value("${offline:false}")
	private boolean offline;
	
	public void offline() {
		offline = true;
	}
	
	public void set(Scene sc){
		for(SceneFixture f:sc.fixtures)
			scene[f.id].set(f,sc.fade);
		fader.start();
	}
	
	/**
	 * sets overrides
	 */
	public void set(SceneOverride o){
		for(SceneFixture f:o.override)
			scene[f.id].override(f,o.fade, o.layer);
		for(int i=0;i<o.reset.length;i++)
			scene[o.reset[i]].reset(o.layer);
		fader.start();
	}

	boolean fade(){
		boolean completed = true;
		for(int i=0;i<scene.length;i++)
			if(scene[i].apply(data)==false) completed=false;
		if(!offline) io.send(data);
		return completed; 
	}
	public void blackout(long fade){
		for(int i=0;i<scene.length;i++){
			scene[i].reset(-1);
			scene[i].set(new SceneFixture(i), fade);
		}
		fader.start();
	}
}
