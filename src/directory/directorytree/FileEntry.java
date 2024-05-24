package directory.directorytree;

import files.FileHolder;
import files.OpenFileOnPathRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileEntry extends FileSystemEntry {

    public FileEntry(String path, DirEntry parent, OpenFileOnPathRequestListener listener, ArrayList<Runnable> closeEventListeners) {
        super(path, parent, listener, closeEventListeners);
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
        List<Runnable> closeEventListenersCopy = List.copyOf(closeEventListeners);
        for(Runnable l : closeEventListenersCopy){
            l.run();
        }
        return null;
    }

}
