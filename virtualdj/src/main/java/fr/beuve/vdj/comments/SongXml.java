package fr.beuve.vdj.comments;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


public class SongXml extends Song {
	public static final String SONG = "Song";
	public static final String COMMENT = "Comment";
	public static final String FILENAME = "FileName";
	public static final String FILESIZE = "FileSize";
	public static final String TAGDATE = "TagDate";
	public static final String FILEPATH = "FilePath";
	
	public SongXml(Song song) {
		super(song);
	}

	/**
	 * @return new node for this song
	 */
	public Node node(Document dom){
		Element songNode = dom.createElement(SONG);
		
		//VirtualDJ_Database Version="1"
		if(version == 1) songNode.setAttribute(FILENAME, name);
		
		//VirtualDJ_Database Version="603"
		if(version == 603) songNode.setAttribute(FILEPATH, fqname);
		
		songNode.setAttribute(FILESIZE, Long.toString(size));
		songNode.setAttribute(TAGDATE, dateVDJ.format(date));
		Element commentNode = dom.createElement(COMMENT);
        songNode.appendChild(commentNode);
		Text text = dom.createTextNode(comment);
        commentNode.appendChild(text);
        return songNode;
	}
}
