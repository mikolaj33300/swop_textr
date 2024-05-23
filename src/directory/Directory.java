package directory;

import directory.directorytree.FileEntry;
import directory.directorytree.FileSystemEntry;

import java.io.File;
import java.util.List;

public class Directory {

    /**
     * The current directory.
     */
    private FileSystemEntry focusedDirectory;

    /**
     * The selected entry in the list of {@link FileSystemEntry}
     */
    private int focused = 0;

    /**
     * The list of {@link FileSystemEntry} the current focused directory has
     */
    private List<FileSystemEntry> entries;

    /**
     * Creates a directory object
     * @param entry if entry is null, we use the root of the current jar
     */
    public Directory(FileSystemEntry entry) {
        //TODO: Attach listener that opens the requested real files and sends requests up
        if(entry == null) this.focusedDirectory = new FileEntry(new File(".").getAbsolutePath(), null, null);
        else focusedDirectory = entry;
    }

    /**
     * Returns the list of entries in the current focused directory
     * @return list of {@link FileSystemEntry} representing the children of {@link Directory#focusedDirectory}
     */
    public List<FileSystemEntry> getEntries() {
        return this.focusedDirectory.getChildren();
    }

    /**
     * Returns the selected {@link FileSystemEntry}.
     */
    public FileSystemEntry selectEntry() {
        if(!this.getEntries().get(focused).isDirectory())
            return this.getEntries().get(focused);
        return null;
    }

    /**
     * Changes {@link Directory#focusedDirectory} to the parent of
     */
    public void moveToParent() {
        this.focusedDirectory = this.focusedDirectory.getParent();
    }

    /**
     * Increases the hover index {@link Directory#focused}
     */
    public void increaseFocused() {
        this.focused = this.focused + 1 >= focusedDirectory.getChildren().size() ? this.focused : this.focused++;
    }

    /**
     * Decreases the hover index {@link Directory#focused}
     */
    public void decreaseFocused() {
        this.focused = this.focused - 1 <= 0 ? this.focused : this.focused--;
    }

    /**
     * Returns the focused integer, representing a file in the {@link Directory#getEntries()} list.
     * @return an integer representing the focused {@link FileSystemEntry}
     */
    public int getFocused() {
        return this.focused;
    }

}

