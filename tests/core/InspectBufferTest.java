package core;

import files.FileBuffer;
import org.junit.jupiter.api.*;
import util.Debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InspectBufferTest {

    TextR c;
    private final String root = "testresources/";
    private final String path1 = root + "test.txt";
    private final String path2 = root + "test2.txt";
    private final String path3 = root + "test3.txt";


    @Test
    public void testInspectBuffer() throws IOException {
        TextR c1 = new TextR(new String[]{"testresources/test.txt", "testresources/test2.txt"});

        String insert = "i love btj <3";
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

        for(int i = 0; i < insert.length(); i++)
            buff.moveCursor('C');
        assertEquals(insert.length(), buff.getInsertionPointCol());

        // Left
        buff.moveCursor('D');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(insert.length()-1, buff.getInsertionPointCol());

        // Up
        buff.moveCursor('A');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(insert.length()-1, buff.getInsertionPointCol());

        // Testing movement in more lines
        Debug.write("testresources/test.txt", "i love btj <3\n and also termios");
        buff = new FileBuffer("testresources/test.txt");

        // Move cursor down
        buff.moveCursor('B');
        assertEquals(1, buff.getInsertionPointLine());
        assertEquals(0, buff.getInsertionPointCol());

        // Up to line 0
        buff.moveCursor('A');
        assertEquals(0, buff.getInsertionPointLine());
        // Up again
        buff.moveCursor('A');
        assertEquals(0, buff.getInsertionPointLine());

        // Try going to next line by going right
        for(int i = 0; i < 13; i++)
            buff.moveCursor('C');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(13, buff.getInsertionPointCol());

        // Test go back to previous line
        buff.moveCursor('D');
        assertEquals(0, buff.getInsertionPointLine());
        assertEquals(insert.length()-1, buff.getInsertionPointCol());


    }

}
