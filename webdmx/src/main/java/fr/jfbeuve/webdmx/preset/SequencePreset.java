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
	//TODO add bistro, sync, slow, music presets + move presets to overlay
	/**
	 * dimmer CHASE without gaps (MAX DIMMER 1,3,2,4)
	 */
	public ScSequence chas = new ScSequence(new Scene[]{
		new Scene(new RGBFixtureState[]{new RGBFixtureState(0,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(2,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(1,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(3,100,-1,-1,-1,false)},0)});
	
	/**
	 * dimmer CHASE with gaps (MAX DIMMER 1,0,3,0,2,0,4,0)
	 */
	public ScSequence gap = new ScSequence(new Scene[]{
		new Scene(new RGBFixtureState[]{new RGBFixtureState(0,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(2,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(1,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(3,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0)});
	
	/**
	 * FULL ON / FULL OFF 1s = applause
	 */
	public ScSequence apl = new ScSequence(new Scene[]{
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
	,500);
	
	/**
	 * FULL ON / FULL OFF 80ms = strob
	 */
	public ScSequence strb = new ScSequence(new Scene[]{
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
	 * 100% dim CHASE at strob speed (ALL BLACK BUT 1,2,3,4)
	 */
	public ScSequence flsh = new ScSequence(new Scene[]{
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,100,-1,-1,-1,false),
				new RGBFixtureState(1,0,-1,-1,-1,false),
				new RGBFixtureState(2,0,-1,-1,-1,false),
				new RGBFixtureState(3,0,-1,-1,-1,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,-1,-1,-1,false),
				new RGBFixtureState(1,100,-1,-1,-1,false),
				new RGBFixtureState(2,0,-1,-1,-1,false),
				new RGBFixtureState(3,0,-1,-1,-1,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,-1,-1,-1,false),
				new RGBFixtureState(1,0,-1,-1,-1,false),
				new RGBFixtureState(2,100,-1,-1,-1,false),
				new RGBFixtureState(3,0,-1,-1,-1,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,-1,-1,-1,false),
				new RGBFixtureState(1,0,-1,-1,-1,false),
				new RGBFixtureState(2,0,-1,-1,-1,false),
				new RGBFixtureState(3,100,-1,-1,-1,false)
		},0)},80);
	/**
	 * light CHASE (ALL BLACK BUT 1,2,3,4)
	 */
	public ScSequence sol = new ScSequence(new Scene[]{
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
	public ScSequence hol = new ScSequence(new Scene[]{
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
	
	// yellow to be overridden by main color and red by reverse color on client side
	public ScSequence fir = new ScSequence(new Scene[]{
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
	
	// yellow to be overridden by main color and red by reverse color on client side	
	public ScSequence wav = new ScSequence(new Scene[]{
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,-1,255,40,0,false),
					new RGBFixtureState(1,-1,255,255,0,false),
					new RGBFixtureState(2,-1,255,40,0,false),
					new RGBFixtureState(3,-1,255,255,0,false)
			},4000),
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,-1,255,255,0,false),
					new RGBFixtureState(1,-1,255,40,0,false),
					new RGBFixtureState(2,-1,255,255,0,false),
					new RGBFixtureState(3,-1,255,40,0,false)
			},4000)},16000);
	
	//TODO BISTRO =  RGBA * 7
	public ScSequence bistro = new ScSequence(new Scene[]{
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,-1,255,0,0,false),
					new RGBFixtureState(1,-1,255,255,0,false),
					new RGBFixtureState(2,-1,255,255,0,false),
					new RGBFixtureState(3,-1,255,0,0,false)},4000),
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,-1,255,255,0,false),
					new RGBFixtureState(1,-1,255,0,0,false),
					new RGBFixtureState(2,-1,255,0,0,false),
					new RGBFixtureState(3,-1,255,255,0,false)},4000),
			new Scene(new RGBFixtureState[]{
					new RGBFixtureState(0,-1,255,255,0,false),
					new RGBFixtureState(1,-1,255,0,0,false),
					new RGBFixtureState(2,-1,255,0,0,false),
					new RGBFixtureState(3,-1,255,255,0,false)},4000),
			},16000);
	
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
