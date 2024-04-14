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
import java.util.ArrayList;
import java.util.Arrays;
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

        buffer.write("b".getBytes()[0], 0);
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
        buff.enterInsertionPoint(0);
        assertEquals(2, buff.getLines().size()); // Does buffer detect two lines correctly?

        // More lines
        Debug.write("testresources/test.txt", "i"+System.lineSeparator()+"b");
        buff = new FileBuffer("testresources/test.txt", System.lineSeparator().getBytes());
        assertEquals(2, buff.getLines().size());

/*        // MOve cursor to after 'maker'
        buff.moveCursor('B');
        assertEquals(1, buff.getInsertionPointLine());
        for(int i = 0; i < 15; i++)
            buff.moveCursor('C');

        buff.enterInsertionPoint();
        for (byte b : "kaas\n".getBytes()) {
            buff.write(b);
        }
        buff.enterInsertionPoint();
        assertEquals(4, buff.getLines().size());*/
    }

/*    @Test
    public void testDeleteLine() throws IOException {

        Debug.write("testresources/test.txt", "everyone likes kaas\nkaas is the founder of termios\none of the best terminal applications");
        FileBuffer buffer = new FileBuffer("testresources/test.txt");

        buffer.moveCursor('B');
        buffer.deleteLine();
        assertEquals(1, buffer.getInsertionPointLine());

        assertEquals(3, buffer.getLines().size());
        assertEquals("kaas is the founder of termios", new String(FileAnalyserUtil.toArray(buffer.getLines().get(1))));

    }*/

/*    @Test
    public void testMoveCursor() throws IOException {
        String insert = "i love kaas <3";
        Debug.write("testresources/test.txt", insert);
        FileBuffer buff = new FileBuffer("testresources/test.txt");

        // Testing movement in one line
        // Down
        buff.moveCursor('B');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(0, buff.getInsertionPointCol());
        assertEquals(0, buff.getInsertionPoint());

        // Right
        buff.moveCursor('C');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(1, buff.getInsertionPointCol());
        assertEquals(1, buff.getInsertionPoint());

        for (int i = 0; i < insert.length(); i++)
            buff.moveCursor('C');
        assertEquals(insert.length(), buff.getInsertionPointCol());

        // Left
        buff.moveCursor('D');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(insert.length() - 1, buff.getInsertionPointCol());

        // Up
        buff.moveCursor('A');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(insert.length() - 1, buff.getInsertionPointCol());

        // Testing movement in more lines
        Debug.write("testresources/test.txt", "i love kaas <3\n and also termios");
        buff = new FileBuffer("testresources/test.txt");

        // Move cursor down
        buff.moveCursor('B');
        assertEquals(1, buff.getInsertionPointLine());
        assertEquals(0, buff.getInsertionPointCol());

        buff.moveCursor('D');
        assertEquals(0, buff.getInsertionPointLine());
        buff.moveCursor('C');

        // Up to line 0
        buff.moveCursor('A');
        assertEquals(0, buff.getInsertionPointLine());
        // Up again
        buff.moveCursor('A');
        assertEquals(0, buff.getInsertionPointLine());

        // Try going to next line by going right
        for (int i = 0; i < 13; i++)
            buff.moveCursor('C');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(13, buff.getInsertionPointCol());

        // Test go back to previous line
        buff.moveCursor('D');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(insert.length() - 1, buff.getInsertionPointCol());

    }*/

    @Test
    public void testAmountChar() throws IOException {

        Debug.write("testresources/test.txt", "kaas");
        FileBuffer buffer = new FileBuffer("testresources/test.txt", System.lineSeparator().getBytes());

        assertEquals(buffer.getAmountChars(), 4);

        buffer.write("a".getBytes()[0], 3);

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

    /*    assertTrue(buffer.getInsertionPointCol() == 0);*/


        Debug.write("testresources/test.txt", "hello kaas\nmister");

        buffer.save();

    }

    @Test
    public void testLayoutScaledCoords() throws IOException {
        LayoutLeaf l1 = new LayoutLeaf(1);
        LayoutLeaf l2 = new LayoutLeaf(2);
        LayoutLeaf l3 = new LayoutLeaf(3);
        ArrayList<Layout> toAdd = new ArrayList<>();
        toAdd.add(l1);
        toAdd.add(l2);
        VerticalLayoutNode v1 = new VerticalLayoutNode(toAdd);

        HashMap<Integer, Rectangle> coordsList = v1.getCoordsList(new Rectangle(0, 0, 1, 1));
        assertEquals(coordsList.get(1).height, 0.5);
        assertEquals(coordsList.get(1).startY, 0);

        assertEquals(coordsList.get(2).height, 0.5);
        assertEquals(coordsList.get(2).startY, 0.5);

        View v = new FileBufferView(new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes()));
        v.setScaledCoords(coordsList.get(2));

    }

}