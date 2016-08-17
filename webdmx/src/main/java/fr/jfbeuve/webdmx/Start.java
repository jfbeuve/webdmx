package fr.jfbeuve.webdmx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Start {
	public static void main(String[] args) throws Exception {
		if(args.length>0&&(args[0].toLowerCase().endsWith("-version")))
			version();
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
}
