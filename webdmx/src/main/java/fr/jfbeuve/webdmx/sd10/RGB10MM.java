package fr.jfbeuve.webdmx.sd10;

import fr.jfbeuve.webdmx.preset.PresetColor;

public class RGB10MM {
	private int redChannel,greenChannel,blueChannel,macroChannel,strobChannel,dimmerChannel, modeChannel;
	private int redValue,greenValue,blueValue,dimmer;
	public RGB10MM(int ch){
		
		redChannel=ch;
		greenChannel=ch+1;
		blueChannel=ch+2;
		macroChannel = ch+3;
		strobChannel = ch+4;
		modeChannel = ch+5;
		dimmerChannel= ch+6;
		
		redValue=0;
		greenValue=0;
		blueValue=0;
		dimmer=0;
	}
	public RGB10MM color(PresetColor c,int dim){
		dimmer = dim;
		redValue=c.r;
		greenValue=c.g;
		blueValue=c.b;
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
		data[dimmerChannel]=dimmer;
		data[redChannel]=redValue;
		data[greenChannel]=greenValue;
		data[blueChannel]=blueValue;
		System.out.println(toString());
	}
	public String toString(){
		return "RGB10MM R "+redValue*dimmer/100+" G "+greenValue*dimmer/100+" B "+blueValue*dimmer/100;
	}
}
