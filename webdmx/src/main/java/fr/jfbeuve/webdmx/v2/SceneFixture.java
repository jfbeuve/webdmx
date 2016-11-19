package fr.jfbeuve.webdmx.v2;

public class SceneFixture {
	
	public int id, dim; 
	public int r,g,b;
	public long strob;
	/**
	 * JUnit constructor
	 */
	public SceneFixture(int id, int dim, int r, int g, int b, long strob) {
		super();
		this.id = id;
		this.dim = dim;
		this.r = r;
		this.g = g;
		this.b = b;
		this.strob = strob;
	}
	/**
	 * blackout constructor
	 */
	public SceneFixture(int _id){
		id=_id;dim=0;r=0;g=0;b=0;strob=0;
	}
	/**
	 * default JSON constructor
	 */
	public SceneFixture(){
		r=-1;g=-1;b=-1;strob=0;
	}
	public String toString(){
		return "["+(r*dim/100)+" "+(g*dim/100)+" "+(g*dim/100)+(strob>0?" S":"")+"]";
	}
}
