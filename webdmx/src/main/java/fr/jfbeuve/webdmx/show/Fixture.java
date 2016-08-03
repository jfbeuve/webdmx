package fr.jfbeuve.webdmx.show;

import java.util.ArrayList;
import java.util.Collection;

public class Fixture{
	Collection<Integer> channels = new ArrayList<Integer>();
	public Collection<Integer> channels(){return channels;}

	protected void channels(int[] in){
		for (int i = 0; i < in.length; i++) {
			channels.add(in[i]);
		}
	}
}
