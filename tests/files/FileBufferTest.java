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
        for (byte b : add.getBytes()) {
            buffer.write(b);
        }

        assertTrue(buffer.getDirty());
        buffer.save();

        assertFalse(buffer.getDirty());
        assertTrue(buffer.contentsEqual(new ArrayList<Byte>(Arrays.<Byte>asList(FileAnalyserUtil.wrapEachByteElem(result.getBytes())))));
        assertTrue(FileHolder.areContentsEqual(buffer.getFileHolder().getContent(), result.getBytes()));

    }


    @Test
    public void testEnterInsertionPoint() {
        // One line test
        write("testresources/test.txt", "termios is life");
        FileBuffer buff = new FileBuffer("testresources/test.txt");
        assertEquals(1, buff.getLines().size());
        buff.enterInsertionPoint();
        assertEquals(2, buff.getLines().size()); // Does buffer detect two lines correctly?

        // More lines
        write("testresources/test.txt", "i\nb");
        buff = new FileBuffer("testresources/test.txt");
        assertEquals(2, buff.getLines().size());

        // MOve cursor to after 'maker'
        buff.moveCursor('B');
        assertEquals(1, buff.insertionPointLine);
        for(int i = 0; i < 15; i++)
            buff.moveCursor('C');

        buff.enterInsertionPoint();
        for (byte b : "btj\n".getBytes()) {
            buff.write(b);
        }
        buff.enterInsertionPoint();
        assertEquals(5, buff.getLines().size());
    }

    @Test
    public void testDeleteLine() {

        write("testresources/test.txt", "everyone likes btj\nbtj is the founder of termios\none of the best terminal applications");
        FileBuffer buffer = new FileBuffer("testresources/test.txt");

        buffer.moveCursor('B');
        buffer.deleteLine();
        assertEquals(1, buffer.insertionPointLine);

        assertEquals(2, buffer.getLines().size());
        assertEquals("one of the best terminal applications", new String(buffer.toArray(buffer.getLines().get(1))));

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
        assertEquals(0, buff.getInsertionPoint());

        // Right
        buff.moveCursor('C');
        assertEquals(0, buff.insertionPointLine);
        assertEquals(1, buff.insertionPointCol);
        assertEquals(1, buff.getInsertionPoint());

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

        // Move cursor down
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
        for(int i = 0; i < 13; i++)
            buff.moveCursor('C');
        assertEquals(1, buff.insertionPointLine);
        assertEquals(0, buff.insertionPointCol);

        // Test go back to previous line
        buff.moveCursor('D');
        assertEquals(0, buff.insertionPointLine);
        assertEquals(insert.length()-1, buff.insertionPointCol);

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