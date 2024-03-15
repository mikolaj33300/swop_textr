package ui;
import files.FileBuffer;
import layouttree.LayoutLeaf;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class FileBufferViewTest {
    LayoutLeaf layouleaf;
    FileBufferView filebufferview;
    FileBuffer filebuffer;
    String path;


    @BeforeEach
    public void setUp(){
        path = "testresources/test.txt";
        LayoutLeaf leaf = new LayoutLeaf(path,true);
        filebufferview = new FileBufferView(path, leaf);
        filebuffer = new FileBuffer(path);
    }

    @Test
    public void testSetCoords(){
        //filebufferview.setCorrectCoords();
    }

    @Test
    public void testGetContainedFileBuffer(){
        assertEquals(filebufferview.getContainedFileBuffer(),filebuffer);
        assertNotSame(filebufferview.getContainedFileBuffer(),filebufferview);

    }

    //TODO: test visueel
    @Test
    public void testRenderCursor(){}
}






















