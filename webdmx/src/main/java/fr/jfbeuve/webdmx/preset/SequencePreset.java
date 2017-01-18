package fr.jfbeuve.webdmx.preset;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.jfbeuve.webdmx.sc.RGBFixtureState;
import fr.jfbeuve.webdmx.sc.ScOverride;
import fr.jfbeuve.webdmx.sc.ScSequence;
import fr.jfbeuve.webdmx.sc.Scene;

/**
 * sequences presets definition
 */

public class SequencePreset {
	//TODO javascript change sequence parsing
	//TODO UI always override speed-1
	/**
	 * FAST CHASE (MAX DIMMER 1,3,2,4)
	 */
	public ScSequence fastchase = new ScSequence(new Scene[]{
		new Scene(new RGBFixtureState[]{new RGBFixtureState(0,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(2,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(1,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(3,100,-1,-1,-1,false)},0)});
	
	/**
	 * SLOW CHASE (MAX DIMMER 1,0,3,0,2,0,4,0)
	 */
	public ScSequence slowchase = new ScSequence(new Scene[]{
		new Scene(new RGBFixtureState[]{new RGBFixtureState(0,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(2,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(1,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(3,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0)});
	/**
	 * FAST STROB CHASE
	 */
	public ScSequence sfastchase = new ScSequence(new Scene[]{
		new Scene(new RGBFixtureState[]{new RGBFixtureState(0,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(2,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(1,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(3,100,-1,-1,-1,false)},0)});
	
	/**
	 * SLOW STROB CHASE 
	 */
	public ScSequence sslowchase = new ScSequence(new Scene[]{
		new Scene(new RGBFixtureState[]{new RGBFixtureState(0,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(2,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(1,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(3,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0)});
	/**
	 * FULL ON / FULL OFF 1s 
	 */
	public ScSequence flash = new ScSequence(new Scene[]{
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,-1,255,255,255,false),
					new RGBFixtureState(1,-1,255,255,255,false),
					new RGBFixtureState(2,-1,255,255,255,false),
					new RGBFixtureState(3,-1,255,255,255,false)},500),
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,0,0,0,0,false),
					new RGBFixtureState(1,0,0,0,0,false),
					new RGBFixtureState(2,0,0,0,0,false),
					new RGBFixtureState(3,0,0,0,0,false)},500)}
	,1000);
	/**
	 * FULL ON / FULL OFF 80ms 
	 */
	public ScSequence sflash = new ScSequence(new Scene[]{
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,-1,255,255,255,false),
					new RGBFixtureState(1,-1,255,255,255,false),
					new RGBFixtureState(2,-1,255,255,255,false),
					new RGBFixtureState(3,-1,255,255,255,false)},0),
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,0,0,0,0,false),
					new RGBFixtureState(1,0,0,0,0,false),
					new RGBFixtureState(2,0,0,0,0,false),
					new RGBFixtureState(3,0,0,0,0,false)},0)}
	,80);

	/**
	 * WHITE CHASE (ALL BLACK BUT 1,2,3,4)
	 */
	public ScSequence wchase = new ScSequence(new Scene[]{
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,-1,-1,-1,-1,false),
				new RGBFixtureState(1,0,-1,-1,-1,false),
				new RGBFixtureState(2,0,-1,-1,-1,false),
				new RGBFixtureState(3,0,-1,-1,-1,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,-1,-1,-1,false),
				new RGBFixtureState(1,-1,-1,-1,-1,false),
				new RGBFixtureState(2,0,-1,-1,-1,false),
				new RGBFixtureState(3,0,-1,-1,-1,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,-1,-1,-1,false),
				new RGBFixtureState(1,0,-1,-1,-1,false),
				new RGBFixtureState(2,-1,-1,-1,-1,false),
				new RGBFixtureState(3,0,-1,-1,-1,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,-1,-1,-1,false),
				new RGBFixtureState(1,0,-1,-1,-1,false),
				new RGBFixtureState(2,0,-1,-1,-1,false),
				new RGBFixtureState(3,-1,-1,-1,-1,false)
		},0)});
	
	/**
	 * BLACK CHASE (ALL WHITE BUT 1,2,3,4)
	 */
	public ScSequence bchase = new ScSequence(new Scene[]{
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,-1,-1,-1,false),
				new RGBFixtureState(1,-1,-1,-1,-1,false),
				new RGBFixtureState(2,-1,-1,-1,-1,false),
				new RGBFixtureState(3,-1,-1,-1,-1,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,-1,-1,-1,-1,false),
				new RGBFixtureState(1,0,-1,-1,-1,false),
				new RGBFixtureState(2,-1,-1,-1,-1,false),
				new RGBFixtureState(3,-1,-1,-1,-1,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,-1,-1,-1,-1,false),
				new RGBFixtureState(1,-1,-1,-1,-1,false),
				new RGBFixtureState(2,0,-1,-1,-1,false),
				new RGBFixtureState(3,-1,-1,-1,-1,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,-1,-1,-1,-1,false),
				new RGBFixtureState(1,-1,-1,-1,-1,false),
				new RGBFixtureState(2,-1,-1,-1,-1,false),
				new RGBFixtureState(3,0,-1,-1,-1,false)
		},0)});
	
	//TODO replace yellow/red by color/reverse
	public ScSequence fire = new ScSequence(new Scene[]{
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,-1,255,40,0,false),
					new RGBFixtureState(1,-1,255,255,0,false),
					new RGBFixtureState(2,-1,255,40,0,false),
					new RGBFixtureState(3,-1,255,255,0,false)
			},0),
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,-1,255,255,0,false),
					new RGBFixtureState(1,-1,255,40,0,false),
					new RGBFixtureState(2,-1,255,255,0,false),
					new RGBFixtureState(3,-1,255,40,0,false)
			},0)});
	
	public static void main(String[] args) throws Exception{
		Scene sc = new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,0,0,0,false),
				new RGBFixtureState(1,0,0,0,0,false),
				new RGBFixtureState(2,0,0,0,0,false),
				new RGBFixtureState(3,0,0,0,0,false)
		},0);
		ScOverride o = new ScOverride(new Scene(new RGBFixtureState[]{new RGBFixtureState(0,0,0,0,0,false)},0),new int[0],2);
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("SCENE "+mapper.writeValueAsString(sc));
		System.out.println();
		System.out.println("OVERRIDE "+mapper.writeValueAsString(o));
		
	}
}
