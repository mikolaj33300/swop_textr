package directory.directorytree;

import java.util.ArrayList;
import java.util.List;

public abstract class FileSystemEntry {

    private final String path;
    private final FileSystemEntry parent;
    private List<FileSystemEntry> children = new ArrayList<>();

    public FileSystemEntry(String path, FileSystemEntry parent) {
        this.path = path;
        this.parent = parent;
        this.children = initChildren();
    }

    /**
     * Maps all {@link FileSystemEntry}'s {@link FileSystemEntry#children} to their path
     * @return a list of {@link String} representing all entry paths
     */
    public List<String> getChildren() {
        return this.children.stream().map(child -> child.path).toList();
    }

    /**
     * Returns the parent's string format
     * @return path of the parent
     */
    public String getParent() {
        return this.parent.path;
    }

    /**
     * Returns the path of this entry
     *
     * @return the string which represents the path
     */
    public String getPath() {
        return new String(this.path);
    }

    public abstract List<FileSystemEntry> initChildren();

}
