package files;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.*;

public class FileHolderTest {
    FileHolder f1 = new FileHolder("testresources/test.txt", null);
    FileHolder f1_ = new FileHolder("testresources/test.txt", null);
    FileHolder f2 = new FileHolder("testresources/test2.txt", null);

    @Test
    void testGetPath(){
        assertEquals("testresources/test.txt",f1.getPath());
        assertEquals("testresources/test2.txt",f2.getPath());
    }

    @Test
    void testSave(){
        String teststring1 = "string to test with";
        String teststring2 = "string to test with in another file";

        f1.save(teststring1);
        assertEquals(f1.getContent(),teststring1);
        assertEquals(f1_.getContent(),teststring1);

        f1_.save(teststring2);
        assertEquals(f1_.getContent(),teststring2);
        assertEquals(f1.getContent(),teststring2);
    }
    @Test
    void testGetContent(){
        String teststring = "string to test with";

        f1.save(teststring);
        assertEquals(f1.getContent(),teststring);
    }

    @Test
    void testClone(){
        FileHolder f1clone = f1.clone();

        assertNotSame(f1clone, f1);

        assertTrue(f1clone.equals(f1));
        assertTrue(f1.equals(f1clone));
    }

    @Test
    void testEqual(){
        assertTrue(f1.equals(f1));
        assertTrue(f1.equals(f1_));
        assertTrue(f1_.equals(f1));
        assertFalse(f1.equals(f2));
        assertFalse(f2.equals(f1));
    }
}

