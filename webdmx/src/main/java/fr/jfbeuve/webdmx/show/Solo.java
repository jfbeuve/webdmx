package fr.jfbeuve.webdmx.show;

import fr.jfbeuve.webdmx.fixture.RGBFixture;

public class Solo {
	public Solo(String _f, String _dim){
		f=RGBFixture.valueOf(_f);
		dim = Integer.valueOf(_dim);
	}
	public RGBFixture f; 
	public int dim;
}
