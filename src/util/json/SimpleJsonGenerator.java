package util.json;

public class SimpleJsonGenerator {

    /**
     * The builder contains the generated JSON
     */
    StringBuilder builder = new StringBuilder();

    /**
     * The objectNestingDepth is used to determine the indentation of the JSON
     */
    int objectNestingDepth;

    /**
     * The line separator to use
     */
    String lineSeparator;

    /**
     * Creates a new SimpleJsonGenerator
     *
     * @param lineSeparator the line separator to use
     */
    SimpleJsonGenerator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    /**
     * Generates linebreaks in the builder based on its objectNestedDepth
     */
    void generateLineBreak() {
        builder.append(lineSeparator);
        int n = 2 * objectNestingDepth;
        for (int i = 0; i < n; i++)
            builder.append(' ');
    }

    //TODO: remove instanceof from prof code
    /**
     * Generates a JSON value from a SimpleJsonValue
     *
     * @param value the value to convert to a JSON value
     */
    void generate(SimpleJsonValue value) {
        if(value instanceof SimpleJsonString simpleJsonString)
            generate(simpleJsonString);
        else if(value instanceof SimpleJsonObject simpleJsonObject)
            generate(simpleJsonObject);
    }

    /**
     * Generates a JSON string from a string
     *
     * @param text the string to convert to a JSON string
     */
    void generate(String text) {
        builder.append('"');
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\r' -> builder.append("\\r");
                case '\n' -> builder.append("\\n");
                case '"' -> builder.append("\\\"");
                case '\\' -> builder.append("\\\\");
                default -> {
                    if (32 <= c && c <= 126)
                        builder.append(c);
                    else
                        throw new RuntimeException("Invalid character in string literal at offset " + i);
                }
            }
        }
        builder.append('"');
    }

    /**
     * Generates a JSON string from a SimpleJsonString
     *
     * @param string the string to convert to a JSON string
     */
    void generate(SimpleJsonString string) {
        generate(string.value);
    }

    /**
     * Generates a JSON object from a SimpleJsonObject
     *
     * @param object the object to convert to a JSON object
     */
    void generate(SimpleJsonObject object) {
        builder.append('{');
        objectNestingDepth++;
        generateLineBreak();
        int i = 0;
        for (SimpleJsonProperty property : object.properties.values()) {
            generate(property.name);
            builder.append(": ");
            generate(property.value);
            i++;
            if (i < object.properties.size()) {
                builder.append(',');
                generateLineBreak();
            }
        }
        objectNestingDepth--;
        generateLineBreak();
        builder.append('}');
    }

    /**
     * Generates a JSON string from a SimpleJsonValue
     *
     * @param lineSeparator the line separator to use
     * @param value the value to convert to a JSON string
     * @return the generated JSON string
     */
    static String generate(String lineSeparator, SimpleJsonValue value) {
        SimpleJsonGenerator generator = new SimpleJsonGenerator(lineSeparator);
        generator.generate(value);
        return generator.builder.toString();
    }

    /**
     * Generates a JSON string literal from a string
     *
     * @param content the string to be converted to a JSON string literal
     * @return the JSON string literal
     */
    static String generateStringLiteral(String content) {
        SimpleJsonGenerator generator = new SimpleJsonGenerator(null);
        generator.generate(content);
        return generator.builder.toString();
    }
}
