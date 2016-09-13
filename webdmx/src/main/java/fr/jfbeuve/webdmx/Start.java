package fr.jfbeuve.webdmx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import fr.jfbeuve.webdmx.io.IOWrapper;
import fr.jfbeuve.webdmx.io.OlaWeb;


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Start {
	public static void main(String[] args) throws Exception {
		if(args.length>0&&(args[0].toLowerCase().endsWith("-version")))
			version();
		else if(args.length>0&&(args[0].toLowerCase().endsWith("-help")))
			help();
		else if(args.length>0&&(args[0].toLowerCase().endsWith("-test"))){
			String host = "localhost";
			if(args.length>1) host = args[1];
			test(host);
		}else if(args.length>0&&(args[0].toLowerCase().endsWith("-strob")))
			strob(args[1]);
		else{
			SpringApplication.run(Start.class, args);
		}
			
	}
	private static void version(){
		Properties props = new Properties();
		InputStream is = Start.class.getClassLoader().getResourceAsStream("META-INF/maven/fr.jfbeuve/webdmx/pom.properties");
		try {
			props.load(is);
			String version = props.getProperty("version");
			System.out.println(version+" "+timestamp());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static String timestamp(){
		InputStream is = Start.class.getClassLoader().getResourceAsStream("META-INF/maven/fr.jfbeuve/webdmx/pom.properties");
		InputStreamReader isr = new InputStreamReader(is);
		LineNumberReader lnr = new LineNumberReader(isr);
		try {
			String line = lnr.readLine(); //#Generated by Maven
			if(line==null) return null;
			return lnr.readLine(); //#Generated by Maven
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void test(String host) {
		//NOTE instantiate WinDll if running on windows and host = localhost
		IOWrapper dmx = new OlaWeb(host);
		int[] data = new int[512];
		
		boolean exit=false;
		
		//reader for console input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String cmd,cmd1,cmd2;
		int sep,channel;
		short value;

		System.out.println(">Write: channel=value (channel 1-512, value 0-255)");

		while(!exit){
					
			try {
				cmd=br.readLine();
				cmd=cmd.trim();
				
				if(cmd.equals("")){
					//exit
					exit=true;
				}
				else{				
					try{
						//parse channel/value
						sep=cmd.indexOf('=');
						cmd1=cmd.substring(0, sep);
						cmd2=cmd.substring(sep+1,cmd.length());
						
						channel=Integer.parseInt(cmd1); //1-512
						value=Short.parseShort(cmd2);
						
						if(channel<1)channel=1;
						if(channel>512)channel=512;
						if(value<0)value=0;
						if(value>255)value=255;
						
						//update dmx output
						data[channel-1]=value;
						long begin = System.nanoTime();
						dmx.send(data);
						long end = System.nanoTime();
						
						long duration = end-begin;
						duration = duration /1000000;
						
						System.out.println(">dmx channel "+String.valueOf(channel)+" set to value "+String.valueOf(value)+"! "+duration+" ms");
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		System.out.println("bye!");
		
		//close
		dmx.disconnect();
	}
	private static void help(){
		System.out.println("Usage: java -jar webdmx.jar -version                    => returns maven version of the build");
		System.out.println("       java -jar webdmx.jar -test                       => allows io testing through command line");
		System.out.println("       java -jar webdmx.jar -test <hostname>            => allows io testing through command line through remote host");
		System.out.println("       java -jar webdmx.jar                             => starts jetty using ola through localhost");
		System.out.println("       java -jar webdmx.jar -Djava.library.path=opendmx => run jetty on windows");
		System.out.println("       java -jar webdmx.jar --remote=<hostname>         => starts jetty using ola through remote host");
		System.out.println("       java -jar webdmx.jar --offline=true              => emulation with no IO");
		System.out.println("       java -jar webdmx.jar -help                       => display command line help (this)");
		System.out.println("       java -jar webdmx.jar -strob <speed>              => flash RGB PAR1 RED at <speed>");
		
	}

	public static void strob(String _speed) throws Exception{
		long speed = Long.parseLong(_speed);
		IOWrapper dmx = new OlaWeb("localhost");
		int[] data = new int[30];
		boolean black=false;
		while(true){
			data[23] = black?0:255;
			dmx.send(data);
			Thread.sleep(speed);
			black = !black;
		}
	}
}
