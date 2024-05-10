package util.json;

import java.util.LinkedHashMap;

public sealed abstract class SimpleJsonValue permits SimpleJsonString, SimpleJsonObject {
    /**
     * parent
     */
    SimpleJsonProperty property;

    public abstract LinkedHashMap<String, SimpleJsonProperty> getChildren();

    public abstract TextLocation getLocation();

}
