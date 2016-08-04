package fr.jfbeuve.webdmx.dmx;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.jfbeuve.webdmx.fixture.RGBFixture;
import fr.jfbeuve.webdmx.show.RGBColor;

public class DmxOverride {
	@JsonProperty
	private String[] fixtures;
	@JsonProperty
	private String color;
	@JsonProperty
	private Integer dimmer;
	@JsonProperty
	private Long fade;
	
	public DmxOverride(){}
	
	public DmxOverride(String[] f, String c, int d){
		fixtures = f;
		color = c;
		dimmer = d;
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
}
