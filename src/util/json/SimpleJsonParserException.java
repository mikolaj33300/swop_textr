package util.json;

class SimpleJsonParserException extends RuntimeException {
    TextLocation location;
    String innerMessage;
    public SimpleJsonParserException(TextLocation location, String message) {
        super(location + ": " + message);
        this.location = location;
        this.innerMessage = message;
    }
}
