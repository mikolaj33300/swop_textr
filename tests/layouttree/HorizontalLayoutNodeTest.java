package layouttree;

import files.FileBuffer;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class HorizontalLayoutNodeTest {
    LayoutLeaf l1;
    LayoutLeaf l2;
    LayoutLeaf l3;
    LayoutLeaf l4;
    LayoutLeaf l5;
    LayoutLeaf l6;
    LayoutLeaf l7;
    HorizontalLayoutNode hn1;
    HorizontalLayoutNode hn2;
    VerticalLayoutNode vn1;
    HorizontalLayoutNode hn11;


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
        hn1 = new HorizontalLayoutNode(children1);

        ArrayList<Layout> children2 = new ArrayList<>();
        children2.add(l3);
        children2.add(l4);
        hn2 = new HorizontalLayoutNode(children2);

        ArrayList<Layout> children3 = new ArrayList<>();
        children3.add(l5);
        children3.add(l6);
        vn1 = new VerticalLayoutNode(children3);

        HorizontalLayoutNode hn14 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l6,l7)));
        HorizontalLayoutNode hn13 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l4,l5)));
        HorizontalLayoutNode hn12 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3)));
        VerticalLayoutNode vn11 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,hn12)));
        VerticalLayoutNode vn12 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(hn13,hn14)));
        hn11 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(vn11,vn12)));
    }
    @Test
    void testGetOrientation(){
        assertEquals(hn1.getOrientation(), LayoutNode.Orientation.HORIZONTAL);
        assertNotEquals(hn1.getOrientation(), LayoutNode.Orientation.VERTICAL);
    }
    @Test
    void testIsAllowedToBeChildOf(){
        assertFalse(hn1.isAllowedToBeChildOf(hn2));
        assertTrue(hn1.isAllowedToBeChildOf(vn1));
    }

    @Test
    void testClone(){
        LayoutNode hn1_clone = hn1.clone();
        assertEquals(hn1,hn1_clone);
        assertNotSame(hn1,hn1_clone);

        LayoutNode hn11_clone = hn11.clone();
        assertEquals(hn11,hn11_clone);
        assertNotSame(hn11,hn11_clone);
    }
}

























