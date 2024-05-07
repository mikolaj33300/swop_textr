package directory.directorytree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirEntry extends FileSystemEntry {

    public DirEntry(FileSystemEntry parent, String path) {
        super(path, parent);
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        List<FileSystemEntry> entry = new ArrayList<>();

        for(File listedFile : new File(getPath()).listFiles()) {

            if(listedFile.isDirectory())
                entry.add(new DirEntry(this, listedFile.getPath()));
            else
                entry.add(new FileEntry(this, listedFile.getPath()));

        }

        return entry;
    }

}
