package fr.jfbeuve.webdmx.sc;

public class Scene {
	
	public RGBFixtureState[] fixtures; 
	public long fade=0;
	
	public Scene(){
		fixtures = new RGBFixtureState[0];
		fade = 0;
	}
		public Scene(RGBFixtureState[] fxt, long fad){
		fixtures = fxt;
		fade=fad;
	}
	public String toString(){
		StringBuffer s = new StringBuffer();
		for(int i=0;i<fixtures.length;i++)
			s.append(fixtures[i]);
		s.append(" fade="+fade);
		return s.toString();
	}
}
