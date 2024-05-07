package util.json;

public class SimpleJsonGenerator {

    private StringBuilder builder = new StringBuilder();
    private int objectNestingDepth;
    private String lineSeparator;

    SimpleJsonGenerator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    private void generateLineBreak() {
        builder.append(lineSeparator);
        int n = 2 * objectNestingDepth;
        for (int i = 0; i < n; i++)
            builder.append(' ');
    }

    private void generate(SimpleJsonValue value) {
        switch (value) {
            case SimpleJsonString s -> generate(s);
            case SimpleJsonObject o -> generate(o);
        }
    }

    private void generate(String text) {
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

    private void generate(SimpleJsonString string) {
        generate(string.value);
    }

    private void generate(SimpleJsonObject object) {
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

    static String generate(String lineSeparator, SimpleJsonValue value) {
        SimpleJsonGenerator generator = new SimpleJsonGenerator(lineSeparator);
        generator.generate(value);
        return generator.builder.toString();
    }

    static String generateStringLiteral(String content) {
        SimpleJsonGenerator generator = new SimpleJsonGenerator(null);
        generator.generate(content);
        return generator.builder.toString();
    }
}
