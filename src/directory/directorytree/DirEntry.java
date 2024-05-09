package directory.directorytree;

import files.FileHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirEntry extends FileSystemEntry {

    public DirEntry(String path, FileSystemEntry parent) {
        super(path, parent);
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        List<FileSystemEntry> entry = new ArrayList<>();

        for(File listedFile : new File(getPath()).listFiles()) {

            if(listedFile.isDirectory())
                entry.add(new DirEntry(listedFile.getPath(),this));
            else
                entry.add(new FileEntry(listedFile.getPath(), this));

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
    public FileHolder createFile(FileCreator creator) {
        return null;
    }

}
