package directory.directorytree;

import directory.FileCreatorVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class FileSystemEntry implements FileCreatorVisitor {

    /**
     * The path of the file entry
     */
    private final String path;
    /**
     * The parent of this entry. This is always a {@link DirEntry}
     */
    protected final FileSystemEntry parent;
    /**
     * The list of children {@link FileSystemEntry}. This will be null for {@link FileEntry}
     */
    private List<FileSystemEntry> children = new ArrayList<>();

    /**
     * Constructor for a generic {@link FileSystemEntry}
     * @param path the full path to the file
     * @param parent the
     */
    public FileSystemEntry(String path, FileSystemEntry parent) {
        this.path = path;
        this.parent = parent;
    }

    /**
     * Allows instances of {@link FileSystemEntry} to set its own children.
     * This method is made because abstract implementations of {@link FileSystemEntry#initChildren()} depended on class
     * variables, but they weren't set because the constructor of {@link FileSystemEntry} called upon {@link FileSystemEntry#initChildren()}
     * before the class variables got its value. Responsibility now lies on the caller to initialize the children
     * @param children the children that should be set
     */
    protected void setChildren(List<FileSystemEntry> children) {
        this.children = children;
    }

    /**
     * Returns a list of names of all the children of the current entry
     * @return list of strings representing the entries in this directory
     */
    public List<String> getDirectChildrenNames() {
        return this.children.stream().map(FileSystemEntry::getName).toList();
    }

    /**
     * Maps all {@link FileSystemEntry}'s {@link FileSystemEntry#children} to their path
     * @return a list of {@link String} representing all entry paths
     */
    public List<String> getChildrenPaths() {
        return this.children.stream().map(FileSystemEntry::getPath).toList();
    }

    /**
     * Returns the parent entry
     * @return the parent {@link FileSystemEntry} of this entry
     */
    public FileSystemEntry getParent() {
        return this.parent;
    }

    /**
     * Returns the path of this entry
     * @return the string which represents the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * The method which will construct the directory tree.
     * @return a list of children.
     */
    public abstract List<FileSystemEntry> initChildren();

    /**
     * Gets the name of the entry using the {@link FileSystemEntry#path}.
     * @return A string value which represents the name of the entry
     */
    public abstract String getName();

    /**
     * Determines if the current entry is a directory
     * @return boolean determining if this entry is a directory.
     */
    public abstract boolean isDirectory();

    /**
     * Returns the list of children
     * @return a list of {@link FileSystemEntry}
     */
    public List<? extends FileSystemEntry> getEntries() {
        List<FileSystemEntry> entries = new ArrayList<>();
        entries.addAll(this.children);
        return entries;
    }

}
