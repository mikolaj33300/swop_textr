package util.json;

import directory.directorytree.DirEntry;

public class JsonUtil {

    private final String contents;
    private final SimpleJsonObject parsedFileBuffer;

    JsonUtil(String contentsBuffer) {
        this.contents = contentsBuffer;
        this.parsedFileBuffer = SimpleJsonParser.parseSimpleJsonObject(contents);
    }

    /**
     * Returns a {@link JsonUtil} object when the parameter contents were correct json format.
     * @param contents the contents that should be parsed to json
     * @return a {@link JsonUtil} object if correct json format, else null
     */
    static DirEntry getParser(String contents) {
        JsonUtil util = new JsonUtil(contents);
        if(util.parsesCorrectly()) return util.buildJsonTree();
        return null;
    }

    /**
     * Determines if the given string parameter has correct JSON structure
     * @return a boolean determining if the contents were correct json
     */
    private boolean parsesCorrectly() {
        try {
            SimpleJsonObject parsed = SimpleJsonParser.parseSimpleJsonObject(contents);
            } catch(SimpleJsonParserException exception) {
            return false;
        }
        return true;
    }


    private DirEntry buildJsonTree() {
        //return new JsonDirectoryEntry(parsedFileBuffer.property.name, parsedFileBuffer.properties, null);
        return null;
    }




}
