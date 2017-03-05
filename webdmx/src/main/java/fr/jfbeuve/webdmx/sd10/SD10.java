package fr.jfbeuve.webdmx.sd10;

import java.io.IOException;
import java.lang.reflect.Method;

import fr.jfbeuve.webdmx.io.OlaWeb;
import fr.jfbeuve.webdmx.preset.PresetColor;

public class SD10 {
	private static final RGB8W RGB1=new RGB8W(0),RGB2=new RGB8W(5),RGB3=new RGB8W(10),RGB4=new RGB8W(15);
    private static final DICRO DICRO=new DICRO(26);
    private static final STROB STROB = new STROB(24);
    private static final SWITCH SWITCH = new SWITCH(20);
    private static final RGB10MM LED = new RGB10MM(30);
    private static final WIZARD WIZARD = new WIZARD(37);
    
    private int[] data = new int[512];
    private int step=0;
    private int dim=100;
    
    public SD10(String host){
    	if(host!=null) io = new OlaWeb(host);
    }
	public static void main(String[] args) throws Exception {
		if(args.length==0){
			Method[] mm = SD10.class.getMethods();
			for(Method m:mm){
				if(m.getParameterCount()==0&&m.getDeclaringClass().equals(SD10.class)){
					StringBuilder s = new StringBuilder();
					
					Address p = m.getAnnotation(Address.class);
					if(p!=null) s.append(p.value()+" - ");
					
					s.append(m.getName());
					
					/*
					switch(m.getName()){
						case "disco":s.append(" -> P1");break;
						case "strob":s.append(" -> S1");break;
						case "slow":s.append(" -> P2");break;
						case "music":s.append(" -> S2");break;
						case "bistro":s.append(" -> P3");break;
						case "sync":s.append(" -> S3");break;
						case "bistroled":s.append(" -> P4");break;
						case "syncled":s.append(" -> S4");break;
						case "triolite":s.append(" -> P5");break;
						case "trio":s.append(" -> S5");break;
						default:
					}*/
						
					System.out.println(s.toString());
				}
			}
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
	@Address("P1")
	public void disco() throws IOException{
		WIZARD.disco().set(data);
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
	@Address("S1")
	public void strob() throws IOException{
		WIZARD.disco().set(data);
		SWITCH.set(true,true,true,true).set(data);
		STROB.set().set(data);
		dmxapply();
		
	}
	/**
	 * LIGHT RGB LED/DICRO, ONLY SWITCH #2 ON 
	 */
	@Address("P2")
	public void slow() throws IOException{
		WIZARD.slow().set(data);
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
	@Address("S3")
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
	@Address("S2")
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
	@Address("P3")
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
	@Address("P4")
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
	@Address("S4")
	public void syncled() throws IOException{
		syncstep(PresetColor.RED,PresetColor.YELLOW);
		syncstep(PresetColor.YELLOW,PresetColor.RED);
		syncstep(PresetColor.GREEN,PresetColor.BLUE);
		syncstep(PresetColor.BLUE,PresetColor.GREEN);
		syncstep(PresetColor.CYAN,PresetColor.VIOLET);
		syncstep(PresetColor.VIOLET,PresetColor.CYAN);	
	}
	@Address("P5")
	public void triolite() throws IOException{
		dim=20;
		trio();
	}
	@Address("S5")
	public void trio() throws IOException{
		triosteps(PresetColor.CYAN,PresetColor.VIOLET);
		triosteps(PresetColor.VIOLET,PresetColor.CYAN);
		triosteps(PresetColor.RED,PresetColor.YELLOW);
		triosteps(PresetColor.YELLOW,PresetColor.RED);
		triosteps(PresetColor.BLUE,PresetColor.GREEN);
		triosteps(PresetColor.GREEN,PresetColor.BLUE);
	}
	private void triosteps(PresetColor bg, PresetColor c) throws IOException{
		triostep(c,bg,bg);
		triostep(bg,c,bg);
		triostep(bg,bg,c);
		triostep(bg,c,bg);
		triostep(c,bg,bg);
	}
	private void triostep(PresetColor c1, PresetColor c2, PresetColor c3) throws IOException{
		System.out.println("** STEP "+step++);
		RGB1.color(c1,dim).set(data);
		RGB2.color(c2,dim).set(data);
		RGB3.color(c3,dim).set(data);
		dmxapply();
		System.in.read();
	}
	//TODO insert P2+1 DISCOLED (+WIZARD MUSIC), insert S2+1 WIZARD (MUSIC), ADD WIZARD MUSIC to P1, S1, S2 ADD WIZARD SLOW to P2
}
