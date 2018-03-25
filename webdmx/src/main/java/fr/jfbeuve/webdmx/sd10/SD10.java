package fr.jfbeuve.webdmx.sd10;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import fr.jfbeuve.webdmx.io.OlaWeb;
import fr.jfbeuve.webdmx.preset.PresetColor;

public class SD10 {
	private static final RGB8W RGB1=new RGB8W(0),RGB2=new RGB8W(5),RGB3=new RGB8W(10),RGB4=new RGB8W(15);
	private static final RGBEuroliteBP EUR1=new RGBEuroliteBP(55),EUR2=new RGBEuroliteBP(60),EUR3=new RGBEuroliteBP(65);
    private static final DICRO DICRO=new DICRO(26);
    private static final STROB STROB = new STROB(24);
    private static final SWITCH SWITCH = new SWITCH(20);
    private static final RGB10MM LED = new RGB10MM(30);
    private static final WIZARD WIZARD = new WIZARD(37);
    
    private int[] data = new int[512];
    
    private int step=0;
	private String cat = "P";
	private int rank = 1;
	
	private static final String[] P={"dicro", "led", "rgb", "rainb"};
	private static final String[] S={"strob","led","rgbled", "srainb"};
	
	private static void compile() throws SecurityException{
		int max = P.length;
		if(S.length>max)max=S.length;
		
		for(int i=0;i<max;i++){
			StringBuilder s = new StringBuilder();
			if(i<P.length&&P[i]!=null){
				try {
					s.append("P"+(i+1)+" - "+P[i]);
					SD10.class.getMethod(P[i]);
				} catch (NoSuchMethodException e) {
					System.err.println(e.getMessage());
					System.exit(-1);
				}
			}
			s.append(" / ");
			if(i<S.length&&S[i]!=null){
				try {
					s.append("S"+(i+1)+" - "+S[i]);
					SD10.class.getMethod(S[i]);
				} catch (NoSuchMethodException e) {
					System.err.println(e.getMessage());
					System.exit(-1);
				}
			}
			System.out.println(s.toString());
		}
	}
	
    public SD10(String host){
    	if(host!=null) io = new OlaWeb(host);
    }
    public SD10(){
    	io = null;
    }
    private void init(){
    	for(int i=0;i<data.length;i++) data[i]=0;
    	step=0;
    }
	public static void main(String[] args) throws Exception {
		SD10 o = new SD10();
		if(args.length>0) o=new SD10(args[0]);
		while(o.loop());
		System.out.println("bye!");
	}
	private boolean loop() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		compile();
		System.out.println("Choice?");
		
		int key = System.in.read();
		//System.out.println(key);
		if(key==115||key==83) {
			cat = "S";
			key = System.in.read();
		}else if(key==112||key==80) {
			cat = "P";
			key = System.in.read();
		}
		if(Character.isDigit(key)){
			rank = Character.getNumericValue(key);
		}else if(key==10){
			//runs with default cat and rank (i.e. P1+)
		}else{
			return false;
		}
		if(key!=10)System.in.read();
		//System.out.println(key);
		init();
		
		String name = name();
		System.out.println("### "+name+" ###");
		SD10.class.getMethod(name).invoke(this);
		
