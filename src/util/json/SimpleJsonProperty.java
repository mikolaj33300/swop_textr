package util.json;

public class SimpleJsonProperty {
    /**
     * Parent object
     */
    SimpleJsonObject object;
    String name;
    /**
     * A value that can either refer to a String or new object
     */
    public SimpleJsonValue value;

    //TODO: check if SimpleJsonValue should be cloned to prevent representation exposure
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
