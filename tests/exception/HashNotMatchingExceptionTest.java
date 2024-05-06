package exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashNotMatchingExceptionTest {
    @Test
    void testConstructor() {
        HashNotMatchingException exception = new HashNotMatchingException();
        assertEquals("Hash contained by this does not coincide with hash sought by method call", exception.getMessage());
    }
}