package util.json;

import java.util.LinkedHashMap;

public class SimpleJsonParser {

    /**
     * The text to parse
     */
    String text;

    /**
     * Zero-based char offset
     */
    int offset;

    /**
     * Zero-based line number
     */
    int line;

    /**
     * Zero-based column number
     */
    int column;

    /**
     * The object nesting depth
     */
    int objectNestingDepth;

    /**
     * Creates a new SimpleJsonParser
     *
     * @param text the text to parse
     */
    SimpleJsonParser(String text) {
        this.text = text;
    }

    /**
     * Returns the current location in the text.
     *
     * @return a copy of the current TextLocation
     */
    TextLocation location() { return new TextLocation(line, column); }

    /**
     * Returns the character at the current location, or -1 if the current location is at the end of the text.
     *
     * @return an integer representing the character at the current location or -1 if the current location is at the end of the text
     */
    int peek() {
        if (offset == text.length())
            return -1;
        return text.charAt(offset);
    }

    /**
     * Reviews the next char in the text and handles it. The correct indentation will be added and the TextLocation will be updated.
     */
    void eat() {
        int c = text.charAt(offset);
        if (c == '\r') {
            // We assume the text has either CRLF or LF line separators.
            offset += 2;
            line++;
            column = 0;
        } else if (c == '\n') {
            offset++;
            line++;
            column = 0;
        } else {
            offset++;
            column++;
        }
    }

    /**
     * Takes an integer as argument and turns it into a readable String.
     * The default case, this means an integer will be interpreted as a char
     * Some characters will be interpreted in a special way:
     *  -1 = "end of text", \r = "carriage return", \n = "newline"
     *
     * @param c The character to convert to a string
     * @return the integer interpreted as a String: the char it represents or a special String for special integers
     */
    String charToString(int c) {
        return switch (c) {
            case -1 -> "end of text";
            case '\r' -> "carriage return";
            case '\n' -> "newline";
            default -> "'" + (char)c + "'";
        };
    }

    /**
     * Checks if the given integer is the same as the next char in the text. If it is not, a SimpleJsonParserException will be thrown.
     *
     * @param c the character to expect
     */
    void expect(int c) {
        if (peek() != c)
            throw new SimpleJsonParserException(location(), "Expected " + charToString(c) + " but found " + charToString(peek()));
        eat();
    }

    /**
     * Checks if the next char in the text is a line separator. If the check succeeds, the character will be handled and the TextLocation will be updated.
     */
    void expectLineSeparator() {
        if (peek() == '\r') {
            eat();
            expect('\n');
        } else
            expect('\n');
    }

    /**
     * Checks if the next char in the text is a space. If the check succeeds, the character will be handled and the TextLocation will be updated.
     */
    void expectIndentation() {
        int n = 2 * objectNestingDepth;
        for (int i = 0; i < n; i++)
            expect(' ');
    }

    /**
     * Checks the next char in the text. It first checks for a line separator, then for indentation. The checked chars will be handled and the TextLocation will be updated.
     */
    void expectLineBreak() {
        expectLineSeparator();
        expectIndentation();
    }

    /**
     * Returns the generated SimpleJsonString starting at the current location and advances the current location.
     *
     * @return the parsed SimpleJsonString
     */
    SimpleJsonString parseSimpleJsonString() {
        TextLocation start = location();
        expect('"');
        StringBuilder builder = new StringBuilder();
        for (;;) {
            int c = peek();
            switch (c) {
                case '"' -> {
                    eat();
                    return new SimpleJsonString(start, column - start.column(), builder.toString());
                }
                case '\\' -> {
                    eat();
                    switch (peek()) {
                        case '\\' -> {
                            eat();
                            builder.append('\\');
                        }
                        case '"' -> {
                            eat();
                            builder.append('"');
                        }
                        case 'r' -> {
                            eat();
                            builder.append('\r');
                        }
                        case 'n' -> {
                            eat();
                            builder.append('\n');
                        }
                        default ->
                                throw new SimpleJsonParserException(location(), "Invalid escape sequence");
                    }
                }
                default -> {
                    if (32 <= c && c <= 126) {
                        eat();
                        builder.append((char)c);
                    } else
                        throw new SimpleJsonParserException(location(), "Invalid character in string value");
                }
            }
        }
    }

    /**
     * Returns the simple JSON value starting at the current location and advances the current location.
     *
     * @return the parsed simple SimpleJsonValue
     */
    SimpleJsonValue parseSimpleJsonValue() {
        return switch (peek()) {
            case '"' -> parseSimpleJsonString();
            case '{' -> parseSimpleJsonObject();
            default -> throw new SimpleJsonParserException(location(), "Value expected");
        };
    }

    /**
     * Returns the simple JSON object starting at the current location and advances the current location.
     *
     * @return the parsed simple SimpleJsonValue
     */
    SimpleJsonObject parseSimpleJsonObject() {
        expect('{');
        objectNestingDepth++;
        expectLineBreak();
        LinkedHashMap<String, SimpleJsonProperty> properties = new LinkedHashMap<>();
        for (;;) {
            String propertyName = parseSimpleJsonString().value;
            if (properties.containsKey(propertyName))
                throw new SimpleJsonParserException(location(), "Duplicate property");
            expect(':');
            expect(' ');
            SimpleJsonValue propertyValue = parseSimpleJsonValue();
            SimpleJsonProperty property = new SimpleJsonProperty(propertyName, propertyValue);
            properties.put(propertyName, property);
            if (peek() != ',')
                break;
            expect(',');
            expectLineBreak();
        }
        objectNestingDepth--;
        expectLineBreak();
        expect('}');
        return new SimpleJsonObject(properties);
    }

    /**
     * Parses the given text and returns the simple JSON object.
     *
     * @param text the text to parse
     * @return the given text, parsed as a SimpleJsonObject
     */
    public static SimpleJsonObject parseObject(String text) {
        SimpleJsonParser parser = new SimpleJsonParser(text);
        return parser.parseSimpleJsonObject();
    }

    /**
     * Checks if the given text has can be parsed. Returns the location of the error if it can't be parsed, otheriwse it returns null
     *
     * @return a textLocation determining the location of the error, or null if there were no errors
     */
    public static TextLocation getErrorLocation(String text) {
        SimpleJsonParser parser = new SimpleJsonParser(text);
        try {
            parser.parseSimpleJsonObject();
        } catch(Exception e) {

        }
        if (parser.peek() != -1)
            return parser.location();
        return null;
    }

}
