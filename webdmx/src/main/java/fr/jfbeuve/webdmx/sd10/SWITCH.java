package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class SWITCH {
	private int channel;
	private boolean[] values = {false,false,false,false};
	
	public SWITCH(int ch){
		channel = ch;
	}

	public SWITCH set(boolean a, boolean b, boolean c, boolean d){
		values[0]=a;
		values[1]=b;
		values[2]=c;
		values[3]=d;
		return this;
	}
	public void set(int[] data){
		data[channel]=values[0]?255:0;
		data[channel+1]=values[1]?255:0;
		data[channel+2]=values[2]?255:0;
		data[channel+3]=values[3]?255:0;
		
		System.out.println(toString());
	}
	public String toString(){
		return "SWITCH "+display(values);
	}
	private static String display(boolean b){
		return b?"1":"0";
	}
	private static String display(boolean[] t){
		StringBuilder s = new StringBuilder();
		for(boolean b:t) s.append(display(b));
		return s.toString();
	}
}
