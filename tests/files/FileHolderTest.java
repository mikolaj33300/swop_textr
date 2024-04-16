package files;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class FileHolderTest {
    FileHolder f1 = new FileHolder("testresources/test.txt", System.lineSeparator().getBytes());
    FileHolder f1_ = new FileHolder("testresources/test.txt", System.lineSeparator().getBytes());
    FileHolder f2 = new FileHolder("testresources/test2.txt", System.lineSeparator().getBytes());

    @Test
    void testGetPath() {
        assertEquals("testresources/test.txt", f1.getPath());
        assertEquals("testresources/test2.txt", f2.getPath());
    }

    @Test
    void testSave() throws IOException {
        String teststring1 = "string to test with";
        String teststring2 = "string to test with in another file";

        f1.save(teststring1.getBytes());
        assertTrue(FileHolder.areContentsEqual(f1.getContent(), teststring1.getBytes()));
        assertTrue(FileHolder.areContentsEqual(f1_.getContent(), teststring1.getBytes()));

        f1_.save(teststring2.getBytes());
        assertTrue(FileHolder.areContentsEqual(f1_.getContent(), teststring2.getBytes()));
        assertTrue(FileHolder.areContentsEqual(f1.getContent(), teststring2.getBytes()));
    }

    @Test
    void testGetContent() throws IOException {
        String teststring = "string to test with";

        f1.save(teststring.getBytes());
        assertTrue(FileHolder.areContentsEqual(f1.getContent(), teststring.getBytes()));
    }

    @Test
    void testClone() {
        FileHolder f1clone = f1.clone();

        assertNotSame(f1clone, f1);

        assertTrue(f1clone.equals(f1));
        assertTrue(f1.equals(f1clone));
    }

    @Test
    void testEqual() {
        assertTrue(f1.equals(f1));
        assertTrue(f1.equals(f1_));
        assertTrue(f1_.equals(f1));
        assertFalse(f1.equals(f2));
        assertFalse(f2.equals(f1));
    }
}

