package fr.jfbeuve.webdmx.gpio;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SoundManager {
	private static final Log log = LogFactory.getLog(SoundManager.class);
	public static final String BUZZER = "746.wav";
    public synchronized void play(String sound){
    	log.info("PLAY "+sound);
    	try {
			InputStream is = SoundManager.class.getClassLoader().getResourceAsStream(sound);
		    InputStream buf = new BufferedInputStream(is);
		    AudioInputStream stream = AudioSystem.getAudioInputStream(buf);
		    AudioFormat format = stream.getFormat();
		    DataLine.Info info = new DataLine.Info(Clip.class, format);
		    Clip clip = (Clip) AudioSystem.getLine(info);
		    clip.addLineListener(new MyLineListener(clip));
    		clip.open(stream);
    		clip.start();
    	}
    	catch (Throwable e) {
    	    log.error(e,e);
    	}
    }

/*    public static void main(String args[]) throws InterruptedException{
    	SoundManager s = new SoundManager();
    	s.play(BUZZER);
    	Thread.sleep(100);
    	s.play(BUZZER);
    	Thread.sleep(500);
    	s.play(BUZZER);
    	Thread.sleep(1000);
    	s.play(BUZZER);
    	Thread.sleep(1000);
    }*/
}
