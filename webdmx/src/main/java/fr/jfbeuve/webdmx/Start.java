package fr.jfbeuve.webdmx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.juanjo.openDmx.OpenDmx;


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Start {
	public static void main(String[] args) throws Exception {
		if(args.length>0&&(args[0].toLowerCase().endsWith("-version")))
			version();
		else if(args.length>0&&(args[0].toLowerCase().endsWith("-test")))
			test();
		else
			SpringApplication.run(Start.class, args);
	}
	private static void version(){
		Properties props = new Properties();
		InputStream is = Start.class.getClassLoader().getResourceAsStream("META-INF/maven/fr.jfbeuve/webdmx/pom.properties");
		try {
			props.load(is);
			String version = props.getProperty("version");
			System.out.println(version);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void test() {
		
		boolean exit=false;
		
		//open send mode
		if(!OpenDmx.connect(OpenDmx.OPENDMX_TX)){
			System.out.println("Open Dmx widget not detected!");
			return;
		}
		
		//reader for console input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String cmd,cmd1,cmd2;
		int sep,channel,value;

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
						value=Integer.parseInt(cmd2);
						
						if(channel<1)channel=1;
						if(channel>512)channel=512;
						if(value<0)value=0;
						if(value>255)value=255;
						
						//update dmx output
						OpenDmx.setValue(channel-1,value);
						
						System.out.println(">dmx channel "+String.valueOf(channel)+" set to value "+String.valueOf(value)+"!");
					}
					catch(Exception e){
						System.out.println(">Incorrect command!");
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		System.out.println("bye!");
		
		//close
		OpenDmx.disconnect();
	}
}
