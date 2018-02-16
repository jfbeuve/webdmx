package fr.jfbeuve.webdmx.gpio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class MyLineListener implements LineListener{
	private Clip clip;
	public MyLineListener(Clip _clip){
		clip = _clip;
	}
	public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP)
            clip.close();
	}

}
