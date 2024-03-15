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
        filebufferview2 = new FileBufferView(path2, leaf2);
        filebufferview3 = new FileBufferView(path3, leaf3);
        filebuffer1 = new FileBuffer(path1);
        filebuffer2 = new FileBuffer(path2);
        filebuffer3 = new FileBuffer(path3);
    }

    @Test
    public void testSetCoords(){
        //filebufferview.setCorrectCoords();
    }

    @Test
    public void testDeleteCharacter(){
        //Byte symbol = 96;
        //filebufferview1.getContainedFileBuffer().getByteContent();
        //filebufferview1.write(symbol);
    }



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






















