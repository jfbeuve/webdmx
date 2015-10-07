package fr.beuve.vdj;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.beuve.vdj.comments.Song;
import fr.beuve.vdj.comments.SongXml;

public abstract class VDJDatabase {
	private static final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	protected File file;
	protected Document xml;
	protected boolean changed=false;
	protected Logger logger = Logger.getLogger(VDJDatabase.class);
	protected int version; // VirtualDJ_Database Version
	protected String repo;
	//private static int[] versions = {1,603};
	
	public VDJDatabase(Properties props) throws ParseException, IOException{
		file = new File(props.getProperty("VDJ.DB"));
		repo = props.getProperty("SONG.HOME");
	}
	
	public void load() throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		//svn checkout http://vdjedit.googlecode.com/svn/trunk/ vdjedit-read-only 
		//InputStream inputStream= new FileInputStream(file);
		//Reader reader = new InputStreamReader(inputStream,"UTF-8");		 
		Reader reader = new FileReader(file);
		InputSource is = new InputSource(reader);
		//is.setEncoding("UTF-8");
		
		xml = db.parse(is);
		
		version=version();
		
		//if(Arrays.binarySearch(versions, version)<0) throw new RuntimeException("Unsupported database version "+version);
		
		logger.info("VirtualDJ_Database Version "+version);
	}
	protected int version() {
		String stringVersion = xml.getFirstChild().getAttributes().getNamedItem("Version").getNodeValue();
	    return Integer.valueOf(stringVersion).intValue();
	}
	
	private void copy() throws IOException{
		String path = file.getPath();
		int slash = path.lastIndexOf(File.separator);
		if(slash>-1)path = path.substring(0,slash);
		String name = file.getName();
		int dot = name.lastIndexOf(".");
		name = name.substring(0,dot+1);
		name += df.format(new Date());
		name += ".xml";
		File copy = new File(path+File.separator+name);
		FileUtils.copyFile(file, copy);
		logger.info("COPY "+file+" TO "+copy);
	}
	private void save() throws TransformerFactoryConfigurationError, TransformerException, IOException{
        logger.info("SAVE "+file);
		Source source = new DOMSource(xml);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        Result result = new StreamResult(new FileWriter(file));
        xformer.transform(source, result);
	}
	public void store() throws IOException, TransformerFactoryConfigurationError, TransformerException{
		if(changed){
			copy();
			save();
		}
	}
	/**
	 * Search for a given song not in Virtual DJ Database
	 */
	public Node song(Song song) throws XPathExpressionException{
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = null;
		String exprStr;

		if(version==1){ 
			//VirtualDJ_Database Version="1"
			exprStr = "//VirtualDJ_Database/"+SongXml.SONG+"[@"+SongXml.FILENAME+"=\""+song.name+"\"]";
			
		}else{
			// VirtualDJ_Database Version="603"
			exprStr = "//VirtualDJ_Database/"+SongXml.SONG+"[@"+SongXml.FILEPATH+"=\""+song.fqname+"\"]";
		}
		logger.debug("XPATH "+exprStr);
		expr = xpath.compile(exprStr);
		
		Object result = expr.evaluate(xml, XPathConstants.NODESET);
	    NodeList list = (NodeList) result;
	    if(list.getLength()==0) return null;
	    Node toReturn = list.item(0);
	    return toReturn;
	}
	
	public NodeList songs() throws XPathExpressionException{
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("//VirtualDJ_Database/"+SongXml.SONG);
	    
		Object result = expr.evaluate(xml, XPathConstants.NODESET);
	    NodeList list = (NodeList) result;
	    
	    return list;
	}
}
