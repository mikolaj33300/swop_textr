package files;

public class FileBuffer {
    private boolean dirty;
    private FileHolder file;
    private String content;

    public FileBuffer(String path) {
        this.file = new FileHolder(path);
        this.content = file.getContent();
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

    public void close() {
        this.file.close();
    }

    private void setContents(String contents) {
        this.content = contents;
    }

    public FileBuffer clone() {
        // Note: path is cloned. Let's clone it again to be sure of rEpReSenTaTioN eXpoSure
        String path = new String(this.file.getPath());
        // Clone of clone of clone to avoid rEpreSenTAtiOn ExpOSure
        FileBuffer copy = new FileBuffer(new String(path));

        // Hier zitten we met een probleem, want contents wordt niet gekopieerd. We kunnen een
        // PRIVATE methode aanmaken voor rEpREsEntATion ExpOsuRE te vermijden.
        copy.setContents(this.content);

        return copy;

    }

}