		blackout();
		return rank();
	}
	/**
	 * @return false if max rank is reached
	 */
	private boolean rank(){
		if(cat.equals("S"))
			return rank(S); 
		else
			return rank(P);
	}
	private boolean rank(String[] t){
		return ++rank<=t.length;
	}
	private String name(){
		if(cat.equals("S"))
			return name(S); 
		else
			return name(P);
	}
	private String name(String[] t){
		return t[rank-1];
	}
	/**
	 * RGB REAR EuroliteBigParty 7 colors chase
	 */
	public void led() throws IOException{
		print();
		SWITCH.set(true,true,true,true).set(data);
		WIZARD.disco().set(data);
		ledstep(PresetColor.RED,100);
		pause();
		print();
		ledstep(PresetColor.ORANGE,100);
		pause();
		print();
		ledstep(PresetColor.YELLOW,100);
		pause();
		print();
		ledstep(PresetColor.GREEN,100);
		pause();
		print();
		ledstep(PresetColor.CYAN,100);
		pause();
		print();
		ledstep(PresetColor.BLUE,100);
		pause();
		print();
		ledstep(PresetColor.VIOLET,100);
		pause();
	}
	private void ledstep(PresetColor c, int dim) throws IOException{
		PresetColor dicrocol = c;
		
		if(c==PresetColor.VIOLET) dicrocol = PresetColor.BLUE;
		if(c==PresetColor.ORANGE) dicrocol = PresetColor.RED;
		if(c==PresetColor.CYAN) dicrocol = PresetColor.BLUE;
		if(c==PresetColor.BLUE) dicrocol = PresetColor.GREEN;
		if(c==PresetColor.GREEN) dicrocol = PresetColor.BLUE;
		if(c==PresetColor.RED) dicrocol = PresetColor.YELLOW;
		if(c==PresetColor.YELLOW) dicrocol = PresetColor.RED;
		
		EUR1.color(c,dim).set(data);
		EUR2.color(PresetColor.BLACK,0).set(data);
		EUR3.color(PresetColor.BLACK,0).set(data);
		DICRO.color(dicrocol, 100);
		pause();
		print();
		EUR1.color(PresetColor.BLACK,0).set(data);
		EUR2.color(c,dim).set(data);
		EUR3.color(PresetColor.BLACK,0).set(data);
		DICRO.color(PresetColor.BLACK, 0);
		pause();
		print();
		EUR1.color(PresetColor.BLACK,0).set(data);
		EUR2.color(PresetColor.BLACK,0).set(data);
		EUR3.color(c,dim).set(data);
		DICRO.color(dicrocol, 100);
	}
	public void srainb() throws IOException{
		EUR1.strob(true);
		EUR2.strob(true);
		EUR3.strob(true);
		rainb();
	}
	public void rainb() throws IOException{
		print();
		EUR1.color(PresetColor.RED,100).set(data);
		EUR2.color(PresetColor.ORANGE,100).set(data);
		EUR3.color(PresetColor.YELLOW,100).set(data);
		pause();
		print();
		EUR1.color(PresetColor.ORANGE,100).set(data);
		EUR2.color(PresetColor.YELLOW,100).set(data);
		EUR3.color(PresetColor.GREEN,100).set(data);
		pause();
		print();
		EUR1.color(PresetColor.YELLOW,100).set(data);
		EUR2.color(PresetColor.GREEN,100).set(data);
		EUR3.color(PresetColor.CYAN,100).set(data);
		pause();
		print();
		EUR1.color(PresetColor.GREEN,100).set(data);
		EUR2.color(PresetColor.CYAN,100).set(data);
		EUR3.color(PresetColor.BLUE,100).set(data);
		pause();
		print();
		EUR1.color(PresetColor.CYAN,100).set(data);
		EUR2.color(PresetColor.BLUE,100).set(data);
		EUR3.color(PresetColor.VIOLET,100).set(data);
		pause();
		print();
		EUR1.color(PresetColor.BLUE,100).set(data);
		EUR2.color(PresetColor.VIOLET,100).set(data);
		EUR3.color(PresetColor.RED,100).set(data);
		pause();
		print();
		EUR1.color(PresetColor.VIOLET,100).set(data);
		EUR2.color(PresetColor.RED,100).set(data);
		EUR3.color(PresetColor.ORANGE,100).set(data);
		pause();
	}
	/**
	 * RGB8W RGB LEFT/RIGHT CHASE
	 */
	public void rgb() throws IOException{
		print();
		SWITCH.set(true,true,true,true).set(data);
		WIZARD.disco().set(data);
		RGB1.color(PresetColor.RED,100).set(data);
		RGB2.color(PresetColor.BLACK,0).set(data);
		print();
		pause();
		RGB2.color(PresetColor.RED,100).set(data);
		RGB1.color(PresetColor.BLACK,0).set(data);
		pause();
		print();
		RGB1.color(PresetColor.GREEN,100).set(data);
		RGB2.color(PresetColor.BLACK,0).set(data);
		pause();
		print();
		RGB2.color(PresetColor.GREEN,100).set(data);
		RGB1.color(PresetColor.BLACK,0).set(data);
		pause();
		print();
		RGB1.color(PresetColor.BLUE,100).set(data);
		RGB2.color(PresetColor.BLACK,0).set(data);
		pause();
		print();
		RGB2.color(PresetColor.BLUE,100).set(data);
		RGB1.color(PresetColor.BLACK,0).set(data);
		pause();
	}
	/**
	 * RGB8W + REAR EuroliteBigParty RGB LEFT/RIGHT CHASE
	 */
	public void rgbled() throws IOException{
		print();
		SWITCH.set(true,true,true,true).set(data);
		WIZARD.disco().set(data);
		RGB1.color(PresetColor.RED,100).set(data);
		RGB2.color(PresetColor.BLACK,0).set(data);
		EUR1.color(PresetColor.YELLOW,100).set(data);
		EUR2.color(PresetColor.RED,100).set(data);
		EUR3.color(PresetColor.ORANGE,100).set(data);
		pause();
		RGB2.color(PresetColor.RED,100).set(data);
		RGB1.color(PresetColor.BLACK,0).set(data);
		EUR3.color(PresetColor.YELLOW,100).set(data);
		EUR2.color(PresetColor.RED,100).set(data);
		EUR1.color(PresetColor.ORANGE,100).set(data);
		pause();
		print();
		RGB1.color(PresetColor.GREEN,100).set(data);
		RGB2.color(PresetColor.BLACK,0).set(data);
		EUR1.color(PresetColor.YELLOW,100).set(data);
		EUR2.color(PresetColor.GREEN,100).set(data);
		EUR3.color(PresetColor.CYAN,100).set(data);
		pause();
		print();
		RGB2.color(PresetColor.GREEN,100).set(data);
		RGB1.color(PresetColor.BLACK,0).set(data);
		EUR3.color(PresetColor.YELLOW,100).set(data);
		EUR2.color(PresetColor.GREEN,100).set(data);
		EUR1.color(PresetColor.CYAN,100).set(data);
		pause();
		print();
		RGB1.color(PresetColor.BLUE,100).set(data);
		RGB2.color(PresetColor.BLACK,0).set(data);
		EUR1.color(PresetColor.YELLOW,100).set(data);
		EUR2.color(PresetColor.BLUE,100).set(data);
		EUR3.color(PresetColor.CYAN,100).set(data);
		pause();
		print();
		RGB2.color(PresetColor.BLUE,100).set(data);
		RGB1.color(PresetColor.BLACK,0).set(data);
		EUR3.color(PresetColor.YELLOW,100).set(data);
		EUR2.color(PresetColor.BLUE,100).set(data);
		EUR1.color(PresetColor.CYAN,100).set(data);
		pause();
	}
	public void blackout() throws IOException{
		dmxapply();		
	}

	public void strob() throws IOException{
		print();
		WIZARD.disco().set(data);
		SWITCH.set(true,true,true,true).set(data);
		STROB.set().set(data);
		pause();
	}
	/**
	 * DICRO/LED RGBA + ALL SWITCHS ON
	 */
	public void dicro() throws IOException{
		print();
		SWITCH.set(true,true,true,true).set(data);
		WIZARD.disco().set(data);
		DICRO.color(PresetColor.RED,100).set(data);
		EUR1.color(PresetColor.RED,100).set(data);
		EUR2.color(PresetColor.BLACK,0).set(data);
		EUR3.color(PresetColor.BLACK,0).set(data);
		pause();
		print();
		DICRO.color(PresetColor.GREEN,100).set(data);
		EUR2.color(PresetColor.GREEN,100).set(data);
		EUR1.color(PresetColor.BLACK,0).set(data);
		EUR3.color(PresetColor.BLACK,0).set(data);
		pause();
		print();
		DICRO.color(PresetColor.BLUE,100).set(data);
		EUR3.color(PresetColor.BLUE,100).set(data);
		EUR2.color(PresetColor.BLACK,0).set(data);
		EUR1.color(PresetColor.BLACK,0).set(data);
		pause();
		print();
		DICRO.color(PresetColor.YELLOW,100).set(data);
		EUR2.color(PresetColor.YELLOW,100).set(data);
		EUR1.color(PresetColor.BLACK,0).set(data);
		EUR3.color(PresetColor.BLACK,0).set(data);
		pause();
	}
	private OlaWeb io;
	private void dmxapply(){
		if(io!=null) io.send(data);
	}

	private void pause() throws IOException{
		dmxapply();
		System.in.read();
	}
	private void print(){
		System.out.println("*** "+cat+rank+":"+step+++" ***");
	}
}
