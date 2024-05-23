package directory.directorytree;

import files.FileHolder;
import files.OpenFileOnPathRequestListener;

import java.io.File;
import java.util.List;

public class FileEntry extends FileSystemEntry {

    public FileEntry(String path, DirEntry parent, OpenFileOnPathRequestListener listener) {
        super(path, parent, listener);
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        return null;
    }

    @Override
    public String getName() {
        return new File(getPath()).getName();
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public FileSystemEntry selectEntry() {
        //TODO notify listener
        return null;
    }

}
