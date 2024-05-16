package util.json;

public record TextLocation(int line, int column) {

    /**
     * Returns a copy of this TextLocation
     *
     * @return a copy of this TextLocation
     */
    public TextLocation clone() {
        return new TextLocation(line, column);
    }
}