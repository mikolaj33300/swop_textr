package directory.directorytree;

import directory.directorytree.DirEntry;

import java.util.ArrayList;
import java.nio.file.Files;
import java.io.File;

public class FileEntry extends DirEntry {
    private File f;

    public FileEntry(String p) {
        this.f = new File(p);
    }

    public String getParent() {
        return f.getParent();
    }

    public DirEntry[] listFiles() {
        ArrayList<FileEntry> res = new ArrayList<FileEntry>(10);
        for (final File file : f.listFiles())
            res.add(new FileEntry(file.getName()));
        return res.toArray(new DirEntry[res.size()]);
    }

    public boolean isHidden() {
        return f.isHidden();
    }

    public boolean isDirectory() {
        return f.isDirectory();
    }

    public String getName() {
        return f.isDirectory() ? f.getName() + "/" : f.getName();
    }


}
