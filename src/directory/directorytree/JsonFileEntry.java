package directory.directorytree;

import files.FileHolder;
import util.json.TextLocation;
import files.FileBuffer;

import java.util.List;

public class JsonFileEntry extends JsonEntry {

    /**
     * This will initialize a file entry.
     * @param name name of this entry in the json structure
     * @param path the path, name, of the file
     * @param referencedBuffer the {@link FileBuffer} which holds the text that was used to create this tree
     * @param parent the parent of this file
     */
    public JsonFileEntry(String name, String path, FileBuffer referencedBuffer, TextLocation loc, JsonDirectoryEntry parent) {
        super(name, path, referencedBuffer, loc, parent);
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
    public FileHolder createFile(FileCreator creator) {
        return creator.createJson(this);
    }

}
