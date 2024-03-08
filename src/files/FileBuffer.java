package files;

public class FileBuffer {

    /**
     * File reference
     */
    private FileHolder file;

    /**
     * Determines if buffer has been modified
     */
    private boolean dirty = false;

    /**
     * Holds the 'in memory' model from the file.
     */
    private String content;

    /**
     * Creates FileBuffer object with given path;
     */
    public FileBuffer(String path) {
        this.file = new FileHolder(path);
        this.content = this.file.getContent();
    }

    /**
     * Updates the content of the FileBuffer
     */
    public final void update(String updatedContents) {
        this.content = updatedContents;
        dirty = true;
    }

    /**
     * Saves the buffer contents to disk
     */
    public final void save() {
        if (!dirty) return;
        this.file.save(this.content);
        this.dirty = false;
    }

    /**
     * Clones this object
     *
     * @return
     */
    public FileBuffer
    clone() {
        FileBuffer copy = new FileBuffer(this.file.getPath());
        copy.dirty = this.dirty;
        copy.content = new String(this.content);
        return copy;
    }

    /**
     *
     */
    public final String
    getContent() {
        return this.file.getContent();
    }
}
