package fr.jfbeuve.webdmx.sd10;

public class SWITCH extends Fixture{
	
	public SWITCH(int ch){
		super(ch);
		val = new int[]{0,0,0,0};
	}

	public SWITCH set(boolean a, boolean b, boolean c, boolean d){
		val[0]=a?255:0;
		val[1]=b?255:0;
		val[2]=c?255:0;
		val[3]=d?255:0;
		return this;
	}

	public String toString(){
		return "SWITCH "+super.toString();
	}
}
