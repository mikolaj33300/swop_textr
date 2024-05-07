package directory.json;

import java.util.LinkedHashMap;

final class SimpleJsonObject extends SimpleJsonValue {
    LinkedHashMap<String, SimpleJsonProperty> properties;
    SimpleJsonObject(LinkedHashMap<String, SimpleJsonProperty> properties) {
        this.properties = properties;
        this.properties.values().forEach(p -> p.object = this);
    }

    @Override
    boolean isObject() {
        return true;
    }
}
