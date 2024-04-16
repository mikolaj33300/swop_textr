package ui;

import files.FileBuffer;
import layouttree.LayoutLeaf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Control;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBufferViewTest {
    LayoutLeaf layouleaf1;
    LayoutLeaf layouleaf2;
    LayoutLeaf layouleaf3;
    FileBufferView filebufferview1;
    FileBufferView filebufferview1_;
    FileBufferView filebufferview2;
    FileBufferView filebufferview3;
    FileBuffer filebuffer1;
    FileBuffer filebuffer2;
    FileBuffer filebuffer3;
    String path1;
    String path2;
    String path3;
    @BeforeEach
    public void setUp() throws IOException {
        path1 = "testresources/test.txt";
        path2 = "testresources/test2.txt";
        path3 = "testresources/test3.txt";
        LayoutLeaf leaf1 = new LayoutLeaf(path1,true);
        LayoutLeaf leaf2 = new LayoutLeaf(path2,true);
        LayoutLeaf leaf3 = new LayoutLeaf(path3,true);
        filebufferview1 = new FileBufferView(path1, leaf1);
        filebufferview1_ = new FileBufferView(path1, leaf1);
        filebufferview2 = new FileBufferView(path2, leaf2);
        filebufferview3 = new FileBufferView(path3, leaf3);
        filebuffer1 = new FileBuffer(path1);
        filebuffer2 = new FileBuffer(path2);
        filebuffer3 = new FileBuffer(path3);
    }

    /**
     * Viseel werkt de render, niets testbaar met JUnit
     */
    @Test
    public void testRender(){}

    @Test public void testGetStatusbarString(){
        String statusbar = filebufferview1.getStatusbarString();
        assertEquals(statusbar,"testresources/test.txt #Lines:1 #Chars:19 Insert:[0;0] Clean Active");
    }

    @Test
    public void testEquals(){
        assertTrue(filebufferview1.equals(filebufferview1_));
        assertFalse(filebufferview1.equals(filebufferview2));
    }


    /**
     * Visueel merkbaar, niets testbaar met Junit
     */
    @Test
    public void testClose(){}


    @Test
    public void testGetContainedFileBuffer(){
        assertTrue(filebufferview1.getContainedFileBuffer().equals(filebuffer1));
        assertNotSame(filebufferview1.getContainedFileBuffer(),filebuffer1);
    }

    /**
     * Visueel merkbaar, niets testbaar met Junit
     */
    @Test
    public void testRenderCursor(){}
}






















