package fr.beuve.vdj;

import java.io.IOException;
import java.text.ParseException;

import fr.beuve.vdj.comments.M3uManager;

public class M3uSerializer {

	public static void main(String[] args) throws IOException, ParseException {
		M3uManager m3u = new M3uManager("m3u");
		m3u.load();
		m3u.write();
	}

}
