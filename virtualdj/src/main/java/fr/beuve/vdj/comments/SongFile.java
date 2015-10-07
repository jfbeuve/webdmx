package fr.beuve.vdj.comments;

import java.io.File;
import java.util.Date;


public class SongFile extends Song {
	private String folder;
	private File file;
	public SongFile(File _file, String root){
		file = _file;
		
		name = file.getName();
		
		String path = file.getPath();
		folder=path.substring(root.length(),path.length()-name.length());
		date = new Date(file.lastModified());
		size = file.length();
		
		fqname=path;
	}
	@Override
	public String toString() {
		return name + " " + folder;
	}
}
