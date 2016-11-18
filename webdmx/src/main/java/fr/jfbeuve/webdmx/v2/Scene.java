package fr.jfbeuve.webdmx.v2;

public class Scene {
	public Scene(){}
	/**
	 * JUnit constructor
	 */
	public Scene(SceneFixture[] fxt, long fad){
		fixtures = fxt;
		fade=fad;
	}
	public SceneFixture[] fixtures;
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
}
