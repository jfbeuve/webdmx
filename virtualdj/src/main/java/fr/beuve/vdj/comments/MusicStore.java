package fr.beuve.vdj.comments;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;


public abstract class MusicStore {
	private M3uManager m3u;
	protected MusicStoreVDJ vdj;
	protected int nbhit = 0, nbnew=0, nblegacy=0, notfound=0, create=0;
	
	public MusicStore(Properties props) throws ParseException, IOException, ParserConfigurationException, SAXException, ClassNotFoundException{
		//m3u = new M3uManager(props);
		m3u = M3uManager.read();
		vdj = new MusicStoreVDJ(props);
		//m3u.load();
		vdj.load();
	}
	protected static final Logger logger = Logger.getLogger(MusicStore.class);
	protected void treat(Song zic) throws XPathExpressionException, IOException, TransformerFactoryConfigurationError, TransformerException{
		//logger.debug("*** "+zic.name+" ***");
		
		String hit = m3u.hit(zic.getName());
		if(hit!=null){
			nbhit++;
			zic.comment = hit;
			if(!vdj.update(zic)){
				//System.out.println("CREATE "+zic.name);
				create++;
			}
		}else if(m3u.isNew(zic)){
			nbnew++;
			zic.comment = zic.tagNew();
			if(!vdj.update(zic)){
				//System.out.println("CREATE "+zic.name);
				create++;
			}
		}else{
			nblegacy++;
			zic.comment = null;
			if(!vdj.update(zic))notfound++;
		}
	}
	@Override
	public String toString() {
		return nbhit+" HITS, "+nbnew+ " NEW, "+nblegacy+" LEGACY, "+create+" CREATE, "+notfound+" NOT FOUND";
	}
}
