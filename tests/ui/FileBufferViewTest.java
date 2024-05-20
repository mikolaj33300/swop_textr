package ui;

import controller.adapter.RealTermiosTerminalAdapter;
import controller.adapter.VirtualTestingTermiosAdapter;
import files.BufferCursorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.Rectangle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBufferViewTest {

    FileBufferView filebufferview;

    BufferCursorContext bufferCursorContext;
    VirtualTestingTermiosAdapter adapter;

    @TempDir
    Path path1;

    String content1 = "termios this termios that, you get no say in it";

    @BeforeEach
    public void setUp() throws IOException {
        path1 = path1.resolve("test1.txt");
        Files.write(path1, content1.getBytes());
        bufferCursorContext = new BufferCursorContext(path1.toString(), System.lineSeparator().getBytes());
        adapter = new VirtualTestingTermiosAdapter(1000, 2, new ArrayList<Integer>(0));
        filebufferview = new FileBufferView(bufferCursorContext, adapter);
    }

    @Test
    public void testRender() throws IOException {
        VirtualTestingTermiosAdapter virtualTestAdapter = new VirtualTestingTermiosAdapter(1000, 3, new ArrayList<Integer>(0));
        FileBufferView toTestFBView = new FileBufferView(bufferCursorContext, virtualTestAdapter);
        toTestFBView.setRealCoords(new Rectangle(0,0,1000,3));
        toTestFBView.render(toTestFBView.hashCode());

        assertArrayEquals(virtualTestAdapter.getVirtualScreen().get(0), (content1 + " ".repeat(1000-content1.length())).toCharArray());
    }




    @Test
    public void testEquals(){
        assertEquals(filebufferview, new FileBufferView(bufferCursorContext, new RealTermiosTerminalAdapter()));
    }

    @Test
    public void testGetContainedFileBuffer(){
        assertEquals(filebufferview.getBufferCursorContext().getFileBuffer(), bufferCursorContext.getFileBuffer());
    }

    /**
     * Visueel merkbaar, niets testbaar met Junit
     */
    @Test
    public void testRenderCursor(){
        adapter.moveCursor(1, 2);
        assertEquals(adapter.getCursorX(), 2);
        assertEquals(adapter.getCursorY(), 1);
    }

}






















