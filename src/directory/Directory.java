package directory;

import directory.directorytree.DirEntry;
import directory.directorytree.FileEntry;
import directory.directorytree.FileSystemEntry;
import files.OpenFileOnPathRequestListener;

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
        if(entry == null) this.focusedDirectory = new FileEntry(new File(".").getAbsolutePath(), null, null, null);
        else focusedDirectory = entry;
        entries = this.focusedDirectory.getChildren();
    }

    public Directory(OpenFileOnPathRequestListener listener) {

        this.focusedDirectory = new DirEntry(listener);
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
        //TODO: Remove isdirectory if possible, make this use selectentry and it either returning an entry or null
        // to determine the next course of action
        if(focused == 0) this.focusedDirectory = this.focusedDirectory.getParent();
        else if(focused <= this.entries.size()) {
            // Directory: we change the parent & update children
            if(entries.get(focused-1).isDirectory()) {
                this.focusedDirectory = entries.get(focused-1).selectEntry();
                this.entries = this.focusedDirectory.getChildren();
            }
            else
                return entries.get(focused - 1).selectEntry();
        }
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
        if(this.focused + 1 <= this.focusedDirectory.getChildren().size())
            this.focused++;
    }

    /**
     * Decreases the hover index {@link Directory#focused}
     */
    public void decreaseFocused() {
        if(this.focused - 1 >= 0)
            this.focused--;
    }

    /**
     * Returns the focused integer, representing a file in the {@link Directory#getEntries()} list.
     * @return an integer representing the focused {@link FileSystemEntry}
     */
    public int getFocused() {
        return this.focused;
    }

}

