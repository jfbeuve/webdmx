package fr.jfbeuve.webdmx.show;

import java.util.HashMap;
import java.util.Map;

/**
 * RGB 7 channels DMX fixture
 */
public class RGB7Fixture extends RGB3Fixture{
	public static final RGB7Fixture LEFT = new RGB7Fixture(11);
	
	int strob, dim;
	
	public RGB7Fixture(int channel) {
		super(channel);
		strob = channel+4;
		dim = channel+6;
		channels(new int[]{red,green,blue, strob, dim});
	}
	public Map<Integer,Integer> set(RGBColor color){
		Map<Integer,Integer> values = new HashMap<Integer,Integer>();
		values.putAll(super.set(color));
		if(color.equals(RGBColor.BLACK)){
			values.put(strob,0);
			values.put(dim,0);
		}
		else values.put(dim,255);
		return values;
	}
	
	public int strob(){return strob;}
	public int dim(){return dim;}
}

