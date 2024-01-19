package fr.jfbeuve.webdmx.sd10;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import fr.jfbeuve.webdmx.io.OlaWeb;
import fr.jfbeuve.webdmx.preset.PresetColor;

public class SD10 {
	private static final RGB8W AV1=new RGB8W(0),AV2=new RGB8W(5),AV3=new RGB8W(10),AV4=new RGB8W(15);
	private static final RGBEuroliteBP AR1=new RGBEuroliteBP(55),AR3=new RGBEuroliteBP(60),AR4=new RGBEuroliteBP(65);
    private static final RGB10MM DR2 = new RGB10MM(30);
    private static final RGB10MM DR4 = new RGB10MM(48);
    private static final WIZARD WIZARD = new WIZARD(37);
	private static final RGBWAUV7 LEFT=new RGBWAUV7(70), LEAD =new RGBWAUV7(80),DRUMRIGHT=new RGBWAUV7(90),RIGHT=new RGBWAUV7(100);

    private int[] data = new int[512];
    
    private int step=0;
	private String cat = "P";
	private int rank = 1;

	private static final String[] P={"dj","cold","warm"};
	private static final String[] S={"strob","dimcold","dimwarm"};

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
		io = new OlaWeb("192.168.1.61");
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

	private void strobostep(PresetColor c, boolean on) throws IOException{
		AV1.color(c,on?100:0).set(data);
		AV2.color(c,on?100:0).set(data);
		AV3.color(c,on?100:0).set(data);
		LEFT.color(c,100).strob(true).set(data);
		LEAD.color(c,100).strob(true).set(data);
		DRUMRIGHT.color(c,100).strob(true).set(data);
		RIGHT.color(c,100).strob(true).set(data);
		pause();
	}

	private void djstep(PresetColor c, int pos) throws IOException{
		AV1.color(c,pos==1?100:0).set(data);
		AV2.color(c,pos==2||pos==4?100:0).set(data);
		AV3.color(c,pos==3?100:0).set(data);
		LEFT.color(c,pos==1?100:0).set(data);
		LEAD.color(c,pos==2?100:0).set(data);
		DRUMRIGHT.color(c,pos==3?100:0).set(data);
		RIGHT.color(c,pos==4?100:0).set(data);
		pause();
	}

	public void strob() throws IOException{
		print();
		WIZARD.disco().set(data);
		strobostep(PresetColor.RED, true);print();
		strobostep(PresetColor.RED, false);print();
		strobostep(PresetColor.ORANGE, true);print();
		strobostep(PresetColor.ORANGE, false);print();
		strobostep(PresetColor.VIOLET, true);print();
		strobostep(PresetColor.VIOLET, false);print();
		strobostep(PresetColor.GREEN, true);print();
		strobostep(PresetColor.GREEN, false);print();
		strobostep(PresetColor.CYAN, true);print();
		strobostep(PresetColor.CYAN, false);print();
		strobostep(PresetColor.BLUE, true);print();
		strobostep(PresetColor.BLUE, false);
	}
	public void dj() throws IOException{
		print();
		WIZARD.disco().set(data);
		for(int i=1;i<5;i++) {djstep(PresetColor.RED,i);print();}
		for(int i=1;i<5;i++) {djstep(PresetColor.ORANGE,i);print();}
		for(int i=1;i<5;i++) {djstep(PresetColor.VIOLET,i);print();}
		for(int i=1;i<5;i++) {djstep(PresetColor.GREEN,i);print();}
		for(int i=1;i<5;i++) {djstep(PresetColor.CYAN,i);print();}
		for(int i=1;i<5;i++) {djstep(PresetColor.BLUE,i);print();}
	}

	public void cold() throws IOException{
		step(PresetColor.CYAN,PresetColor.VIOLET);
		step(PresetColor.VIOLET,PresetColor.CYAN);
		step(PresetColor.VIOLET,PresetColor.ORANGE);
		step(PresetColor.CYAN,PresetColor.ORANGE);
	}
	public void warm() throws IOException{
		step(PresetColor.YELLOW,PresetColor.RED);
		step(PresetColor.RED,PresetColor.YELLOW);
		step(PresetColor.ORANGE,PresetColor.RED);
		step(PresetColor.RED,PresetColor.ORANGE);
	}

