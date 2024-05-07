package directory.directorytree;

public abstract class DirEntry {
    abstract public String getParent();

    abstract public DirEntry[] listFiles();

    abstract public boolean isHidden();

    abstract public boolean isDirectory();

    abstract public String getName();
}