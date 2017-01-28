package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class DICRO {
	private int redChannel,greenChannel,blueChannel,amberChannel;
	private int redValue,greenValue,blueValue,amberValue, dimmer;
	public DICRO(int ch){
		redChannel=ch+1;
		greenChannel=ch+2;
		blueChannel=ch;
		amberChannel=ch+3;
		
		redValue=0;
		greenValue=0;
		blueValue=0;
		amberValue=0;
		dimmer=0;
	}
	public DICRO color(PresetColor c, int dim){
		dimmer=dim;
		if(c==PresetColor.YELLOW){
			redValue=0;
			greenValue=0;
			blueValue=0;
			amberValue=255;
		}else if(c==PresetColor.ORANGE){
			redValue=127;
			greenValue=0;
			blueValue=0;
			amberValue=255;
		}else{
			redValue=c.r;
			greenValue=c.g;
			blueValue=c.b;
			amberValue=0;
		}
		if(c==PresetColor.RED){
			greenValue=0;
		}
		if(c==PresetColor.GREEN){
			blueValue=0;
		}
		if(c==PresetColor.BLUE){
			greenValue=0;
		}
		return this;
	}
	public void set(int[] data){
		data[redChannel]=redValue*dimmer/100;
		data[greenChannel]=greenValue*dimmer/100;
		data[blueChannel]=blueValue*dimmer/100;
		data[amberChannel]=amberValue*dimmer/100;
		System.out.println(toString());
	}
	public String toString(){
		return "DICRO R "+redValue*dimmer/100+" G "+greenValue*dimmer/100+" B "+blueValue*dimmer/100+" A "+amberValue*dimmer/100;
	}
}
