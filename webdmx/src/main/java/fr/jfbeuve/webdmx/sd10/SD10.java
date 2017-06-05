package fr.jfbeuve.webdmx.sd10;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
	private String cat = "P";
	private int rank = 1;
	
	//TODO update SD10 trioled2, trioled2disco
	//TODO wizard strob
	// CARNAVAL SAINT PIERRE trioled2, trioled2disco
	private static final String[] P={"disco", "slow", "bistro","bistroled","triolite","discoled3","discoled2"};
	private static final String[] S={"strob", "music","sync",  "syncled",  "trio",    "discoled4", "discoled2trio"};
	
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
    	dim=100;
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
	 * DICRO/LED RGBA + ALL SWITCHS ON
	 */
	public void disco() throws IOException{
		print();
		SWITCH.set(true,true,true,true).set(data);
		WIZARD.disco().set(data);
		DICRO.color(PresetColor.R,100).set(data);
		RGB4.color(PresetColor.R,100).set(data);
		pause();
		print();
		DICRO.color(PresetColor.G,100).set(data);
		RGB4.color(PresetColor.G,100).set(data);
		pause();
		print();
		DICRO.color(PresetColor.B,100).set(data);
		RGB4.color(PresetColor.B,100).set(data);
		pause();
		print();
		DICRO.color(PresetColor.YELLOW,100).set(data);
		RGB4.color(PresetColor.YELLOW,100).set(data);
		pause();
		print();
		DICRO.color(PresetColor.BLACK,100).set(data);
		RGB4.color(PresetColor.BLACK,100).set(data);
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
	 * LIGHT RGB LED/DICRO, ONLY SWITCH #2 ON 
	 */
	public void slow() throws IOException{
		print();
		WIZARD.slow().set(data);
		SWITCH.set(false,true,false,false).set(data);
		colorall(PresetColor.B,3);
		pause();
		print();
		colorall(PresetColor.G,3);
		pause();
		print();
		colorall(PresetColor.R,3);
		pause();
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
	private void syncstep(PresetColor bg, PresetColor c) throws IOException{
		if(bg==c)return;
		print();
		bgcolor(bg,100);
		color(c,100);
		pause();
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
	
	private OlaWeb io;
	private void dmxapply(){
		if(io!=null) io.send(data);
	}
	public void music() throws IOException{
		print();
		WIZARD.disco().set(data);
		SWITCH.set(true,true,true,true).set(data);
		RGB1.music().set(data);
		RGB2.music().set(data);
		RGB3.music().set(data);
		RGB4.music().set(data);
		LED.music().set(data);
		pause();
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
	private void bistrostep(PresetColor c) throws IOException{
		syncstep(PresetColor.RED,c);
		syncstep(PresetColor.GREEN,c);
		syncstep(PresetColor.BLUE,c);
		syncstep(PresetColor.YELLOW,c);
	}
	private void bistroledstep(PresetColor c) throws IOException{
		syncstep(PresetColor.RED,c);
		syncstep(PresetColor.GREEN,c);
		syncstep(PresetColor.BLUE,c);
		syncstep(PresetColor.YELLOW,c);
		syncstep(PresetColor.ORANGE,c);
		syncstep(PresetColor.VIOLET,c);
		syncstep(PresetColor.CYAN,c);
	}
	public void bistroled() throws IOException{
		bistroledstep(PresetColor.RED);
		bistroledstep(PresetColor.GREEN);
		bistroledstep(PresetColor.BLUE);
		bistroledstep(PresetColor.YELLOW);
		bistroledstep(PresetColor.ORANGE);
		bistroledstep(PresetColor.VIOLET);
		bistroledstep(PresetColor.CYAN);
	}
	public void syncled() throws IOException{
		syncstep(PresetColor.RED,PresetColor.YELLOW);
		syncstep(PresetColor.YELLOW,PresetColor.RED);
		syncstep(PresetColor.GREEN,PresetColor.BLUE);
		syncstep(PresetColor.BLUE,PresetColor.GREEN);
		syncstep(PresetColor.CYAN,PresetColor.VIOLET);
		syncstep(PresetColor.VIOLET,PresetColor.CYAN);	
	}
	private final int LITE = 20;
	public void triolite() throws IOException{
		dim=LITE;
		trio();
	}
	public void trio() throws IOException{
		triosteps(PresetColor.CYAN,PresetColor.VIOLET);
		triosteps(PresetColor.VIOLET,PresetColor.CYAN);
		triosteps(PresetColor.RED,PresetColor.YELLOW);
		triosteps(PresetColor.YELLOW,PresetColor.RED);
		triosteps(PresetColor.BLUE,PresetColor.GREEN);
		triosteps(PresetColor.GREEN,PresetColor.BLUE);
	}
	private void triosteps(PresetColor bg, PresetColor c) throws IOException{
		triostep(c,bg,bg,dim);
		triostep(bg,c,bg,dim);
		triostep(bg,bg,c,dim);
		triostep(bg,c,bg,dim);
		triostep(c,bg,bg,dim);
	}
	private void triostep(PresetColor c1, PresetColor c2, PresetColor c3, int dim) throws IOException{
		print();
		RGB3.color(c1,dim).set(data);
		LED.color(c2,100).set(data);
		RGB4.color(c3,dim).set(data);
		pause();
	}
	public void discoled4() throws IOException{
		discoled(4);
	}
	public void discoled3() throws IOException{
		discoled(3);
	}
	public void discoled2() throws IOException{
		discoled2(false);
	}
	public void discoled2trio() throws IOException{
		discoled2(true);
	}
	public void discoled2(boolean trio) throws IOException{
		print();
		WIZARD.disco().set(data);
		SWITCH.set(true,true,true,true).set(data);
		discoled2step(PresetColor.R,trio);
		print();
		discoled2step(PresetColor.G,trio);
		print();
		discoled2step(PresetColor.B,trio);
		print();
		discoled2step(PresetColor.YELLOW,trio);
		print();
		discoled2step(PresetColor.VIOLET,trio);
		print();
		discoled2step(PresetColor.CYAN,trio);
		print();
	}
	private void discoled2step(PresetColor c, boolean trio) throws IOException{
		RGB1.color(c, dim);
		RGB2.color(PresetColor.BLACK, dim);
		if(trio)triostep(c, reverse(c), reverse(c), LITE);
		pause();print();
		RGB1.color(PresetColor.BLACK, dim);
		RGB2.color(c, dim);
		if(trio)triostep(reverse(c), c, reverse(c), LITE);
		pause();print();
		RGB1.color(c, dim);
		RGB2.color(PresetColor.BLACK, dim);
		if(trio)triostep(reverse(c), reverse(c),c, LITE);
		pause();print();
		//NOTE remove last step?
		RGB1.color(PresetColor.BLACK, dim);
		RGB2.color(PresetColor.BLACK, dim);
		if(trio)triostep(reverse(c), reverse(c), reverse(c), LITE);
		pause();
	}
	private PresetColor reverse(PresetColor c){
		if(c==PresetColor.BLACK) return PresetColor.WHITE;
		if(c==PresetColor.CYAN) return PresetColor.VIOLET;
		if(c==PresetColor.VIOLET) return PresetColor.CYAN;
		if(c==PresetColor.R) return PresetColor.YELLOW;
		if(c==PresetColor.RED) return PresetColor.YELLOW;
		if(c==PresetColor.YELLOW) return PresetColor.RED;
		if(c==PresetColor.G) return PresetColor.BLUE;
		if(c==PresetColor.GREEN) return PresetColor.BLUE;
		if(c==PresetColor.B) return PresetColor.GREEN;
		if(c==PresetColor.BLUE) return PresetColor.GREEN;
		return PresetColor.BLACK;
	}
	private void discoled(int nbfxtr) throws IOException{
		print();
		WIZARD.disco().set(data);
		SWITCH.set(true,true,true,true).set(data);
		discoledstep(PresetColor.CYAN,nbfxtr);
		print();
		discoledstep(PresetColor.VIOLET,nbfxtr);
		print();
		discoledstep(PresetColor.R,nbfxtr);
		print();
		discoledstep(PresetColor.YELLOW,nbfxtr);
		print();
		discoledstep(PresetColor.B,nbfxtr);
		print();
		discoledstep(PresetColor.G,nbfxtr);
	}
	private void discoledstep(PresetColor c, int nbfxtr) throws IOException{
		RGB1.color(c,dim).set(data);
		RGB2.color(PresetColor.BLACK,dim).set(data);
		RGB3.color(PresetColor.BLACK,dim).set(data);
		RGB4.color(PresetColor.BLACK,dim).set(data);
		pause();
		print();
		RGB1.color(PresetColor.BLACK,dim).set(data);
		RGB2.color(c,dim).set(data);
		RGB3.color(PresetColor.BLACK,dim).set(data);
		RGB4.color(PresetColor.BLACK,dim).set(data);
		pause();
		print();
		RGB1.color(PresetColor.BLACK,dim).set(data);
		RGB2.color(PresetColor.BLACK,dim).set(data);
		RGB3.color(c,dim).set(data);
		RGB4.color(PresetColor.BLACK,dim).set(data);
		pause();
		if(nbfxtr<4) return;
		print();
		RGB1.color(PresetColor.BLACK,dim).set(data);
		RGB2.color(PresetColor.BLACK,dim).set(data);
		RGB3.color(PresetColor.BLACK,dim).set(data);
		RGB4.color(c,dim).set(data);
		pause();
	}

	private void pause() throws IOException{
		dmxapply();
		System.in.read();
	}
	private void print(){
		System.out.println("*** "+cat+rank+":"+step+++" ***");
	}
}
