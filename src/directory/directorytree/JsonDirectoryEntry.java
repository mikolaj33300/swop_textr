package directory.directorytree;

import files.FileBuffer;
import files.FileHolder;
import util.json.SimpleJsonProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonDirectoryEntry extends JsonEntry {

    /**
     * The list of entries of a {@link util.json.SimpleJsonObject}. This is used to initialize the children.
     */
    private final LinkedHashMap<String, SimpleJsonProperty> entries;

    /**
     * The constructor for a {@link JsonDirectoryEntry}
     * @param name the name of the entry itself.
     * @param bufferReference the path of the Json file on disk
     * @param properties the properties of the {@link util.json.SimpleJsonObject} from {@link util.json.SimpleJsonObject#properties}
     * @param parent the parent of this object, representing the {@link JsonDirectoryEntry}
     */
    public JsonDirectoryEntry(String name, String path, FileBuffer bufferReference, LinkedHashMap<String, SimpleJsonProperty> properties, JsonDirectoryEntry parent) {
        super(name, path, bufferReference, null, parent);
        this.entries = properties;
    }

    @Override
    public List<FileSystemEntry> initChildren() {
        ArrayList<FileSystemEntry> json = new ArrayList<>();

        for (Map.Entry<String, SimpleJsonProperty> entry : this.entries.entrySet()) {

            // If the selected file's SimpleJsonValue is not a SimpleJsonObject (children = null), we add a FileEntry
            if (entry.getValue().value.getChildren() == null)
                json.add(new JsonFileEntry(entry.getKey(), getPath(), getBuffer(), entry.getValue().value.getLocation(), this));
            else
                json.add(new JsonDirectoryEntry(entry.getKey(), getPath(), getBuffer(), entry.getValue().value.getChildren(), this));

        }

        json.forEach(FileSystemEntry::initChildren);
        this.setChildren(json);

        return json;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public FileHolder createFile(FileCreator creator) {
        return null;
    }

}