package directory.directorytree;

import directory.FileCreator;
import files.FileHolder;

import java.util.List;

public class JsonFileEntry extends JsonEntry {

    /**
     * This will initialize a file entry.
     * @param path the path, name, of the file
     * @param fullPath the full path to the json file on disk
     * @param parent the parent of this file
     */
    public JsonFileEntry(String path, String fullPath, JsonDirectoryEntry parent) {
        super(path, fullPath, parent);
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
