package exception;

public class HashNotMatchingException extends RuntimeException {

    /**
     * Creates a new HashNotMatchingException
     * Used when a hash does not match the hash sought by a method call
     */
    public HashNotMatchingException() {
            super("Hash contained by this does not coincide with hash sought by method call");
    }
}
