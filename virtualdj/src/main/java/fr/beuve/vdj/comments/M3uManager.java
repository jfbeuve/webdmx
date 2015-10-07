package fr.beuve.vdj.comments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

public class M3uManager implements Serializable {
	private static final long serialVersionUID = 1L;
	private String REPO;
	private List<M3u> playlists;
	private Date latest;
	private Date older;
	private NumberFormat nf = new DecimalFormat("00");

	public M3uManager(Properties props) {
		this(props.getProperty("VDJ.M3U"));
	}

	public M3uManager(String _REPO) {
		REPO = _REPO;

		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, -4);
		older = cal.getTime();
		
		playlists = new ArrayList<M3u>();
	}

	public void load() throws ParseException, IOException {
		File repo = new File(REPO);
		for (File m3u : repo.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.endsWith(".m3u"))
					return true;
				return false;
			}
		})) {
			load(m3u);
		}
	}

	private void load(File m3u) throws ParseException, IOException {
		M3u mylist = new M3u(m3u);
		playlists.add(mylist);
		if (latest == null || mylist.getDate().after(latest)) {
			latest = mylist.getDate();
		}
	}

	public boolean isNew(Song zic) {
		// return zic.getDate().after(latest);
		return zic.getDate().after(older);
	}

	/**
	 * @return null if not hit, else return hit tag
	 */
	public String hit(String filename) {
		int count = 0;
		for (M3u m3u : playlists) {
			if (m3u.contains(filename))
				count++;
		}
		if (count == 0)
			return null;
		return "HIT " + nf.format(count);
	}
	/**
	 * Deserialize object from classpath
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static M3uManager read() throws IOException, ClassNotFoundException {
		InputStream is = M3uManager.class.getResourceAsStream("/fr/beuve/vdj/M3uManager.ser");
		ObjectInputStream ois = new ObjectInputStream(is);
		try {
			return (M3uManager) ois.readObject();
		} finally {
			try {
				ois.close();
			} finally {
				is.close();
			}
		}
	}

	/**
	 * Serialize object in classpath
	 */
	public void write() throws IOException {
		FileOutputStream fos = new FileOutputStream("src/main/java/fr/beuve/vdj/M3uManager.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		try {
			oos.writeObject(this);
			oos.flush();
		} finally {
			try {
				oos.close();
			} finally {
				fos.close();
			}
		}
	}
}