	public void dimcold() throws IOException{
		dim(PresetColor.CYAN,PresetColor.VIOLET);
		dim(PresetColor.VIOLET,PresetColor.CYAN);
		dim(PresetColor.VIOLET,PresetColor.ORANGE);
		dim(PresetColor.CYAN,PresetColor.ORANGE);
	}
	public void dimwarm() throws IOException{
		dim(PresetColor.YELLOW,PresetColor.RED);
		dim(PresetColor.RED,PresetColor.YELLOW);
		dim(PresetColor.ORANGE,PresetColor.RED);
		dim(PresetColor.RED,PresetColor.ORANGE);
	}

	private void step(PresetColor c1, PresetColor c2) throws IOException {
		print();
		AV1.color(c1,20).set(data);
		LEFT.color(c1,100).set(data);
		AV2.color(c2,20).set(data);
		LEAD.color(c2,100).set(data);
		DRUMRIGHT.color(c2,100).set(data);
		AV4.color(c1,20).set(data);
		RIGHT.color(c1,100).set(data);
		pause();
		print();
		AV1.color(c1,20).set(data);
		LEFT.color(c1,100).set(data);
		AV2.color(c1,20).set(data);
		LEAD.color(c1,100).set(data);
		DRUMRIGHT.color(c1,100).set(data);
		AV4.color(c2,20).set(data);
		RIGHT.color(c2,100).set(data);
		pause();
		print();
		AV1.color(c1,20).set(data);
		LEFT.color(c1,100).set(data);
		AV2.color(c2,20).set(data);
		LEAD.color(c2,100).set(data);
		DRUMRIGHT.color(c2,100).set(data);
		AV4.color(c1,20).set(data);
		RIGHT.color(c1,100).set(data);
		pause();
		print();
		AV1.color(c2,20).set(data);
		LEFT.color(c2,100).set(data);
		AV2.color(c1,20).set(data);
		LEAD.color(c1,100).set(data);
		DRUMRIGHT.color(c1,100).set(data);
		AV4.color(c1,20).set(data);
		RIGHT.color(c1,100).set(data);
		pause();
		print();
		AV1.color(c1,20).set(data);
		LEFT.color(c1,100).set(data);
		AV2.color(c2,20).set(data);
		LEAD.color(c2,100).set(data);
		DRUMRIGHT.color(c2,100).set(data);
		AV4.color(c1,20).set(data);
		RIGHT.color(c1,100).set(data);
		pause();
	}

	private void dim(PresetColor c1, PresetColor c2) throws IOException {
		print();
		AV1.color(c1,20).set(data);
		LEFT.color(c1,100).set(data);
		AV2.color(c2,20).set(data);
		LEAD.color(c2,100).set(data);
		DRUMRIGHT.color(c2,100).set(data);
		AV4.color(c1,20).set(data);
		RIGHT.color(c1,100).set(data);
		pause();
		print();
		AV1.color(c1,100).set(data);
		LEFT.color(c1,50).set(data);
		AV2.color(c2,20).set(data);
		LEAD.color(c2,100).set(data);
		DRUMRIGHT.color(c2,100).set(data);
		AV4.color(c1,20).set(data);
		RIGHT.color(c1,100).set(data);
		pause();
		print();
		AV1.color(c1,20).set(data);
		LEFT.color(c1,100).set(data);
		AV2.color(c2,20).set(data);
		LEAD.color(c2,100).set(data);
		DRUMRIGHT.color(c2,100).set(data);
		AV4.color(c1,20).set(data);
		RIGHT.color(c1,100).set(data);
		pause();
		print();
		AV1.color(c1,20).set(data);
		LEFT.color(c1,100).set(data);
		AV2.color(c2,20).set(data);
		LEAD.color(c2,100).set(data);
		DRUMRIGHT.color(c2,100).set(data);
		AV4.color(c1,100).set(data);
		RIGHT.color(c1,50).set(data);
		pause();
		print();
		AV1.color(c1,20).set(data);
		LEFT.color(c1,100).set(data);
		AV2.color(c2,20).set(data);
		LEAD.color(c2,100).set(data);
		DRUMRIGHT.color(c2,100).set(data);
		AV4.color(c1,20).set(data);
		RIGHT.color(c1,100).set(data);
		pause();
		print();
		AV1.color(c1,20).set(data);
		LEFT.color(c1,100).set(data);
		AV2.color(c2,100).set(data);
		LEAD.color(c2,50).set(data);
		DRUMRIGHT.color(c2,50).set(data);
		AV4.color(c1,20).set(data);
		RIGHT.color(c1,100).set(data);
		pause();
	}
	public void blackout() throws IOException{
		dmxapply();		
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
