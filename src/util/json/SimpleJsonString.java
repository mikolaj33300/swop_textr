package util.json;

import java.util.LinkedHashMap;

public final class SimpleJsonString extends SimpleJsonValue {

    /**
     * The start location of this SimpleJsonString
     */
    TextLocation start;

    /**
     * The length of this SimpleJsonString
     */
    int length;

    /**
     * The String value of this SimpleJsonString
     */
    String value;

    /**
     * Creates a new SimpleJsonString, which loses the reference to the given TextLocation
     *
     * @param start the start location of this SimpleJsonString
     * @param length the length of this SimpleJsonString
     * @param value the value of this SimpleJsonString
     */
    SimpleJsonString(TextLocation start, int length, String value) {
        this.start = start.clone();
        this.length = length;
        this.value = value;
    }

    /**
     * Returns the children of this SimpleJsonString, since this is the end of the JSON structure, it has no children and whill return null
     *
     * @return null
     */
    @Override
    public LinkedHashMap<String, SimpleJsonProperty> getChildren() {
        return null;
    }

    /**
     * Returns a copy pf the location of this SimpleJsonString, which is its start location
     *
     * @return a copy of start
     */
    @Override
    public TextLocation getLocation() {
        return this.start.clone();
    }
}

