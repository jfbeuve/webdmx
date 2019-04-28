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
    
    private int[] data = new int[512];
    
    private int step=0;
	private String cat = "P";
	private int rank = 1;
	
	private static final String[] P={"disco3","scnwit","scncold"};
	private static final String[] S={"disco6","scnrbw","scnwarm"};
	//TODO P5 1-20 Wizard FX 80% S5-1 Wizard music S5-2 Wizard FX random 80% 
	
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

	private void discostep(PresetColor c, int pos) throws IOException{
		if(pos!=1) AV1.color(PresetColor.BLACK,0).set(data);
		if(pos!=2) AV2.color(PresetColor.BLACK,0).set(data);
		if(pos!=3) AV3.color(PresetColor.BLACK,0).set(data);
		if(pos==1) AV1.color(c,100).set(data);
		if(pos==2) AV2.color(c,100).set(data);
		if(pos==3) AV3.color(c,100).set(data);
		pause();
	}

	public void disco3() throws IOException{
		print();
		WIZARD.disco().set(data);
		discostep(PresetColor.RED,1);print();
		discostep(PresetColor.RED,2);print();
		discostep(PresetColor.RED,3);print();
		discostep(PresetColor.GREEN,1);print();
		discostep(PresetColor.GREEN,2);print();
		discostep(PresetColor.GREEN,3);print();
		discostep(PresetColor.BLUE,1);print();
		discostep(PresetColor.BLUE,2);print();
		discostep(PresetColor.BLUE,3);	
	}
	public void disco6() throws IOException{
		print();
		WIZARD.disco().set(data);
		discostep(PresetColor.RED,1);print();
		discostep(PresetColor.ORANGE,2);print();
		discostep(PresetColor.YELLOW,3);print();
		discostep(PresetColor.YELLOW,1);print();
		discostep(PresetColor.RED,2);print();
		discostep(PresetColor.ORANGE,3);print();
		discostep(PresetColor.ORANGE,1);print();
		discostep(PresetColor.YELLOW,2);print();
		discostep(PresetColor.RED,3);print();
		
		discostep(PresetColor.CYAN,1);print();
		discostep(PresetColor.BLUE,2);print();
		discostep(PresetColor.VIOLET,3);print();
		discostep(PresetColor.VIOLET,1);print();
		discostep(PresetColor.CYAN,2);print();
		discostep(PresetColor.BLUE,3);print();
		discostep(PresetColor.BLUE,1);print();
		discostep(PresetColor.VIOLET,2);print();
		discostep(PresetColor.CYAN,3);
	}
	public void scnwit() throws IOException{
		print();
		DR2.color(PresetColor.WHITE,100).set(data);
		DR4.color(PresetColor.WHITE,100).set(data);
		pause();
	}
	public void scnrbw() throws IOException{
		print();
		DR2.color(PresetColor.RED,100).set(data);
		DR4.color(PresetColor.RED,100).set(data);
		pause();
		print();
		DR2.color(PresetColor.ORANGE,100).set(data);
		DR4.color(PresetColor.ORANGE,100).set(data);
		pause();
		print();
		DR2.color(PresetColor.YELLOW,100).set(data);
		DR4.color(PresetColor.YELLOW,100).set(data);
		pause();
		print();
		DR2.color(PresetColor.GREEN,100).set(data);
		DR4.color(PresetColor.GREEN,100).set(data);
		pause();
		print();
		DR2.color(PresetColor.CYAN,100).set(data);
		DR4.color(PresetColor.CYAN,100).set(data);
		pause();
		print();
		DR2.color(PresetColor.BLUE,100).set(data);
		DR4.color(PresetColor.BLUE,100).set(data);
		pause();
		print();
		DR2.color(PresetColor.VIOLET,100).set(data);
		DR4.color(PresetColor.VIOLET,100).set(data);
		pause();
	}
	public void scncold() throws IOException{
		print();
		DR2.color(PresetColor.CYAN,100).set(data);
		DR4.color(PresetColor.VIOLET,100).set(data);
		pause();
		print();
		DR2.color(PresetColor.VIOLET,100).set(data);
		DR4.color(PresetColor.CYAN,100).set(data);
		pause();
	}
	public void scnwarm() throws IOException{
		print();
		DR2.color(PresetColor.RED,100).set(data);
		DR4.color(PresetColor.YELLOW,100).set(data);
		pause();
		print();
		DR2.color(PresetColor.YELLOW,100).set(data);
		DR4.color(PresetColor.RED,100).set(data);
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
