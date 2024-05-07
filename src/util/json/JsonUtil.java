package util.json;

import directory.directorytree.FileSystemEntry;
import directory.directorytree.JsonDirectoryEntry;

public class JsonUtil {

    /**
     * Returns a {@link JsonUtil} object when the parameter contents were correct json format.
     * @param contents the contents that should be parsed to json
     * @return a {@link JsonUtil} object if correct json format, else null
     */
    public static FileSystemEntry getParser(String contents) {
        SimpleJsonObject object = SimpleJsonParser.parseObject(contents);
        TextLocation location = JsonUtil.getErrorLocation(contents);
        return location != null ? null :
                new JsonDirectoryEntry(object.property.name,
                object.properties,
                null);
    }

    /**
     * Determines if the given string parameter has correct JSON structure
     * @return a textlocation determining the location of the error, or null if there were no errors
     */
    public static TextLocation getErrorLocation(String contents) {
        return SimpleJsonParser.getErrorLocation(contents);
    }

}
