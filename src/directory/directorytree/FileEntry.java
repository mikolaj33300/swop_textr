package directory.directorytree;

import java.util.List;

public class FileEntry extends FileSystemEntry {

    public FileEntry(DirEntry parent, String path) {
        super(path, parent);
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        return null;
    }

}
