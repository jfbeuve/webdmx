package fr.beuve.vdj.comments;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import fr.beuve.vdj.VDJDatabase;

public class MusicStoreVDJ extends VDJDatabase{
	private boolean create;
	
	public MusicStoreVDJ(Properties props) throws ParseException, IOException{
		super(props);
		create = props.getProperty("SONG.CREATE").equalsIgnoreCase("true");
	}
	
	/**
	* update comment of the song if found
	 * @return true if found
	 */
	public boolean update(Song song) throws IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException{
		Node nodeSong = song(song);
		if(nodeSong==null&&song.comment==null){
			logger.debug("NOT FOUND "+song.name);
			return false; // no song
		}
		if(nodeSong==null&&song.comment!=null&&!create){
			logger.info("NOT FOUND "+song.name+" FOR "+song.comment);
			return false; // no song
		}
		if(nodeSong==null&&song.comment!=null&&create){
			logger.info("CREATE SONG "+song.name+" FOR "+song.comment);
			
			//create song

			Node newSong = new SongXml(song).node(xml);
			xml.getFirstChild().appendChild(newSong);
			
			changed = true;
			return false; // no song
		}
		
		Node nodeComment = comment(nodeSong);
		if(nodeComment==null&&song.comment!=null){
			//no comment
			changed = true;
			logger.info("*** "+song.name+" ***");
			logger.info("ADD "+song.comment+" TO "+song.name);

			Element commment = xml.createElement(SongXml.COMMENT);
			nodeSong.appendChild(commment);
            Text text = xml.createTextNode(song.comment);
            commment.appendChild(text);
		} else {
			if(nodeComment==null){
				logger.info("KEEP NO COMMENT FOR "+song.name);
				return true;
			}
			String old = nodeComment.getTextContent();
			if(old.equals(song.comment)) {
				logger.info("*** "+song.name+" ***");
				logger.info("KEEP "+old+" FOR "+song.name);
				return true;
			}
			changed = true;
			if(song.comment==null){
				logger.info("*** "+song.name+" ***");
				logger.info("REMOVE "+old+" FROM "+song.name);
				nodeSong.removeChild(nodeComment);
			} else {
				logger.info("*** "+song.name+" ***");
				logger.info("UPDATE "+old+" BY "+song.comment+" FOR "+song.name);
				Element commment = xml.createElement(SongXml.COMMENT);
				nodeSong.replaceChild(commment,nodeComment);
	            Text text = xml.createTextNode(song.comment);
	            commment.appendChild(text);
			}
		}
		return true;
	}
	/**
	 * @deprecated with Virtual Database 603
	 */
	private Node comment(Song song) throws XPathExpressionException{
		XPath xpath = XPathFactory.newInstance().newXPath();
	    XPathExpression expr = xpath.compile("//VirtualDJ_Database/"+SongXml.SONG+"[@"+SongXml.FILENAME+"=\""+song.name+"\"]/"+SongXml.COMMENT);
	    Object result = expr.evaluate(xml, XPathConstants.NODESET);
	    NodeList list = (NodeList) result;
	    if(list.getLength()==0) return null;
	    Node toReturn = list.item(0);
	    return toReturn;
	}
	
	private static Node comment(Node song){
		NodeList list = song.getChildNodes();
		for(int i=0;i<list.getLength();i++){
			Node item = list.item(i);
			if(item.getNodeName().equals(SongXml.COMMENT))
				return item;
		}
		return null;
	}

}
