package fr.beuve.vdj.title;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.beuve.vdj.VDJDatabase;
import fr.beuve.vdj.comments.MusicStoreFile;

public class TitleCleaner extends VDJDatabase{
	int count=0;
	protected static Logger logger = Logger.getLogger(TitleCleaner.class);
	
	public TitleCleaner(Properties props) throws ParseException, IOException {
		super(props);
	}

	public static void main(String[] args) throws Exception{
		Properties props = new Properties();
		InputStream is = MusicStoreFile.class.getResourceAsStream("/"+args[0]);
		try{
			props.load(is);
		}finally{
			is.close();
		}
		
		TitleCleaner vdj = new TitleCleaner(props);
		vdj.load();
		int info = vdj.fix();
		logger.info(info+" SONG FIXED");	
		vdj.store();
	}
	private int fix() throws XPathExpressionException, DOMException, ParseException{
		NodeList list = songs();
		for(int i=0; i<list.getLength();i++){
			fix(list.item(i));
		}
		return count;
	}
	private void fix(Node node) throws DOMException, ParseException{
		VDJSong song = new VDJSong(node, repo);
		boolean fixed = song.fix();
		if(fixed) {
			count++;
			super.changed = true;
		}
	}
}
