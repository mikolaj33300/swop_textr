package util.json;

class SimpleJsonParserException extends RuntimeException {

    /**
     * The TextLocation where this parsing exception occured
     */
    TextLocation location;

    /**
     * The String displayed when this error occurs
     */
    String innerMessage;

    /**
     * Creates a new SimpleJsonParserException with the given messages and a clone of the given location
     *
     * @param location the location of this exception
     * @param message the message of this exception
     */
    public SimpleJsonParserException(TextLocation location, String message) {
        super(location.clone() + ": " + message);
        this.location = location.clone();
        this.innerMessage = message;
    }
}
