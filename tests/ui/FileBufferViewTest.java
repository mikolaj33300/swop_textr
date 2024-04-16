package ui;

import controller.RealTermiosTerminalAdapter;
import controller.TermiosTerminalAdapter;
import controller.VirtualTestingTermiosAdapter;
import files.BufferCursorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Debug;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileBufferViewTest {

    FileBufferView filebufferview;

    BufferCursorContext bufferCursorContext;

    String path1;
    @BeforeEach
    public void setUp() throws IOException {
        path1 = "testresources/test.txt";
        bufferCursorContext = new BufferCursorContext(path1, System.lineSeparator().getBytes());
    }

    /**
     * Viseel werkt de render, niets testbaar met JUnit
     */
    @Test
    public void testRender() throws IOException {
        Debug.write(path1, "testingline");
        VirtualTestingTermiosAdapter virtualTestAdapter = new VirtualTestingTermiosAdapter(100, 2, new ArrayList<Integer>(0));
        FileBufferView toTestFBView = new FileBufferView(bufferCursorContext, virtualTestAdapter);
        toTestFBView.setScaledCoords(new Rectangle(0,0,1,1));
        toTestFBView.render(toTestFBView.hashCode());

        assertArrayEquals(virtualTestAdapter.getVirtualScreen().get(0), ("testingline"+ " ".repeat(88)+"+").toCharArray());
    }



    @Test
    public void testEquals(){
        assertEquals(filebufferview, new FileBufferView(bufferCursorContext, new RealTermiosTerminalAdapter()));
    }

    @Test
    public void testGetContainedFileBuffer(){
        assertTrue(bufferCursorContext.getFileBuffer().equals(bufferCursorContext));
    }

    /**
     * Visueel merkbaar, niets testbaar met Junit
     */
    @Test
    public void testRenderCursor(){

    }
}






















