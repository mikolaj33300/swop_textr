package directory.json;

class SimpleJsonProperty {
    /**
     * Parent object
     */
    SimpleJsonObject object;
    String name;
    /**
     * A value, can be a {@link SimpleJsonObject} or {@link SimpleJsonString}
     */
    SimpleJsonValue value;
    SimpleJsonProperty(String name, SimpleJsonValue value) {
        this.name = name;
        this.value = value;
        value.property = this;
    }
}
