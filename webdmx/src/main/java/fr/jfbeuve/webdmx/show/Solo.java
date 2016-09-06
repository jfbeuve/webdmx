package fr.jfbeuve.webdmx.show;

import fr.jfbeuve.webdmx.fixture.RGBFixture;

public class Solo {
	public Solo(RGBFixture _f, int _dim, boolean _strob){
		f=_f;
		dim = _dim;
		strob=_strob;
	}
	public RGBFixture f; 
	public int dim;
	public boolean strob;
}
