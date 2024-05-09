package util.json;

import directory.directorytree.FileSystemEntry;
import directory.directorytree.JsonDirectoryEntry;
import directory.directorytree.JsonEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonUtil {

    /**
     * This will retrieve the {@link TextLocation} of a json entry path in a json file.
     * @param jsonFilePath the file path of the Json file
     * @param jsonPath the target path in the json structure
     * @return a {@link TextLocation} object that specifies the location where the " starts for the target path. Will return null if the path was not found or parsing failed
     */
    public static TextLocation getTextLocationFor(String jsonFilePath, String jsonPath) throws IOException {

    }

    /**
     * Returns a {@link JsonUtil} object when the parameter contents were correct json format.
     * @param path the location of the file
     * @param contents the contents of that file that should be parsed to Json
     * @return a {@link JsonUtil} object if correct json format, else null
     */
    public static FileSystemEntry parseDirectory(String contents, String path) {
        SimpleJsonObject object = SimpleJsonParser.parseObject(contents);
        TextLocation location = JsonUtil.getErrorLocation(contents);
        if(location == null) {
            FileSystemEntry e = new JsonDirectoryEntry(
                    "root",
                    path,
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

    private static TextLocation findLocationForKey(String key, List<JsonEntry> entries, TextLocation foundLocation) {

        for(JsonEntry e : entries) {

            // Found!
            if(e.getName().equals(key)) foundLocation = e.getLocation();

            // Else keep searching
            //List<JsonEntry> casted = (List<JsonEntry>) e.getEntries();
            //foundLocation = findLocationForKey(key, e.getEntries());

        }

        return foundLocation;

    }

}
