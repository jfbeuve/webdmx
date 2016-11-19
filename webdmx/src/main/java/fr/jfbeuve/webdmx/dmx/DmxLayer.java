package fr.jfbeuve.webdmx.dmx;

public class DmxLayer {
	//private final static int[] scale = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,30,40,50,60,70,80,90,100,150,200,255};
	
	private int startVal, endVal, dmxVal;
	private long startTime, fadeTime;
	//private int startStep;
	//private long steps;
	
	boolean strob;
	
	DmxLayer(int val, boolean s){
		dmxVal = val;
		endVal = val;
		strob=s;
	}
	
	void set(int val, long time){
		startTime = System.currentTimeMillis();
		startVal = dmxVal;
		endVal = val;
		fadeTime = time;
		
		/*
		int endStep = 0;
		for(int i=0;i<scale.length;i++){
			if(startVal<=scale[i]) startStep = i;
			if(endVal<=scale[i]) endStep = i;
		}
		steps = endStep - startStep;
		*/
	}
	int get(boolean flash){
		if(strob&&!flash)
			return 0;
		else
			return val();
	}
	private int val(){
		if(dmxVal == endVal) 
			return endVal;
		
		if (fadeTime == 0){
			dmxVal = endVal;
			return endVal;
		}
		
		return getByLin();
	}
	/*
	 * fade using a scale rather than %
	 * @deprecated prefer getByLin()
	 
	private int getByLog(){
		
		long time = System.currentTimeMillis();
		
		if(time>startTime+fadeTime){
			// fading end
			dmxVal = endVal;
			return dmxVal;
		}
		
		// ratio by scale
		int step = startStep + (int) ((time-startTime)*steps/fadeTime) ; 
		dmxVal = scale[step];
		
		return dmxVal;
	}*/
	
	/**
	 * fade using %
	 */
	private int getByLin(){
		long time = System.currentTimeMillis();
		if(time>startTime+fadeTime){
			// fading end
			dmxVal = endVal;
			return dmxVal;
		}
		
		// ratio linear 
		dmxVal = startVal + (endVal-startVal) * ((int)(time-startTime)) / (int)fadeTime;
		
		return dmxVal;
	}
	
	boolean isCompleted(){
		if(strob) return false;
		return dmxVal == endVal;
	}
	
	void strob(boolean s){
		strob=s;
	}
}
