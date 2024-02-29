import java.io.File;
import java.io.FileWriter;

class 
fileBuffer 
{
	boolean dirty;
	file file;
	String content;

	public void
	createFileBuffer (String path)
	{
		this.file = new file;
		this.content = this.file.createFile(path);
	}
	/*
	 * this needs to get a bit more complex since the content will probably be a single char that needs to be written to a single place
	 */
	public void
	update (String content)
	{
		this.dirty = true;
		this.content = content;
	}

	public void
	saveBuffer ()
	{
		this.file.save(this.content);
	}

	public void
	close () 
	{
		this.file.close();
	}
}

class 
file 
{
	private String path;
	private File fd;

	public String
	createFile (String path)
	{
		this.path = path;
		this.fd = new File(path);

		return fd.readString(path);
	}
	
	public void
	save (String: content)
	{
		FileWriter w = new FileWriter(this.fd);
		writer.write(content);
		w.close();
	}

	public void
	close ()
	{
		this.fd.close();
	}
}
