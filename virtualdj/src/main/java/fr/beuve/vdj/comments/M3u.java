package fr.beuve.vdj.comments;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class M3u implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Date date;
	
	public Date getDate() {
		return date;
	}

	private List<String> files = new ArrayList<String>();
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	
	public M3u(File m3u) throws ParseException, IOException {
		
		String name = m3u.getName();
		int dot = name.lastIndexOf(".");
		String sdate = name.substring(0,dot);
		date = sdf.parse(sdate);
		
		LineNumberReader lnr = new LineNumberReader(new FileReader(m3u));
		try{
			String line = lnr.readLine();
			while(line!=null){
				if(!line.startsWith("#")){
					int slash = line.lastIndexOf(File.separator);
					files.add(line.substring(slash+1));
				}
				line = lnr.readLine();
			}
		}finally{
			lnr.close();
		}
	}
	
	public boolean contains(String filename){
		for(String item : files){
			if(item.equals(filename)) return true;
		}
		Logger.getLogger(M3u.class).debug(filename+" IS NOT A HIT!");
		return false;
	}
}
