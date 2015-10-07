package fr.beuve.vdj.comments;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract class Song {
	public String name;
	public String fqname;

	protected Date date;
	public long size;
	protected Logger logger = Logger.getLogger(Song.class);
	private DateFormat newdf = new SimpleDateFormat("yyyy");
	protected DateFormat dateVDJ = new SimpleDateFormat("yyMMddHHmm");
	public String comment;
	
	protected int version; // VirtualDJ_Database Version
	private DateFormat dateDisplay = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	protected Song(){};
	
	protected Song(Node node, int _version) throws DOMException, ParseException{
		// LEGACY name = node.getAttributes().getNamedItem("FileName").getNodeValue();
		name = node.getAttributes().getNamedItem("FilePath").getNodeValue();
		int slash = name.lastIndexOf(File.separator);
		name = name.substring(slash+1);		
		
		date = dateVDJ.parse(node.getAttributes().getNamedItem("TagDate").getNodeValue());
		size = Long.valueOf(node.getAttributes().getNamedItem("FileSize").getNodeValue());
		logger.debug(name +" "+dateDisplay.format(date));
		version=_version;
	}
	
	protected Song(Song song){
		name = song.name;
		date = new Date();
		size = song.size;
		comment = song.comment;
	}
	
	public Date getDate(){
		return date;
	}
	public String getName(){
		return name;
	}
	public String tagNew(){
		return "NEW "+newdf.format(date);
	}

}
