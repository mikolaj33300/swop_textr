package layouttree;

import files.FileBuffer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayoutNodeTest {
    LayoutLeaf l1;
    LayoutLeaf l1p;
    LayoutLeaf l2;
    LayoutLeaf l2a;
    LayoutLeaf l3;
    LayoutLeaf l3a;
    LayoutLeaf l4;
    LayoutLeaf l4a;
    LayoutLeaf l5;
    LayoutLeaf l5a;
    LayoutLeaf l6;
    LayoutLeaf l6a;
    LayoutLeaf l7;
    LayoutLeaf l7a;
    LayoutLeaf l8;
    LayoutLeaf l8a;
    LayoutLeaf l9;
    LayoutLeaf l9a;
    LayoutLeaf l10;
    LayoutLeaf l10a;
    ArrayList<Layout> children1;
    ArrayList<Layout> children2;
    ArrayList<Layout> children3;
    LayoutNode hn1;
    LayoutNode hn2;
    LayoutNode vn1;
    LayoutNode vn2;

    @BeforeEach
    void setUp() {
        String path1 = "testresources/test.txt";
        String path2 = "testresources/test2.txt";
        String path3 = "testresources/test3.txt";
        String path4 = "testresources/test4.txt";
        String path5 = "testresources/test5.txt";
        String path6 = "testresources/test6.txt";
        String path7 = "testresources/test7.txt";
        String path8= "testresources/test8.txt";
        String path9 = "testresources/test9.txt";
        String path10 = "testresources/test10.txt";
        FileBuffer fb1 = new FileBuffer(path1);
        FileBuffer fb2 = new FileBuffer(path2);
        FileBuffer fb3 = new FileBuffer(path3);
        FileBuffer fb4 = new FileBuffer(path4);
        FileBuffer fb5 = new FileBuffer(path5);
        FileBuffer fb6 = new FileBuffer(path6);
        FileBuffer fb7 = new FileBuffer(path7);
        FileBuffer fb8 = new FileBuffer(path8);
        FileBuffer fb9 = new FileBuffer(path9);
        FileBuffer fb10 = new FileBuffer(path10);
        l1 = new LayoutLeaf(fb1, true);
        l1p = new LayoutLeaf(fb1, false);
        l2 = new LayoutLeaf(fb2, false);
        l2a = new LayoutLeaf(fb2, true);
        l3 = new LayoutLeaf(fb3, false);
        l3a = new LayoutLeaf(fb3, true);
        l4 = new LayoutLeaf(fb4, false);
        l4a = new LayoutLeaf(fb4, true);
        l5 = new LayoutLeaf(fb5, false);
        l5a = new LayoutLeaf(fb5, true);
        l6 = new LayoutLeaf(fb6, false);
        l6a = new LayoutLeaf(fb6, true);
        l7 = new LayoutLeaf(fb6, false);
        l7a = new LayoutLeaf(fb7, true);
        l8 = new LayoutLeaf(fb8, false);
        l8a = new LayoutLeaf(fb8, true);
        l9 = new LayoutLeaf(fb9, false);
        l9a = new LayoutLeaf(fb9, true);
        l10 = new LayoutLeaf(fb10, false);
        l10a = new LayoutLeaf(fb10, true);

        children1 = new ArrayList<>(Arrays.asList(l1,l2,l3));
        children2 = new ArrayList<>(Arrays.asList(l4,l5,l6));
        children3 = new ArrayList<>(Arrays.asList(l7,l8,l9));
        hn1 = new HorizontalLayoutNode(children1);
        vn1 = new VerticalLayoutNode(children2);
        hn2 = new HorizontalLayoutNode(children3);
        vn2 = new VerticalLayoutNode(children3);
    }
    //TODO: Constructor testen voor throws als kinderen tekort of illegal children
    @Test
    void getOrientation(){
        assertEquals(hn1.getOrientation(), LayoutNode.Orientation.HORIZONTAL);
        assertEquals(vn1.getOrientation(), LayoutNode.Orientation.VERTICAL);
    }
    @Test
    void testGetDirectChildren() {
        ArrayList<Layout> get_children1 = hn1.getDirectChildren();
        assertNotSame(get_children1,children1);
        for(int i = 0;i < children1.size();i++){
            assertEquals(get_children1.get(i), children1.get(i));
            assertNotSame(get_children1.get(i), children1.get(i));
        }
        ArrayList<Layout> get_children2 = vn1.getDirectChildren();
        assertNotSame(get_children2,children2);
        for(int i = 0;i < children2.size();i++){
            assertEquals(get_children2.get(i), children2.get(i));
            assertNotSame(get_children2.get(i), children2.get(i));
        }
        LayoutNode hn3 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(vn1,l9,l10)));
        ArrayList<Layout> get_children3 = hn3.getDirectChildren();
        assertNotSame(get_children3,children3);
        for(int i = 0;i < get_children3.size();i++){
            assertEquals(get_children3.get(i), Arrays.asList(vn1,l9,l10).get(i));
            assertNotSame(get_children3.get(i), Arrays.asList(vn1,l9,l10).get(i));
        }
    }
    @Test
    void moveFocus(){
        //Test Node with only leafs
        hn1.moveFocus(Layout.DIRECTION.LEFT);
        ArrayList<Layout> moved_children1 = hn1.getDirectChildren();
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), children1.get(i));
            assertNotSame(moved_children1.get(i), children1.get(i));
        }
        hn1.moveFocus(Layout.DIRECTION.RIGHT);
        moved_children1 = hn1.getDirectChildren();
        ArrayList<Layout> correct_children1 = new ArrayList<>(Arrays.asList(l1p,l2a,l3));
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }
        hn1.moveFocus(Layout.DIRECTION.RIGHT);
        moved_children1 = hn1.getDirectChildren();
        correct_children1 = new ArrayList<>(Arrays.asList(l1p,l2,l3a));
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }

        hn1.moveFocus(Layout.DIRECTION.RIGHT);
        moved_children1 = hn1.getDirectChildren();
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }

        hn1.moveFocus(Layout.DIRECTION.LEFT);
        moved_children1 = hn1.getDirectChildren();
        correct_children1 = new ArrayList<>(Arrays.asList(l1p,l2a,l3));
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }
    }
    @Test
    void moveFocusComplex(){
        //Test with a complex tree
        HorizontalLayoutNode hn10 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, vn2,l10)));
        VerticalLayoutNode currentlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(hn1,l4,hn10)));

        VerticalLayoutNode correctlayout = currentlayout.clone();
        currentlayout.moveFocus(Layout.DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2a,l3))),l4,hn10)));
        currentlayout.moveFocus(Layout.DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3a))),l4,hn10)));
        currentlayout.moveFocus(Layout.DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4a,hn10)));
        currentlayout.moveFocus(Layout.DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5a, vn2,l10))))));
        currentlayout.moveFocus(Layout.DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7a,l8,l9))),l10))))));
        currentlayout.moveFocus(Layout.DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8a,l9))),l10))))));
        currentlayout.moveFocus(Layout.DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8,l9a))),l10))))));
        currentlayout.moveFocus(Layout.DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, vn2,l10a))))));
        currentlayout.moveFocus(Layout.DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, vn2,l10a))))));
        currentlayout.moveFocus(Layout.DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8,l9a))),l10))))));
        currentlayout.moveFocus(Layout.DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8a,l9))),l10))))));
        currentlayout.moveFocus(Layout.DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7a,l8,l9))),l10))))));
        currentlayout.moveFocus(Layout.DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2a,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5a, vn2,l10))))));
        currentlayout.moveFocus(Layout.DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4a,hn10)));
        currentlayout.moveFocus(Layout.DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3a))),l4,hn10)));
        currentlayout.moveFocus(Layout.DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3))),l4,hn10)));
        currentlayout.moveFocus(Layout.DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2a,l3))),l4,hn10)));
        currentlayout.moveFocus(Layout.DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);
    }

    @Test
    void testInsertDirectChild(){}

    @Test
    void testRotateRelationshipNeighbor(){}

    @Test
    void testEquals(){
    }

    @Test
    void testtest(){
        VerticalLayoutNode vn12 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,hn2)));
        vn12.moveFocus(Layout.DIRECTION.RIGHT);
        int k = 5;
    }
}
























