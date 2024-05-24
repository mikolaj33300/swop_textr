package directory.directorytree;

import files.FileHolder;
import files.OpenFileOnPathRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirEntry extends FileSystemEntry {

    public DirEntry(String path, FileSystemEntry parent, OpenFileOnPathRequestListener listener, Runnable closeEventListener) {
        super(path, parent, listener, closeEventListener);
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        List<FileSystemEntry> entry = new ArrayList<>();

        for(File listedFile : new File(getPath()).listFiles()) {

            if(listedFile.isDirectory())
                entry.add(new DirEntry(listedFile.getAbsolutePath(),this, this.openOnPathListener, closeEventListener));
            else
                entry.add(new FileEntry(listedFile.getAbsolutePath(), this, this.openOnPathListener, closeEventListener));

        }

        return entry;
    }

    @Override
    public String getName() {
        return new File(this.getPath()).getName();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public FileSystemEntry selectEntry() {
        return this;
    }

}
