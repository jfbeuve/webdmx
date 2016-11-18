package fr.jfbeuve.webdmx.v2;

public class SceneFixture {
	
	public int id, dim; 
	public int r,g,b;
	public long strob;
	
	public SceneFixture(int _id){
		id=_id;dim=0;r=0;g=0;b=0;strob=0;
	}
	
	public SceneFixture(){
		r=-1;g=-1;b=-1;strob=0;
	}
}
