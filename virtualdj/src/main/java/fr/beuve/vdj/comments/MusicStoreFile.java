package fr.beuve.vdj.comments;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FilenameUtils;


public class MusicStoreFile extends MusicStore {
	public MusicStoreFile(Properties props) throws Exception {
		super(props);
		REPO = props.getProperty("SONG.HOME");
		String _exclude = props.getProperty("SONG.EXCLUDE");
		if(_exclude==null){
			exclude = new String[0];
		} else {
			exclude = _exclude.split(";");
		}
		String _apply = props.getProperty("VDJ.UPDATE");
		apply = _apply!=null&&_apply.equalsIgnoreCase("true");
	}
	private static final long serialVersionUID = 1L;
	//private final String REPO = "F:\\PUBLIC\\WINDJ20110122\\";
	private String REPO;
	private String[] exclude;
	private boolean apply = false;

	public void load() throws XPathExpressionException, IOException, TransformerFactoryConfigurationError, TransformerException{
		load(new File(REPO));
	}
	private void load(File file) throws XPathExpressionException, IOException, TransformerFactoryConfigurationError, TransformerException{
		if(file.isFile()){
			//zic.add(new Music(file, REPO));
			Song zic = new SongFile(file, REPO);
			if(apply){
				treat(zic);
			}
		} else {
			if(!apply) {
				logger.info(file.toString());
			}
			for(File a : file.listFiles(new FileFilter() {
				
				public boolean accept(File file) {
					for(int i=0;i<exclude.length;i++){
						if(FilenameUtils.wildcardMatch(file.getName(),exclude[i])){
							logger.debug("EXCLUDE["+exclude[i]+ "]="+ file.getName());
							return false;
						}
					}
					if(file.isDirectory())return true;
					if(file.getName().endsWith(".mp3")) return true;
					if(file.getName().endsWith(".MP3")) return true;
					if(file.getName().endsWith(".WMA")) return true;
					if(file.getName().endsWith(".wma")) return true;
					if(file.getName().endsWith(".wav")) return true;
					if(file.getName().endsWith(".WAV")) return true;
					return false;
				}
			})){
				load(a);
			}
		}
	}
	public void save() throws IOException, TransformerFactoryConfigurationError, TransformerException{
		vdj.store();
		if(apply) logger.info(this.toString());
	}
	
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		InputStream is = MusicStoreFile.class.getResourceAsStream("/"+args[0]);
		try{
			props.load(is);
		}finally{
			is.close();
		}
		//props.load(new FileInputStream(args[0]));
		
		MusicStoreFile store = new MusicStoreFile(props);
		store.load();
		store.save();
	}
}
