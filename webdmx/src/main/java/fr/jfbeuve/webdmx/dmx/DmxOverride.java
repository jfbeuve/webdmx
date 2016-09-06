package fr.jfbeuve.webdmx.dmx;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGBColor;
import fr.jfbeuve.webdmx.show.Solo;

public class DmxOverride {
	@JsonProperty
	private String[] fixtures;
	@JsonProperty
	private String color;
	@JsonProperty
	private Integer dimmer;
	@JsonProperty
	private Long fade;
	@JsonProperty
	private boolean strob;
	
	public DmxOverride(){}
	
	public DmxOverride(String[] f, String c, int d){
		fixtures = f;
		color = c;
		dimmer = d;
	}
	public DmxOverride(RGBFixture f, RGBColor c, int d, long _fade){
		this(f,c,d,_fade, false);
	}
	public DmxOverride(Solo solo, RGBColor c, long _fade){
		this(solo.f,c,solo.dim,_fade, solo.strob);
	}
	public DmxOverride(RGBFixture f, RGBColor c, int d, long _fade, boolean _strob){
		fixtures = new String[]{f.toString()};
		color = c.toString();
		dimmer = d;
		this.fade = _fade;
		this.strob = _strob;
	}
	public DmxOverride(RGBFixture f){
		fixtures = new String[]{f.toString()};
		color = null;
		dimmer = null;
	}
	
	public List<RGBFixture> fixtures(){
		List<RGBFixture> toReturn = new ArrayList<RGBFixture>();
		for (int i = 0; i < fixtures.length; i++) {
			RGBFixture f = RGBFixture.valueOf(fixtures[i]);
			toReturn.add(f);
		}
		return toReturn;
	}
	
	public RGBColor color(){
		if(color==null)return null;
		return RGBColor.valueOf(color);
	}
	
	public Integer dimmer(){
		return dimmer;
	}
	public long fade(){
		if(fade==null)return 0;
		return fade;
	}
	public boolean strob(){
		return strob;
	}
}
