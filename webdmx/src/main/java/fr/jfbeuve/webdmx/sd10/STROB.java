package fr.jfbeuve.webdmx.sd10;

public class STROB {
	int dimChannel, speedChannel;
	int dimValue, speedValue;
	public STROB(int ch){
		dimChannel=ch+1;
		speedChannel=ch;
	}
	public STROB set(){
		dimValue = 255;
		speedValue = 225; 
		return this;
	}
	public void set(int[] data){
		data[dimChannel]=dimValue;
		data[speedChannel]=speedValue;
		System.out.println(toString());
	}
	public String toString(){
		return "STROB DIM "+dimValue+" SPEED "+speedValue;
	}
}
