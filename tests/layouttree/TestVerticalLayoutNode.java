package layouttree;

import files.FileBuffer;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestVerticalLayoutNode {
    LayoutLeaf l1;
    LayoutLeaf l2;
    LayoutLeaf l3;
    LayoutLeaf l4;
    LayoutLeaf l5;
    LayoutLeaf l6;
    LayoutLeaf l7;
    VerticalLayoutNode vn1;
    VerticalLayoutNode vn2;
    HorizontalLayoutNode hn1;
    VerticalLayoutNode vn11;


    @BeforeEach
    void setUp() {
        String path1 = "testresources/test.txt";
        String path2 = "testresources/test2.txt";
        String path3 = "testresources/test3.txt";
        String path4 = "testresources/test4.txt";
        String path5 = "testresources/test5.txt";
        String path6 = "testresources/test6.txt";
        String path7 = "testresources/test7.txt";

        FileBuffer fb1 = new FileBuffer(path1, null);
        FileBuffer fb2 = new FileBuffer(path2, null);
        FileBuffer fb3 = new FileBuffer(path3, null);
        FileBuffer fb4 = new FileBuffer(path4, null);
        FileBuffer fb5 = new FileBuffer(path5, null);
        FileBuffer fb6 = new FileBuffer(path6, null);
        FileBuffer fb7 = new FileBuffer(path7, null);

        l1 = new LayoutLeaf(fb1,false);
        l2 = new LayoutLeaf(fb2, false);
        l3 = new LayoutLeaf(fb3, false);
        l4 = new LayoutLeaf(fb4, false);
        l5 = new LayoutLeaf(fb5,false);
        l6 = new LayoutLeaf(fb6,false);
        l7 = new LayoutLeaf(fb7,false);

        ArrayList<Layout> children1 = new ArrayList<>();
        children1.add(l1);
        children1.add(l2);
        vn1 = new VerticalLayoutNode(children1);

        ArrayList<Layout> children2 = new ArrayList<>();
        children2.add(l3);
        children2.add(l4);
        vn2 = new VerticalLayoutNode(children2);

        ArrayList<Layout> children3 = new ArrayList<>();
        children3.add(l5);
        children3.add(l6);
        hn1 = new HorizontalLayoutNode(children3);

        VerticalLayoutNode vn14 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l6,l7)));
        VerticalLayoutNode vn13 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l4,l5)));
        VerticalLayoutNode vn12 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3)));
        HorizontalLayoutNode hn11 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,vn12)));
        HorizontalLayoutNode hn12 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(vn13,vn14)));
        vn11 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(hn11,hn12)));
    }
    @Test
    void testGetOrientation(){
        assertEquals(vn1.getOrientation(), LayoutNode.Orientation.VERTICAL);
        assertNotEquals(vn1.getOrientation(), LayoutNode.Orientation.HORIZONTAL);
    }
    @Test
    void testIsAllowedToBeChildOf(){
        assertFalse(vn1.isAllowedToBeChildOf(vn2));
        assertTrue(vn1.isAllowedToBeChildOf(hn1));
    }

    @Test
    void testClone(){
        LayoutNode hn1_clone = vn1.clone();
        assertEquals(vn1,hn1_clone);
        assertNotSame(vn1,hn1_clone);

        LayoutNode hn11_clone = vn11.clone();
        assertEquals(vn11,hn11_clone);
        assertNotSame(vn11,hn11_clone);
    }
}





