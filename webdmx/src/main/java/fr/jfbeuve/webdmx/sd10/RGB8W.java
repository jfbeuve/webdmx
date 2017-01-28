package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class RGB8W {
	private int redChannel,greenChannel,blueChannel,macroChannel, speedChannel;
	private int redValue,greenValue,blueValue,macroValue, speedValue, dimmer;
	public RGB8W(int ch){
		macroChannel = ch;
		redChannel=ch+1;
		greenChannel=ch+2;
		blueChannel=ch+3;
		speedChannel=ch+4;
		
		macroValue=0;
		redValue=0;
		greenValue=0;
		blueValue=0;
		speedValue=0;
		dimmer=0;
	}
	public RGB8W color(PresetColor c,int dim){
		dimmer=dim;
		redValue=c.r;
		greenValue=c.g;
		blueValue=c.b;
		return this;
	}
	public void set(int[] data){
		data[macroChannel]=macroValue;
		data[redChannel]=redValue*dimmer/100;
		data[greenChannel]=greenValue*dimmer/100;
		data[blueChannel]=blueValue*dimmer/100;
		data[speedChannel]=speedValue;
		System.out.println(toString());
	}
	public String toString(){
		return "RGB"+(speedChannel/5)+" R "+redValue*dimmer/100+" G "+greenValue*dimmer/100+" B "+blueValue*dimmer/100+" MACRO "+macroValue+" SPEED "+speedValue;
	}
}
