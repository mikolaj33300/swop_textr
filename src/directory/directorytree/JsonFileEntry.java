package directory.directorytree;

import util.json.TextLocation;

import java.util.List;

public class JsonFileEntry extends JsonEntry {

    private final TextLocation location;

    public JsonFileEntry(String path, TextLocation location, JsonDirectoryEntry parent) {
        super(path, parent);
        this.location = location;
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        return null;
    }

}
