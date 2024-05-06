package exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathNotMatchingExceptionTest {
    @Test
    void testConstructor() {
        PathNotFoundException exception = new PathNotFoundException();
        assertEquals("Path not found.", exception.getMessage());
    }
}

