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
     * Initializes {@link FileHolder} object and retrieves its {@link FileHolder#getContent()}
     */
    public FileBuffer(String path, String lineSeparator) {
        this.file = new FileHolder(path, lineSeparator);
        this.content = this.file.getContent();
    }

    // Implementation

    /**
     * Updates the content of the FileBuffer
     */
    public void write(String updatedContents) {
        this.content += updatedContents;
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

    // Test Methods

    /**
     * Returns the FileHolder object
     */
    FileHolder getFileHolder() {
        return this.file;
    }

    /**
     * Returns copy of this buffers' content.
     */
    String getContent() {
        return new String(this.content);
    }

    /**
     * Determines if the buffer is empty
     */
    boolean isDirty() {
        return this.dirty;
    }

    // Base methods

    /**
     * Clones this object
     */
    public FileBuffer clone() {
        FileBuffer copy = new FileBuffer(this.file.getPath(), this.file.getLineSeparator());
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
