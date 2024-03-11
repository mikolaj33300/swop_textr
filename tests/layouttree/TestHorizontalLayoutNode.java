package layouttree;

import files.FileBuffer;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestHorizontalLayoutNode {
    LayoutLeaf l1;
    LayoutLeaf l2;
    LayoutLeaf l3;
    LayoutLeaf l4;
    LayoutLeaf l5;
    LayoutLeaf l6;
    LayoutLeaf l7;
    LayoutLeaf l8;
    LayoutLeaf l9;
    LayoutLeaf l10;
    HorizontalLayoutNode hn1;
    HorizontalLayoutNode hn2;
    VerticalLayoutNode vn1;

    @BeforeEach
    void setUp() {
        String path1 = "testresources/test.txt";
        String path2 = "testresources/test2.txt";
        String path3 = "testresources/test3.txt";
        String path4 = "testresources/test4.txt";
        String path5 = "testresources/test5.txt";
        String path6 = "testresources/test6.txt";
        String path7 = "testresources/test7.txt";
        String path8 = "testresources/test8.txt";
        String path9 = "testresources/test9.txt";
        String path10 = "testresources/test10.txt";

        FileBuffer fb1 = new FileBuffer(path1, null);
        FileBuffer fb2 = new FileBuffer(path2, null);
        FileBuffer fb3 = new FileBuffer(path3, null);
        FileBuffer fb4 = new FileBuffer(path4, null);
        FileBuffer fb5 = new FileBuffer(path5, null);
        FileBuffer fb6 = new FileBuffer(path6, null);
        FileBuffer fb7 = new FileBuffer(path7, null);
        FileBuffer fb8 = new FileBuffer(path8, null);
        FileBuffer fb9 = new FileBuffer(path9, null);
        FileBuffer fb10 = new FileBuffer(path10, null);

        l1 = new LayoutLeaf(fb1,false);
        l2 = new LayoutLeaf(fb2, false);
        l3 = new LayoutLeaf(fb3, false);
        l4 = new LayoutLeaf(fb4, false);
        l5 = new LayoutLeaf(fb5,false);
        l6 = new LayoutLeaf(fb6,false);
        l7 = new LayoutLeaf(fb7,false);
        l8 = new LayoutLeaf(fb8,false);
        l9 = new LayoutLeaf(fb9,false);
        l10 = new LayoutLeaf(fb10,false);

        ArrayList<Layout> children1 = new ArrayList<Layout>();
        children1.add(l1);
        children1.add(l2);
        hn1 = new HorizontalLayoutNode(children1);

        ArrayList<Layout> children2 = new ArrayList<Layout>();
        children2.add(l3);
        children2.add(l4);
        hn2 = new HorizontalLayoutNode(children2);

        ArrayList<Layout> children3 = new ArrayList<Layout>();
        children3.add(l5);
        children3.add(l6);
        vn1 = new VerticalLayoutNode(children3);

    }
    @Test
    void testGetOrientation(){
        ArrayList<Layout> children = new ArrayList<Layout>();
        children.add(l1);
        children.add(l2);
        HorizontalLayoutNode hn = new HorizontalLayoutNode(children);
        assertEquals(hn.getOrientation(), LayoutNode.Orientation.HORIZONTAL);
        assertNotEquals(hn.getOrientation(), LayoutNode.Orientation.VERTICAL);
    }
    @Test
    void testGetNewMergedRotatedChild(){

    }
    @Test
    void testIsAllowedToBeChildOf(){
        assertFalse(hn1.isAllowedToBeChildOf(hn2));
        assertTrue(hn1.isAllowedToBeChildOf(vn1));
    }

    @Test
    void testClone(){
        HorizontalLayoutNode hn1_clone = hn1.clone();
        assertEquals(hn1,hn1_clone);
        assertNotSame(hn1,hn1_clone);
    }
}

























