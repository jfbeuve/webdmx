package fr.jfbeuve.webdmx.awt;

public class RGB {
	public RGB(int r, int g, int b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}

	int r,g,b;
	
	public RGBD eval(){
		RGBD rgbd = new RGBD();
		
		int max = r;
		if(max<g)max=g;
		if(max<b)max=b;
		
		if(max==0) return rgbd; 
		
		rgbd.r = r * 255 / max;
		rgbd.g = g * 255 / max;
		rgbd.b = b * 255 / max;
		rgbd.d = max * 100 / 255;
		
		return rgbd; 
	}
}
