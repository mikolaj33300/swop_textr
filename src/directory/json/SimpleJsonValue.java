package directory.json;

sealed abstract class SimpleJsonValue permits SimpleJsonString, SimpleJsonObject {
    /**
     * The property for which this is the value, or null if this is a toplevel value.
     */
    SimpleJsonProperty property;

    abstract boolean isObject();
}
