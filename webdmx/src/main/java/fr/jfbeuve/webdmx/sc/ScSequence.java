package fr.jfbeuve.webdmx.sc;

public class ScSequence {
	public Scene[] scenes;
	public long speed;
	public ScSequence(){
		this(new Scene[]{});
	}
	public ScSequence(Scene[] s){
		this(s,-1);
	}
	public ScSequence(Scene[] s, long t){
		scenes = s;
		speed = t;
	}
}
