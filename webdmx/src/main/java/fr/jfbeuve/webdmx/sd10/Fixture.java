package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public abstract class Fixture {
	protected int ch;
	protected int[] val= {};

	public Fixture(int _ch){
		ch=_ch;
	}
	public void set(int[] data){
		for(int i=0;i<val.length;i++)
			data[ch+i]=val[i];
		System.out.println(toString());
	}
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append(ch+" [");
		
		for(int i=0;i<val.length;i++){
			s.append(val[i]);
			if(i+1<val.length)s.append(",");
		}
		s.append("]");
		return s.toString();
	}
}
