package fr.jfbeuve.webdmx.preset;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.jfbeuve.webdmx.sc.RGBFixtureState;
import fr.jfbeuve.webdmx.sc.Scene;

/**
 * sequences presets definition
 */

public class SequencePreset {
	
	/**
	 * FAST CHASE (MAX DIMMER 1,3,2,4)
	 */
	public Scene[] fchase = {
		new Scene(new RGBFixtureState[]{new RGBFixtureState(0,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(2,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(1,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(3,100,-1,-1,-1,false)},0)};
	
	/**
	 * SLOW CHASE (MAX DIMMER 1,3,2,4)
	 */
	public Scene[] schase = {
		new Scene(new RGBFixtureState[]{new RGBFixtureState(0,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(2,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(1,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0),
		new Scene(new RGBFixtureState[]{new RGBFixtureState(3,100,-1,-1,-1,false)},0),
		new Scene(new RGBFixtureState[]{},0)};
	
	public Scene[] flash = {
			new Scene(new RGBFixtureState[]{new RGBFixtureState(0,100,255,255,255,false)},0),
			new Scene(new RGBFixtureState[]{},0)};
	
	/**
	 * WHITE CHASE (ALL BLACK BUT 1,2,3,4)
	 */
	public Scene[] wchase = {
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,100,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,100,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,100,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,100,255,255,255,false)
		},0)};
	
	/**
	 * BLACK CHASE (ALL WHITE BUT 1,2,3,4)
	 */
	public Scene[] bchase = {
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,100,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,100,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,100,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false)
		},0),
		new Scene(new RGBFixtureState[]{
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,0,255,255,255,false),
				new RGBFixtureState(0,100,255,255,255,false)
		},0)};
	
	public static void main(String[] args) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(new SequencePreset()));
	}
}
