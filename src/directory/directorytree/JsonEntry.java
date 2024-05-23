package directory.directorytree;

import files.FileBuffer;
import files.OpenFileOnPathRequestListener;
import util.json.TextLocation;

import java.util.List;

public abstract class JsonEntry extends FileSystemEntry {

    /**
     * The path to the entry in the JSON structure. This is used to retrieve the {@link util.json.TextLocation} later.
     * We assume that all entry names are unique, so we don't keep track of where a certain name is located within the structure
     */
    private final String name;

    /**
     * The location of where the contents of the file are located. Only used within this generic class.
     */
    private final TextLocation location;


    /**
     * Constructor for a generic {@link JsonEntry}.
     * @param name the name of the entry in the json entry
     * @param path path of the json file on disk
     * @param parent the parent entry. This is always a {@link JsonDirectoryEntry}
     */
    public JsonEntry(String name, String path, TextLocation location, JsonDirectoryEntry parent, OpenFileOnPathRequestListener listener) {
        super(path, parent, listener);
        this.name = name;
        this.location = location;
    }

    /**
     * Returns the location of the contents of a certain key in the json structure
     * @return a {@link TextLocation} object which specifies the location of the columns " after the key (name) in the json file
     */
    public TextLocation getLocation() {
        return this.location;
    }

    /**
     * This method will return the reference to the file in the JSON structure
     * @return the string which determines the path of the entry in the JSON
     */
    @Override
    public String getName() {
        return name;
    }


    @Override
    public List<JsonEntry> getEntries() {
        return (List<JsonEntry>) super.getEntries();
    }


    /**
     * If a parent is null, then we are at the root of the Json file system
     * @return a {@link FileSystemEntry} or null incase we are at the root
     */
    @Override
    public FileSystemEntry getParent() {
        return this.parent;
    }

}