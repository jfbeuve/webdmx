package fr.jfbeuve.webdmx.sc;

public class FixtureState {
	
	public int id, dim; 
	public int r,g,b;
	public boolean strob;
	/**
	 * JUnit constructor
	 */
	public FixtureState(int id, int dim, int r, int g, int b, boolean strob) {
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
	public FixtureState(int _id){
		id=_id;dim=0;r=0;g=0;b=0;strob=false;
	}
	/**
	 * default JSON constructor
	 */
	public FixtureState(){
		r=-1;g=-1;b=-1;strob=false;
	}
	public String toString(){
		return "["+(r*dim/100)+" "+(g*dim/100)+" "+(g*dim/100)+(strob?" S":"")+"]";
	}
}
