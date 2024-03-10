package layouttree;

import files.FileBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LayoutTest {

    String path1;
    String path2;
    String path3;
    String path4;
    FileBuffer fb1;
    FileBuffer fb2;
    FileBuffer fb3;
    FileBuffer fb4;
    LayoutLeaf l1;
    LayoutLeaf l1a;
    LayoutLeaf l1p;
    LayoutLeaf l2;
    LayoutLeaf l2a;
    LayoutLeaf l2p;
    LayoutLeaf l3;
    LayoutLeaf l4;

    @BeforeEach
    void setUp() {
        path1 = "testresources/test.txt";
        path2 = "testresources/test2.txt";
        path3 = "testresources/test3.txt";
        path4 = "testresources/test4.txt";
        fb1 = new FileBuffer(path1);
        fb2 = new FileBuffer(path2);
        fb3 = new FileBuffer(path1);
        fb4 = new FileBuffer(path2);
        l1 = new LayoutLeaf(fb1, true);
        l1a = new LayoutLeaf(fb1, true);
        l1p = new LayoutLeaf(fb1, false);
        l2 = new LayoutLeaf(fb2, false);
        l2a = new LayoutLeaf(fb2, true);
        l2p = new LayoutLeaf(fb2, false);
        l3 = new LayoutLeaf(fb1, true);
        l4 = new LayoutLeaf(fb2, false);
    }
    @Test
    void testGetContainsActive(){

        assertTrue(l1.getContainsActive());
        assertFalse(l2.getContainsActive());
    }

    @Test
    void setContainsActive(){
        l1.setContainsActive(true);
        assertTrue(l1.getContainsActive());
        l1.setContainsActive(false);
        assertFalse(l1.getContainsActive());

        l2.setContainsActive(false);
        assertFalse(l2.getContainsActive());
        l2.setContainsActive(true);
        assertTrue(l2.getContainsActive());
    }
}


















