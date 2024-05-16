package util.json;

import java.util.LinkedHashMap;

public sealed abstract class SimpleJsonValue permits SimpleJsonString, SimpleJsonObject {

    /**
     * The parent property of this SimpleJsonValue
     */
    SimpleJsonProperty property;

    /**
     * Returns the children of this SimpleJsonValue
     *
     * @return a copy pf the children of this SimpleJsonValue
     */
    public abstract LinkedHashMap<String, SimpleJsonProperty> getChildren();

    /**
     * Returns the location of this SimpleJsonValue
     *
     * @return a copy of the location of this SimpleJsonValue
     */
    public abstract TextLocation getLocation();
}
