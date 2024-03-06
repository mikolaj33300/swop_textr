package files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

public class FileHolder {
	private String path;
	/**
	 * Holds base IO java File object
	 */
	private File file;

	// Nog defensief afwerken zodat File niet een ongeldig pad heeft?
	public FileHolder(String path) {
		this.path = path;
		this.file = new File(path);
	}

	/* CreateFile kan niet aangeroepen worden tenzij File al bestaat...
	   lijkt onnodig...?
	public String createFile(String path) {
		this.path = path;
		this.fd = new File(path);

		return fd.readString(path);
	}*/
	
	public void save(String content) {
		try {
			FileWriter writer = new FileWriter(this.file);
			writer.write(content);
			writer.close();
		} catch(IOException e) {
			System.out.println("[files.FileHolder] Exception while saving file");
		}
	}

	public void close() {

	}

	public String getContent(String path) {
		try {
			FileReader reader = new FileReader(this.file);

		} catch(IOException e) {

		}
		return "";
	}

}
