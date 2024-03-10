package files;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBufferTest {

    @Test
    public void testConstructor() {

        // Test if the FileHolder is equal == Paths are equal
        String path = "testresources/test.txt";
        FileHolder holder = new FileHolder(path, null);

        FileBuffer buffer = new FileBuffer(path, null);

        assertTrue(buffer.getFileHolder().equals(holder));

        // Test if the content is correctly retrieved
        String content = "abc";
        write(path, content);
        buffer = new FileBuffer(path, null);

        assertEquals(new String(buffer.getContent()), content);

    }

    @Test
    public void testClone() {
        String path = "testresources/test.txt";
        FileBuffer buffer = new FileBuffer(path, null);

        assertTrue(buffer.equals(buffer.clone()));
    }

    @Test
    public void testWriteSave() {
        // Test if the function write correctly adds strings to the content.
        String text = "i love termios ; long live btj";
        String path = "testresources/test.txt";
        write(path, text);
        FileBuffer buffer = new FileBuffer(path, null);

        assertEquals(new String(buffer.getContent()), new String(text.getBytes()));

        String add = " ; i love using termios library ";
        String result = " ; i love using termios library i love termios ; long live btj";
        buffer.write(add);

        assertTrue(buffer.isDirty());
        buffer.save();

        assertFalse(buffer.isDirty());
        assertEquals(new String(buffer.getContent()), new String(result.getBytes()));
        assertEquals(new String(buffer.getFileHolder().getContent()), new String(result.getBytes()));

    }

    /**
     * Helper method that writes given text into the file at given path
     */
    private void write(String path, String text) {
        try {
            // Overwrite file test.txt
            FileWriter writer = new FileWriter(new File(path));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}