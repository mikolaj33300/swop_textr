package files;

public class FileBuffer {
    private boolean dirty;
    private FileHolder file;
    private String content;

    public void createFileBuffer(String path) {
        this.file = new FileHolder(path);
        this.content = this.file.getContent(path);
    }

    /*
     * this needs to get a bit more complex since the content will probably be a single char that needs to be written to a single place
     */
    public void update(String content) {
        this.dirty = true;
        this.content = content;
    }

    public void saveBuffer()
    {
        this.file.save(this.content);
    }

    public void close()
    {
        this.file.close();
    }
}
