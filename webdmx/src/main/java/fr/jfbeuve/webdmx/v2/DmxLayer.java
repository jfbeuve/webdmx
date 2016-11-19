package fr.jfbeuve.webdmx.v2;

public class DmxLayer {
	//private final static int[] scale = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,30,40,50,60,70,80,90,100,150,200,255};
	
	private int startVal, endVal, dmxVal;
	private long startTime, fadeTime;
	//private int startStep;
	//private long steps;
	
	private long stroboSpeed=0;
	private long stroboTime=0;
	
	DmxLayer(int val){
		dmxVal = val;
		endVal = val;
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
	int get(){
		if(stroboSpeed>0){
			long time = System.currentTimeMillis()-stroboTime;
			if(time>stroboSpeed)
				return 0;
			if(time>stroboSpeed*2)
				stroboTime = time;
			return val();
		} else
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
		if(stroboSpeed>0) return false;
		return dmxVal == endVal;
	}
	
	void strob(long s){
		stroboSpeed = s;
	}
}
