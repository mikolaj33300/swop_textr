package util.json;

import java.util.LinkedHashMap;

public final class SimpleJsonObject extends SimpleJsonValue {

    /**
     * A LinkedHashMap of all the properties under this SimpleJsonObject
     */
    public LinkedHashMap<String, SimpleJsonProperty> properties;

    //TODO: kijk of LinkedHashMap gecloned mag worden om representation exposure te voorkomen
    /**
     * Creates a new SimpleJsonObject with the given properties
     *
     * @param properties the properties of this SimpleJsonObject
     */
    SimpleJsonObject(LinkedHashMap<String, SimpleJsonProperty> properties) {
        this.properties = properties;
        this.properties.values().forEach(p -> p.object = this);
    }

    //TODO: kijk of LinkedHashMap gecloned mag worden om representation exposure te voorkomen
    /**
     * Returns the properties of this SimpleJsonObject
     *
     * @return the LinkedHashMap of properties of this SimpleJsonObject
     */
    @Override
    public LinkedHashMap<String, SimpleJsonProperty> getChildren() {
        return this.properties;
    }

    /**
     * Returns the location of this SimpleJsonObject, since this is a SimeJsonObject, and not part of the text, the returned location will be null
     *
     * @return null
     */
    @Override
    public TextLocation getLocation() {
        return null;
    }
}
