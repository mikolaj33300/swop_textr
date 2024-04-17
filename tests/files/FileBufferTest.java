package files;

import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.VerticalLayoutNode;
import org.junit.jupiter.api.Test;
import ui.FileBufferView;
import ui.Rectangle;
import ui.View;
import util.Debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList; import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

// Tests eenkel relevant op mac
public class FileBufferTest {

    @Test
    public void testConstructor() throws IOException {

        // Test if the FileHolder is equal == Paths are equal
        String path = "testresources/test.txt";
        FileHolder holder = new FileHolder(path, System.lineSeparator().getBytes());

        FileBuffer buffer = new FileBuffer(path, System.lineSeparator().getBytes());

        assertTrue(buffer.getFileHolder().equals(holder));

        // Test if the content is correctly retrieved
        String content = "abc";
        Debug.write(path, content);
        buffer = new FileBuffer(path, System.lineSeparator().getBytes());

        assertTrue(buffer.contentsEqual(new ArrayList<Byte>(Arrays.<Byte>asList(FileAnalyserUtil.wrapEachByteElem(content.getBytes())))));

    }

    @Test
    public void testClone() throws IOException {
        String path = "testresources/test.txt";
        FileBuffer buffer = new FileBuffer(path, System.lineSeparator().getBytes());

        assertTrue(buffer.equals(buffer.clone()));
    }

    @Test
    public void testWriteSave() throws IOException {
        // Test if the function write correctly adds strings to the content.
        String text = "i love termios ; long live kaas";
        String path = "testresources/test.txt";
        Debug.write(path, text);
        FileBuffer buffer = new FileBuffer(path, System.lineSeparator().getBytes());
        assertEquals(new String(buffer.getBytes()), new String(text.getBytes()));

        buffer.write("b".getBytes()[0], 0,0);
        String result = "bi love termios ; long live kaas";

        assertTrue(buffer.getDirty());
        buffer.save();

        assertFalse(buffer.getDirty());
        assertTrue(buffer.contentsEqual(new ArrayList<Byte>(Arrays.<Byte>asList(FileAnalyserUtil.wrapEachByteElem(result.getBytes())))));
        assertTrue(FileHolder.areContentsEqual(buffer.getFileHolder().getContent(), result.getBytes()));

    }


    @Test
    public void testEnterInsertionPoint() throws IOException {
        // One line test
        Debug.write("testresources/test.txt", "termios is life");
        FileBuffer buff = new FileBuffer("testresources/test.txt", System.lineSeparator().getBytes());
        assertEquals(1, buff.getLines().size());
        buff.enterInsertionPoint(0, 0);
        assertEquals(2, buff.getLines().size()); // Does buffer detect two lines correctly?

        // More lines
        Debug.write("testresources/test.txt", "i"+System.lineSeparator()+"b");
        buff = new FileBuffer("testresources/test.txt", System.lineSeparator().getBytes());
        assertEquals(2, buff.getLines().size());
    }

    @Test
    public void testAmountChar() throws IOException {

        Debug.write("testresources/test.txt", "kaas");
        FileBuffer buffer = new FileBuffer("testresources/test.txt", System.lineSeparator().getBytes());

        assertEquals(buffer.getAmountChars(), 4);

        buffer.write("a".getBytes()[0], 0,3);

        assertEquals(buffer.getAmountChars(),5);
    }

    @Test
    public void testDeleteCharacter() throws IOException {
        String textToWrite = "hallo kaas i am your loyal student i use termios daily";

        Debug.write("testresources/test.txt", textToWrite);
        FileBuffer buffer = new FileBuffer("testresources/test.txt", System.lineSeparator().getBytes());

        buffer.deleteCharacter(1, 0);

        assertTrue(
                FileHolder.areContentsEqual(
                        buffer.getBytes(),
                        "allo kaas i am your loyal student i use termios daily".getBytes()
                        )
                );

        Debug.write("testresources/test.txt", "hello kaas\nmister");

        buffer.save();

    }

    @Test
    public void testWriteUndo() throws IOException {
        String path = "testresources/test.txt";

        Debug.write(path, "");
        FileBuffer buffer = new FileBuffer(path, System.lineSeparator().getBytes());

        buffer.writeCmd("t".getBytes()[0], 0, 0);
        buffer.writeCmd("e".getBytes()[0], 0,1);
        buffer.writeCmd("s".getBytes()[0], 0,2);
        buffer.writeCmd("t".getBytes()[0], 0,3);

	buffer.undo();// I can undo
        assertEquals("tes", new String(buffer.getBytes()));
	buffer.undo();
        assertEquals("te", new String(buffer.getBytes()));
	buffer.undo();
        assertEquals("t", new String(buffer.getBytes()));
	buffer.undo();
        assertEquals("", new String(buffer.getBytes()));
	buffer.undo();// if we undo too much we don't do nothing
        assertEquals("", new String(buffer.getBytes()));
	buffer.redo();
        assertEquals("t", new String(buffer.getBytes()));
        buffer.writeCmd("u".getBytes()[0], 0,1);
	buffer.redo();// if we redo too much we don't do nothing
        assertEquals("tu", new String(buffer.getBytes()));
	buffer.redo();
        assertEquals("tu", new String(buffer.getBytes()));
    }

    @Test
    public void testDeleteUndo() throws IOException {
        String path = "testresources/test.txt";

        Debug.write(path, "");
        FileBuffer buffer = new FileBuffer(path, System.lineSeparator().getBytes());

        buffer.writeCmd("t".getBytes()[0], 0,0);
        buffer.writeCmd("e".getBytes()[0], 0,1);
        buffer.writeCmd("s".getBytes()[0], 0,2);
        buffer.writeCmd("t".getBytes()[0], 0,3);

	buffer.deleteCharacterCmd(2, 0);
        assertEquals("tst", new String(buffer.getBytes()));
	buffer.undo();
        assertEquals("test", new String(buffer.getBytes()));
	buffer.deleteCharacterCmd(0, 0);// if we delete at the first char we don't do nothing
        assertEquals("test", new String(buffer.getBytes()));
	buffer.undo();
        assertEquals("test", new String(buffer.getBytes()));
    }

    @Test
    public void testEnterUndo() throws IOException {
        String path = "testresources/test.txt";

        Debug.write(path, "lineOne");
        FileBuffer buffer = new FileBuffer(path, System.lineSeparator().getBytes());

        buffer.enterInsertionCmd(0,0);
        assertEquals(buffer.getLines().size(), 2);

        buffer.undo();
        assertEquals(buffer.getLines().size(), 1);
        assertEquals(new String(buffer.getBytes()), "lineOne");
    }
}
