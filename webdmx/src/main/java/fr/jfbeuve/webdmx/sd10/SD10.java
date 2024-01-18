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

	// RGBAWUV 70 Right 80 Drum Right 90 Drum Left 100 Left
    
    private int[] data = new int[512];
    
    private int step=0;
	private String cat = "P";
	private int rank = 1;

	private static final String[] P={"dj","cold"};
	private static final String[] S={"djstrob","warm"};

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

	private void setdiscofixture(Fixture f, PresetColor c, int dim, boolean strob){
		if(dim==0) c=PresetColor.BLACK;
		f.color(c,dim).strob(dim==0?false:strob).set(data);
	}

	private void discostep(PresetColor c, int pos, boolean strob) throws IOException{
		setdiscofixture(AV1,c,pos==1?100:0,strob);
		setdiscofixture(AV2,c,pos==2||pos==4?100:0,strob);
		setdiscofixture(AV3,c,pos==3?100:0,strob);
		setdiscofixture(LEFT,c,pos==1?100:0,strob);
		setdiscofixture(LEAD,c,pos==2?100:0,strob);
		setdiscofixture(DRUMRIGHT,c,pos==3?100:0,strob);
		setdiscofixture(RIGHT,c,pos==4?100:0,strob);
		pause();
	}
	public void dj() throws IOException{
		disco(false);
	}
	public void djstrob() throws IOException{
		disco(true);
	}
	private void disco(boolean strob) throws IOException{
		print();
		WIZARD.disco().set(data);
		for(int i=1;i<5;i++) discostep(PresetColor.RED,i,strob);print();
		for(int i=1;i<5;i++) discostep(PresetColor.ORANGE,i,strob);print();
		for(int i=1;i<5;i++) discostep(PresetColor.VIOLET,i,strob);print();
		for(int i=1;i<5;i++) discostep(PresetColor.GREEN,i,strob);print();
		for(int i=1;i<5;i++) discostep(PresetColor.CYAN,i,strob);print();
		for(int i=1;i<5;i++) discostep(PresetColor.BLUE,i,strob);print();
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
