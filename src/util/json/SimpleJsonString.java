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
     * The value of this SimpleJsonString
     */
    String value;

    //TODO: Verander TextLocation door een coordinate zodat die geclonet kan worden en er geen representation exposure voorkomt
    /**
     * Creates a new SimpleJsonString
     *
     * @param start the start location of this SimpleJsonString
     * @param length the length of this SimpleJsonString
     * @param value the value of this SimpleJsonString
     */
    SimpleJsonString(TextLocation start, int length, String value) {
        this.start = start;
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
     * Returns the location of this SimpleJsonString, which is its start location
     *
     * @return start
     */
    @Override
    public TextLocation getLocation() {
        return this.start;
    }
}

