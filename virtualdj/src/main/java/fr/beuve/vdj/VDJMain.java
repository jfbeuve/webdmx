package fr.beuve.vdj;

import fr.beuve.vdj.comments.MusicStoreFile;
import fr.beuve.vdj.title.TitleCleaner;



public class VDJMain {

	public static void main(String[] args) throws Exception {
		System.out.println("Updating comments...");
		MusicStoreFile.main(args);
		System.out.println("Cleaning titles...");
		TitleCleaner.main(args);
		System.out.println("DONE!");
		//System.in.read();
		//System.out.println("[EXIT]");
	}

}
