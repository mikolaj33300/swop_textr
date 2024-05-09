package directory.directorytree;

import directory.FileCreator;
import files.FileHolder;

import java.io.File;
import java.util.List;

public class FileEntry extends FileSystemEntry {

    public FileEntry(String path, DirEntry parent) {
        super(path, parent);
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
    public FileHolder createFile(FileCreator creator) {
        return creator.createDefault(this);
    }

}
