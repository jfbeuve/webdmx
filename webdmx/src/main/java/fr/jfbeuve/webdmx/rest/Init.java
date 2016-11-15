package fr.jfbeuve.webdmx.rest;

public class Init {
	long speed = 600; // 300 ms snap, 1500ms fade
	int speedrange = 50;
	long fade = 2000; // 0s snap, 1s fade, 5s long
	int faderange = 50;
	long strobospeed = 80;
	int dimmer= 100;
	long autocolor = -1; // -1, 0s, 4s
	boolean bgblack = false; 
	class Solo{
		String fixture=null;
		int dimmer = 100;
		boolean strob = false;
	}
	Solo solo=new Solo();
	class Macro{
		String show;
		long speed;
		int speedrange;
		Macro(String _show, long _speed, int _speedrange){
			show = _show;
			speed = _speed;
			speedrange = _speedrange;
		}
		
	}
	Macro snapshow = new Macro("CHASEMIX",600,50);
	Macro fadeshow = new Macro("CHASEMIX",3000,50);
}
