package fr.jfbeuve.webdmx.dmx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DmxLayer {
	private static final Log log = LogFactory.getLog(DmxLayer.class);
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
	/**
	 * fade to the new target value 
	 * @param val : target dmx value
	 * @param time : fade time
	 */
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
	/**
	 * fade to the old target value
	 * @param val : current dmx value
	 * @param time : fade time
	 */
	void reset(int val, long time){
		startTime = System.currentTimeMillis();
		startVal = val;
		dmxVal = val;
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
	/**
	 * @return next dmx value for this channel
	 */
	int next(boolean flash, long timestamp){
		if(strob&&!flash)
			return 0;
		
		if (fadeTime == 0)
			dmxVal = endVal;
		
		if(dmxVal == endVal) 
			return endVal;
		
		return getByLin(timestamp);
	}
	/**
	 * @return current dmx value for this channel
	 */
	int val(){
		return dmxVal;
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
	private int getByLin(long time){
		if(time>=startTime+fadeTime){
			// fading end
			dmxVal = endVal;
			return endVal;
		}
		
		int ratio = (int) ((time-startTime) * 100 / fadeTime);
		log.debug(ratio+"%");
		dmxVal = startVal + (endVal-startVal) * ratio / 100;
		
		return dmxVal;
	}
	/**
	 * @return true if nothing to do
	 */
	boolean done(){
		if(strob) return false;
		return dmxVal == endVal;
	}
	
	void strob(boolean s){
		strob=s;
	}
}
