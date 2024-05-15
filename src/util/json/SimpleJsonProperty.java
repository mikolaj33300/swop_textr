package util.json;

public class SimpleJsonProperty {
    /**
     * Parent object
     */
    SimpleJsonObject object;
    String name;
    /**
     * A value, can be a {@link SimpleJsonObject} or {@link SimpleJsonString}
     */
    public SimpleJsonValue value;

    /**
     * Creates a new SimpleJsonProperty
     *
     * @param name  the name of the property
     * @param value the value of the property
     */
    SimpleJsonProperty(String name, SimpleJsonValue value) {
        this.name = name;
        this.value = value;
        value.property = this;
    }

}
