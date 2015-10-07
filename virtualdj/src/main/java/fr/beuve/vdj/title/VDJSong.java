package fr.beuve.vdj.title;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VDJSong {
	Node node, display;
	String file, title, artist;
	private static String AUTHOR = "Author";
	private static String TITLE = "Title";
	protected Logger logger = Logger.getLogger(VDJSong.class);
	private String repo;
	
	public VDJSong(Node _node, String _repo) throws DOMException, ParseException{
		node = _node;
		repo = _repo;
		
		file = node.getAttributes().getNamedItem("FilePath").getNodeValue();
		logger.debug("*** "+file+" ***");		
		
		//<Display Author="artist" Title="WHAT IS LOVE;HADDAWAY"
		display = display(node);
		if(display!=null){
			artist = getDisplayAttribute(AUTHOR);
			title = getDisplayAttribute(TITLE);
		}
	}
	private String getDisplayAttribute(String name){
		Node attr = display.getAttributes().getNamedItem(name);
		if(attr!=null) return attr.getNodeValue();
		return null;
	}
	
	private Node display(Node song){
		NodeList list = song.getChildNodes();
		for(int i=0; i<list.getLength();i++){
			Node item = list.item(i);
			if(item.getNodeName().equals("Display")) return item;
		}
		return null;
	}
	/**
	 * Fix artist and title based on filename
	 * @return true if xml has been modified and has to be saved
	 */
	public boolean fix(){
		if(display==null) return false; 
		if(!file.startsWith(repo)) return false;
		if(artist!=null){
			if(!artist.equals("Artiste")&&!artist.equals("artist")) {
				//logger.info("not fixing SONG because Artist is "+artist);
				return false;
			}
		}
		
		boolean modified = false;
		String[] split = file.split("-");
		
		if(split.length<2) return false; 
		
		String newArtist = split[0];
		int slash = newArtist.lastIndexOf("\\");
		newArtist = newArtist.substring(slash+1);
		
		String newTitle = split[split.length-1];
		int dot = newTitle.lastIndexOf(".");
		newTitle = newTitle.substring(0,dot);
		
		if(artist!=null && !newArtist.equalsIgnoreCase(artist)){
			modified = true;
			logger.info("replacing "+artist+" by "+newArtist);
			artist = newArtist;
			display.getAttributes().getNamedItem(AUTHOR).setNodeValue(artist);
		}
		
		if(title!=null && !newTitle.equalsIgnoreCase(title)){
			modified = true;
			logger.info("replacing "+title+" by "+newTitle);
			title=newTitle;
			display.getAttributes().getNamedItem(TITLE).setNodeValue(title);
		}
		
		if(modified)logger.info("*** "+file+" ***");
		
		return modified;
	}
}
