package files;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FileBufferTest {

    @Test
    public void testConstructor() {

        // Test if the FileHolder is equal == Paths are equal
        String path = "testresources/test.txt";
        FileHolder holder = new FileHolder(path);

        FileBuffer buffer = new FileBuffer(path);

        assertTrue(buffer.getFileHolder().equals(holder));

        // Test if the content is correctly retrieved
        String content = "abc";
        write(path, content);
        buffer = new FileBuffer(path);

        assertTrue(buffer.contentsEqual(new ArrayList<Byte>(Arrays.<Byte>asList(FileAnalyserUtil.wrapEachByteElem(content.getBytes())))));

    }

    @Test
    public void testClone() {
        String path = "testresources/test.txt";
        FileBuffer buffer = new FileBuffer(path);

        assertTrue(buffer.equals(buffer.clone()));
    }

    @Test
    public void testWriteSave() {
        // Test if the function write correctly adds strings to the content.
        String text = "i love termios ; long live btj";
        String path = "testresources/test.txt";
        write(path, text);
        FileBuffer buffer = new FileBuffer(path);

        assertEquals(new String(buffer.getBytes()), new String(text.getBytes()));

        String add = " ; i love using termios library ";
        String result = " ; i love using termios library i love termios ; long live btj";
        buffer.write(add);

        assertTrue(buffer.isDirty());
        buffer.save();

        assertFalse(buffer.isDirty());
        assertTrue(buffer.contentsEqual(new ArrayList<Byte>(Arrays.<Byte>asList(FileAnalyserUtil.wrapEachByteElem(result.getBytes())))));
        assertTrue(FileHolder.areContentsEqual(buffer.getFileHolder().getContent(), result.getBytes()));

    }

    @Test
    public void testEnterInsertionPoint() {
        write("testresources/test.txt", "hello");
        FileBuffer buff = new FileBuffer("testresources/test.txt");
        assertEquals(1, buff.getLines().size());
        buff.enterInsertionPoint();

        assertEquals(1, buff.getLines().size());

        write("testresources/test.txt", "hello\nhello");
        buff = new FileBuffer("testresources/test.txt");
        assertEquals(2, buff.getLines().size());

        buff.enterInsertionPoint();
        buff.write("hello mister");
        buff.enterInsertionPoint();
        //assertEquals(4, buff.getLines().size());
        //assertTrue(buff.getLines().get(3).isEmpty());
    }

    @Test
    public void testMoveCursor() {
        String insert = "i love btj <3";
        write("testresources/test.txt", insert);
        FileBuffer buff = new FileBuffer("testresources/test.txt");

        // Testing movement in one line
        // Down
        buff.moveCursor('B');
        assertEquals(0, buff.insertionPointLine);
        assertEquals(0, buff.insertionPointCol);

        // Right
        buff.moveCursor('C');
        assertEquals(0, buff.insertionPointLine);
        assertEquals(1, buff.insertionPointCol);

        for(int i = 0; i < insert.length(); i++)
            buff.moveCursor('C');
        assertEquals(insert.length()-1, buff.insertionPointCol);

        // Left
        buff.moveCursor('D');
        assertEquals(0, buff.insertionPointLine);
        assertEquals(insert.length()-2, buff.insertionPointCol);

        // Up
        buff.moveCursor('A');
        assertEquals(0, buff.insertionPointLine);
        assertEquals(insert.length()-2, buff.insertionPointCol);

        // Testing movement in more lines
        write("testresources/test.txt", "i love btj <3\n and also termios");
        buff = new FileBuffer("testresources/test.txt");

        buff.moveCursor('B');
        assertEquals(1, buff.insertionPointLine);
        assertEquals(0, buff.insertionPointCol);

        // Up to line 0
        buff.moveCursor('A');
        assertEquals(0, buff.insertionPointLine);
        // Up again
        buff.moveCursor('A');
        assertEquals(0, buff.insertionPointLine);

        // Try going to next line by going right
        for(int i = 0; i < insert.length()+1; i++)
            buff.moveCursor('C');
        assertEquals(1, buff.insertionPointLine);
        assertEquals(0, buff.insertionPointCol);


        buff.moveCursor('D');
        assertEquals(0, buff.insertionPointLine);
        assertEquals(insert.length(), buff.insertionPointCol);

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