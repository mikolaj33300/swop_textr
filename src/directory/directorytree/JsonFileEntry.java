package directory.directorytree;

import java.util.List;

public class JsonFileEntry extends JsonEntry {

    public JsonFileEntry(String path, JsonDirectoryEntry parent) {
        super(path, parent);
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        return null;
    }

}
