package directory.directorytree;

import files.FileHolder;
import files.OpenFileOnPathRequestListener;

import java.io.File;
import java.util.List;

public class FileEntry extends FileSystemEntry {

    public FileEntry(String path, DirEntry parent, OpenFileOnPathRequestListener listener, Runnable closeEventListener) {
        super(path, parent, listener, closeEventListener);
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
        openOnPathListener.notifyRequestToOpenFile(this.getPath());
        return null;
    }

}
