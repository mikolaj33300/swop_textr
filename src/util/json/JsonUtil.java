package util.json;

import directory.directorytree.FileSystemEntry;
import directory.directorytree.JsonDirectoryEntry;
import directory.directorytree.JsonEntry;
import files.FileBuffer;

import java.util.List;

public class JsonUtil {

    /**
     * This will retrieve the TextLocation of a json entry path in a json file.
     * @param buffer the {@link FileBuffer} which holds the information
     * @param jsonName the target name in the json structure
     * @return a {@link TextLocation} object that specifies the location where the columns starts for the target path. Will return null if the path was not found or parsing failed
     */
    public static TextLocation getTextLocationFor(FileBuffer buffer, String jsonName) {
        JsonEntry entry = (JsonEntry) parseDirectory(buffer);
        return findLocationForKey(jsonName, entry.getEntries(), null);
    }

    /**
     * Returns a {@link JsonUtil} object when the parameter contents were correct json format.
     * @param buffer the location of the file
     * @return a {@link JsonUtil} object if correct json format, else null
     */
    public static FileSystemEntry parseDirectory(FileBuffer buffer) {
        TextLocation location = JsonUtil.getErrorLocation(new String(buffer.getBytes()));
        if(location == null) {
            SimpleJsonObject object = SimpleJsonParser.parseObject(new String(buffer.getBytes()));
            FileSystemEntry e = new JsonDirectoryEntry(
                    "root",
                    buffer.getPath(),
                    buffer,
                    object.properties,
                    null);
            e.initChildren();
            return e;
        }
        return null;
    }

    /**
     * Determines if the given string parameter has correct JSON structure
     * @return a textlocation determining the location of the error, or null if there were no errors
     */
    public static TextLocation getErrorLocation(String contents) {
        return SimpleJsonParser.getErrorLocation(contents);
    }

    /**
     * This will retrieve the TextLocation of a json entry path in a json file.
     * @param key the path to the json file
     * @param entries the target name in the json structure
     * @param foundLocation the location of the json file
     * @return a {@link TextLocation} object that specifies the location where the columns starts for the target path. Will return null if the path was not found or parsing failed
     */
    private static TextLocation findLocationForKey(String key, List<JsonEntry> entries, TextLocation foundLocation) {
        for(JsonEntry e : entries) {
            // Found!
            if(e.getName().equals(key)) foundLocation = e.getLocation();
            // Else keep searching
            foundLocation = findLocationForKey(key, e.getEntries(), foundLocation);
        }
        return foundLocation;
    }
}
