package util.json;

import java.util.LinkedHashMap;

public final class SimpleJsonString extends SimpleJsonValue {
    TextLocation start;
    int length;
    String value;

    SimpleJsonString(TextLocation start, int length, String value) {
        this.start = start;
        this.length = length;
        this.value = value;
    }

    @Override
    public LinkedHashMap<String, SimpleJsonProperty> getChildren() {
        return null;
    }

    @Override
    public TextLocation getLocation() {
        return this.start;
    }
}

