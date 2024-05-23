package directory.directorytree;

import files.FileHolder;
import files.OpenFileOnPathRequestListener;
import util.json.TextLocation;
import files.FileBuffer;

import java.util.List;

public class JsonFileEntry extends JsonEntry {

    /**
     * This will initialize a file entry.
     * @param name name of this entry in the json structure
     * @param path the path, name, of the file
     * @param parent the parent of this file
     */
    public JsonFileEntry(String name, String path, TextLocation loc, JsonDirectoryEntry parent, OpenFileOnPathRequestListener listener) {
        super(name, path, loc, parent, listener);
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public FileSystemEntry selectEntry() {
        //TODO Notify listener
        return null;
    }

}
