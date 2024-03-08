package files;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileBufferTest {

    @Test
    public void testConstructor() {

        // Test if the FileHolder is equal == Paths are equal
        String path = "testresources/test.txt";
        FileHolder holder = new FileHolder(path);

        FileBuffer buffer = new FileBuffer(path);

        assert buffer.getFileHolder().equals(holder);

        // Test if the content is correctly retrieved
        String content = "abc";
        write(path, content);
        buffer = new FileBuffer(path);

        assert buffer.getContent().equals(content);

    }

    @Test
    public void testClone() {
        String path = "testresources/test.txt";
        FileBuffer buffer = new FileBuffer(path);

        assert buffer.equals(buffer.clone());
    }

    @Test
    public void testWriteSave() {
        // Test if the function write correctly adds strings to the content.
        String text = "i love termios ; long live btj";
        String path = "testresources/test.txt";
        write(path, text);
        FileBuffer buffer = new FileBuffer(path);

        assert buffer.getContent().equals(text);

        String add = " ; i love using termios library ";
        String result = "i love termios ; long live btj ; i love using termios library ";
        buffer.write(add);

        assert buffer.isDirty();
        buffer.save();

        assert !buffer.isDirty();
        assert buffer.getContent().equals(result);
        assert buffer.getFileHolder().getContent().equals(result);


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