package fr.jfbeuve.webdmx.show;

import java.util.HashMap;
import java.util.Map;

/**
 * RGB 3 channels DMX fixture
 */
public class RGB3Fixture {
	public static final RGB3Fixture PAR1 = new RGB3Fixture(24);
	public static final RGB3Fixture PAR2 =new RGB3Fixture(27);
	public static final RGB3Fixture PAR3 = new RGB3Fixture(30);
	public static final RGB3Fixture PAR4 = new RGB3Fixture(33);
	
	int red, green, blue;
	
	public RGB3Fixture(int channel) {
		super();
		this.red = channel;
		this.green = channel+1;
		this.blue = channel+2;
	}

	public Map<Integer,Integer> set(RGBColor color){
		Map<Integer,Integer> values = new HashMap<Integer,Integer>();
		values.put(red, color.red());
		values.put(green, color.green());
		values.put(blue, color.blue());
		return values;
	}
}
