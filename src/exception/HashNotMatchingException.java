package exception;

public class HashNotMatchingException extends RuntimeException {
    public HashNotMatchingException() {
            super("Hash contained by this does not coincide with hash sought by method call");
    }
}
