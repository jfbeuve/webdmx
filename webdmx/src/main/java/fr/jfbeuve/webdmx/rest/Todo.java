package fr.jfbeuve.webdmx.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Todo{
	@JsonProperty("name")
	private String name;
	@JsonProperty("id")
	private Integer id;
		
	@Override
	public String toString() {
		return "{name:"+name+",id:"+id+"}";
	}
}
