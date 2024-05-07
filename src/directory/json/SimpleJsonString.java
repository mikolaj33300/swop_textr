package directory.json;

final class SimpleJsonString extends SimpleJsonValue {
    TextLocation start;
    int length;
    String value;
    SimpleJsonString(TextLocation start, int length, String value) {
        this.start = start;
        this.length = length;
        this.value = value;
    }

    @Override
    boolean isObject() {
        return false;
    }

}
