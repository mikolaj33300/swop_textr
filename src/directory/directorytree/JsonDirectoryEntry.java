package directory.directorytree;

import util.json.SimpleJsonProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonDirectoryEntry extends JsonEntry {

    private final LinkedHashMap<String, SimpleJsonProperty> entries;

    public JsonDirectoryEntry(String name, LinkedHashMap<String, SimpleJsonProperty> properties, JsonDirectoryEntry parent) {
        super(name, parent);
        this.entries = properties;
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        ArrayList<FileSystemEntry> json = new ArrayList<>();

        for (Map.Entry<String, SimpleJsonProperty> entry : this.entries.entrySet()) {

            if (entry.getValue().value.getChildren() == null)
                json.add(new JsonFileEntry(entry.getKey(), this));
            else
                json.add(new JsonDirectoryEntry(entry.getKey(), entry.getValue().value.getChildren(), this));

        }

        return json;
    }

}
