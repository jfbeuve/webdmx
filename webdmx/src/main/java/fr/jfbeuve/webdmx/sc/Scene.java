package fr.jfbeuve.webdmx.sc;

public class Scene {
	
	public FixtureState[] fixtures;
	public long fade=0;
	/* {
	 *   fixtures: [
	 *   	{id:1, r:255, g:0, b:0, dim:20, strob:0},
	 *   	{id:2, r:255, g:0, b:0, dim:20, strob:0},
	 *      {id:3, r:255, g:255, b:255, dim:10, strob:0},
	 *      {id:4, r:255, g:0, b:0, dim:20, strob:0}
	 *   ],
	 *   fade: 200
	 * }
	 */
	public Scene(){
		fixtures = new FixtureState[0];
		fade = 0;
	}
		public Scene(FixtureState[] fxt, long fad){
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
