package files;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
public class InsertionPointTest {

    @BeforeEach
    void setUp() throws IOException {
        String path1 = "testresources/test.txt";
        FileBuffer fb1 = new FileBuffer(path1, System.lineSeparator().getBytes());
    }
    @Test
    void testGetLines(){

    }
    @Test
    void testRenderStatus(){

    }
    @Test
    void testGetScrollbar(){

    }
}
/*
the number of lines and number of characters
in the file buffer, the line and column of the insertion point, and, optionally, a
horizontal scroll bar.
 */