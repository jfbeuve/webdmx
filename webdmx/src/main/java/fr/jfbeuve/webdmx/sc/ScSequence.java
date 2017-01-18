package fr.jfbeuve.webdmx.sc;

public class ScSequence {
	public Scene[] scenes;
	long speed;
	public ScSequence(Scene[] s){
		this(s,-1);
	}
	public ScSequence(Scene[] s, long t){
		scenes = s;
		speed = t;
	}
}
