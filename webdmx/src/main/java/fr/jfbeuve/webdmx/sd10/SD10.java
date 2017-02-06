package fr.jfbeuve.webdmx.sd10;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import fr.jfbeuve.webdmx.io.OlaWeb;
import fr.jfbeuve.webdmx.preset.PresetColor;

public class SD10 {
	private static final RGB8W RGB1=new RGB8W(0),RGB2=new RGB8W(5),RGB3=new RGB8W(10),RGB4=new RGB8W(15);
    private static final DICRO DICRO=new DICRO(26);
    private static final STROB STROB = new STROB(24);
    private static final SWITCH SWITCH = new SWITCH(20);
    private static final RGB10MM LED = new RGB10MM(30);
    
	private static final Map<String, String> NAME = new HashMap<String, String>(){
        {
            put("P1", "disco");
            put("S1", "strobo");
            put("P2", "slow");
            put("S2", "music");
            put("P3", "bistro");
            put("S3", "sync");
            put("blackout", "blackout");
        }
    };
    private int[] data = new int[512];

    public SD10(String host){
    	if(host!=null) io = new OlaWeb(host);
    }
	public static void main(String[] args) throws Exception {
		if(args.length==0){
			for(Map.Entry<String,String> e:NAME.entrySet())
				System.out.println(e.getKey()+" - "+e.getValue());
			return;
		}
		String host = null;
		if(args.length>1) host = args[1];
		Method m = SD10.class.getMethod(args[0]);
		m.invoke(new SD10(host));
	}
	/**
	 * DICRO/LED RGBA + ALL SWITCHS ON
	 */
	public void disco() throws IOException{
		discostep(PresetColor.R);
		discostep(PresetColor.G);
		discostep(PresetColor.B);
		discostep(PresetColor.YELLOW);
	}
	/**
	 * ALL CHANNELS OFF
	 */
	public void blackout() throws IOException{
		dmxapply();		
	}
	/**
	 * ALL SWITCHS ON, STROBO, NO DICRO/LED 
	 */
	public void strob() throws IOException{
		SWITCH.set(true,true,true,true).set(data);
		STROB.set().set(data);
		dmxapply();
		
	}
	/**
	 * LIGHT RGB LED/DICRO, ONLY SWITCH #2 ON 
	 */
	public void slow() throws IOException{
		SWITCH.set(false,true,false,false).set(data);
		colorall(PresetColor.B,3);
		dmxapply();
		System.in.read();
		colorall(PresetColor.G,3);
		dmxapply();
		System.in.read();
		colorall(PresetColor.R,3);
		dmxapply();
		System.in.read();
	}
	/**
	 * DICRO/LED1/LED3 RGBA + LED10MM/LED2/LED3 REVERSE COLOR 
	 */
	public void sync() throws IOException{
		syncstep(PresetColor.RED,PresetColor.YELLOW);
		syncstep(PresetColor.GREEN,PresetColor.ORANGE);
		syncstep(PresetColor.BLUE,PresetColor.VIOLET);
		syncstep(PresetColor.YELLOW,PresetColor.RED);
	}
	private void bgcolor(PresetColor c,int dim){
		RGB1.color(c,dim).set(data);
		RGB4.color(c,dim).set(data);
		DICRO.color(c,dim).set(data);
	}
	private void color(PresetColor c,int dim){
		RGB2.color(c,dim).set(data);
		RGB3.color(c,dim).set(data);
		LED.color(c,dim).set(data);
	}
	private void colorall(PresetColor c,int dim){
		RGB2.color(c,dim).set(data);
		RGB3.color(c,dim).set(data);
		LED.color(c,dim*10).set(data);
		RGB1.color(c,dim).set(data);
		RGB4.color(c,dim).set(data);
		DICRO.color(c,dim*7).set(data);
	}
	private int step=0;
	private void syncstep(PresetColor bg, PresetColor c) throws IOException{
		System.out.println("** STEP "+step++);
		bgcolor(bg,100);
		color(c,100);
		dmxapply();
		System.in.read();
	}
	private void discostep(PresetColor c) throws IOException{
		System.out.println("** STEP "+step++);
		SWITCH.set(true,true,true,true).set(data);
		bgcolor(c,100);
		color(c,100);
		dmxapply();
		System.in.read();
	}
	private OlaWeb io;
	private void dmxapply(){
		if(io!=null) io.send(data);
	}
	public void music() throws IOException{
		SWITCH.set(true,true,true,true).set(data);
		RGB1.music().set(data);
		RGB2.music().set(data);
		RGB3.music().set(data);
		RGB4.music().set(data);
		LED.music().set(data);
	}
	private void bistrostep(PresetColor c) throws IOException{
		syncstep(PresetColor.RED,c);
		syncstep(PresetColor.GREEN,c);
		syncstep(PresetColor.BLUE,c);
		syncstep(PresetColor.YELLOW,c);
	}
	public void bistro() throws IOException{
		bistrostep(PresetColor.RED);
		bistrostep(PresetColor.GREEN);
		bistrostep(PresetColor.BLUE);
		bistrostep(PresetColor.YELLOW);
		bistrostep(PresetColor.ORANGE);
		bistrostep(PresetColor.VIOLET);
		bistrostep(PresetColor.CYAN);
		bistrostep(PresetColor.WHITE);
	}
	private void bistroledstep(PresetColor c) throws IOException{
		syncstep(PresetColor.RED,c);
		syncstep(PresetColor.GREEN,c);
		syncstep(PresetColor.BLUE,c);
		syncstep(PresetColor.YELLOW,c);
		syncstep(PresetColor.ORANGE,c);
		syncstep(PresetColor.VIOLET,c);
		syncstep(PresetColor.CYAN,c);
		syncstep(PresetColor.WHITE,c);
	}
	public void bistroled() throws IOException{
		bistroledstep(PresetColor.RED);
		bistroledstep(PresetColor.GREEN);
		bistroledstep(PresetColor.BLUE);
		bistroledstep(PresetColor.YELLOW);
		bistroledstep(PresetColor.ORANGE);
		bistroledstep(PresetColor.VIOLET);
		bistroledstep(PresetColor.CYAN);
		bistroledstep(PresetColor.WHITE);
	}
	public void syncled() throws IOException{
		syncstep(PresetColor.RED,PresetColor.YELLOW);
		syncstep(PresetColor.YELLOW,PresetColor.RED);
		syncstep(PresetColor.GREEN,PresetColor.BLUE);
		syncstep(PresetColor.BLUE,PresetColor.GREEN);
		syncstep(PresetColor.CYAN,PresetColor.VIOLET);
		syncstep(PresetColor.VIOLET,PresetColor.CYAN);	
	}
	public void trio() throws IOException{
		triosteps(PresetColor.CYAN,PresetColor.VIOLET);
		triosteps(PresetColor.VIOLET,PresetColor.CYAN);
		triosteps(PresetColor.RED,PresetColor.YELLOW);
		triosteps(PresetColor.YELLOW,PresetColor.RED);
		triosteps(PresetColor.BLUE,PresetColor.GREEN);
		triosteps(PresetColor.GREEN,PresetColor.BLUE);
	}
	public void triosteps(PresetColor bg, PresetColor c) throws IOException{
		triostep(c,bg,bg);
		triostep(bg,c,bg);
		triostep(bg,bg,c);
		triostep(bg,c,bg);
	}
	private void triostep(PresetColor c1, PresetColor c2, PresetColor c3) throws IOException{
		System.out.println("** STEP "+step++);
		RGB1.color(c1,100).set(data);
		RGB2.color(c2,100).set(data);
		RGB3.color(c3,100).set(data);
		dmxapply();
		System.in.read();
	}
}
