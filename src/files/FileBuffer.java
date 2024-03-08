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
     * Returns copy of this buffers' content.
     */
    public String getContent() {
        return new String(this.file.getContent());
    }

    /**
     * Clones this object
     */
    public FileBuffer clone() {
        FileBuffer copy = new FileBuffer(this.file.getPath());
        copy.dirty = this.dirty;
        copy.content = new String(this.content);
        return copy;
    }

    /**
     * Checks if the given {@link FileBuffer} references the same {@link FileHolder}
     * and temporarily, if the content, and dirty boolean match.
     */
    public boolean equals(FileBuffer buffer) {
        return this.dirty == buffer.dirty && this.content.equals(buffer.content) && this.file.getPath().equals(buffer.file.getPath());
    }

}
