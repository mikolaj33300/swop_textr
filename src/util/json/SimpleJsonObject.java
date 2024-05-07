package util.json;

import java.util.LinkedHashMap;

public final class SimpleJsonObject extends SimpleJsonValue {

    public LinkedHashMap<String, SimpleJsonProperty> properties;

    SimpleJsonObject(LinkedHashMap<String, SimpleJsonProperty> properties) {
        this.properties = properties;
        this.properties.values().forEach(p -> p.object = this);
    }

    @Override
    public LinkedHashMap<String, SimpleJsonProperty> getChildren() {
        return this.properties;
    }

    @Override
    public TextLocation getLocation() {
        return null;
    }
}
