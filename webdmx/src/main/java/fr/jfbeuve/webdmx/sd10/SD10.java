package fr.jfbeuve.webdmx.sd10;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import fr.jfbeuve.webdmx.io.OlaWeb;
import fr.jfbeuve.webdmx.preset.PresetColor;

public class SD10 {
	private static final RGB8W RGB1=new RGB8W(1),RGB2=new RGB8W(6),RGB3=new RGB8W(11),RGB4=new RGB8W(16);
    private static final DICRO DICRO=new DICRO(27);
    private static final STROB STROB = new STROB(25);
    private static final SWITCH SWITCH = new SWITCH(21);
    private static final RGB10MM LED = new RGB10MM(31);
    
	private static final Map<String, String> NAME = new HashMap<String, String>(){
        {
            put("P1", "disco");
            put("S1", "strobo");
            put("P2", "bistro");
            put("S2", "slow");
        }
    };
    private int[] data = new int[512];
    public SD10(String host){
    	for(int i=0;i<data.length;i++) data[i]=0;
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
		System.out.println(args[0]+" - "+NAME.get(args[0]));
		Method m = SD10.class.getMethod(args[0]);
		m.invoke(new SD10(host));
	}

	public void P1() throws IOException{
		p1step(PresetColor.RED);
		p1step(PresetColor.GREEN);
		p1step(PresetColor.BLUE);
		p1step(PresetColor.YELLOW);
	}
	public void S1() throws IOException{
		SWITCH.set(true,true,true,true).set(data);
		STROB.set().set(data);
		dmxapply();
	}
	public void S2() throws IOException{
		SWITCH.set(false,true,false,false).set(data);
		color(PresetColor.BLUE,10);
		bgcolor(PresetColor.BLUE,10);
		dmxapply();
	}
	public void P2() throws IOException{
		p2step(PresetColor.RED,PresetColor.YELLOW);
		p2step(PresetColor.GREEN,PresetColor.ORANGE);
		p2step(PresetColor.BLUE,PresetColor.VIOLET);
		p2step(PresetColor.YELLOW,PresetColor.RED);
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
	private int step=0;
	private void p2step(PresetColor bg, PresetColor c) throws IOException{
		System.out.println("** STEP "+step++);
		bgcolor(bg,100);
		color(c,100);
		dmxapply();
		System.in.read();
	}
	private void p1step(PresetColor c) throws IOException{
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
}
