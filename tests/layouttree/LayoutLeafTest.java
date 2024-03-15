package layouttree;

import files.FileBuffer;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LayoutLeafTest {
    LayoutLeaf l1;
    LayoutLeaf l1a;
    LayoutLeaf l1p;
    LayoutLeaf l2;
    LayoutLeaf l2a;
    LayoutLeaf l2p;
    LayoutLeaf l3;
    LayoutLeaf l4;
    LayoutNode ln;

    @BeforeEach
    void setUp() throws IOException {
        String path1 = "testresources/test.txt";
        String path2 = "testresources/test2.txt";
        String path3 = "testresources/test3.txt";
        String path4 = "testresources/test4.txt";
        FileBuffer fb1 = new FileBuffer(path1);
        FileBuffer fb2 = new FileBuffer(path2);
        FileBuffer fb3 = new FileBuffer(path3);
        FileBuffer fb4 = new FileBuffer(path4);
        l1 = new LayoutLeaf(path1, true);
        l1a = new LayoutLeaf(path1, true);
        l1p = new LayoutLeaf(path1, false);
        l2 = new LayoutLeaf(path2, false);
        l2a = new LayoutLeaf(path2, true);
        l2p = new LayoutLeaf(path2, false);
        l3 = new LayoutLeaf(path3, true);
        l4 = new LayoutLeaf(path4, false);
        ArrayList<Layout> children = new ArrayList<>();
        children.add(l1);
        children.add(l2);
        children.add(l4);
        ln = new VerticalLayoutNode(children);
    }

    //Dit zijn eigenlijk testen op ln -> testen op een object is niet mogelijk zonder representation exposure
    @Test
    void testMoveFocus(){
        ArrayList<Layout> children = ln.getDirectChildren();
        assertTrue(ln.getContainsActiveView());
        assertTrue(children.get(0).getContainsActiveView());
        assertFalse(children.get(1).getContainsActiveView());
        assertFalse(children.get(2).getContainsActiveView());

        ln.moveFocus(DIRECTION.RIGHT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActiveView());
        assertFalse(children.get(0).getContainsActiveView());
        assertTrue(children.get(1).getContainsActiveView());
        assertFalse(children.get(2).getContainsActiveView());

        ln.moveFocus(DIRECTION.LEFT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActiveView());
        assertTrue(children.get(0).getContainsActiveView());
        assertFalse(children.get(1).getContainsActiveView());
        assertFalse(children.get(2).getContainsActiveView());

        ln.moveFocus(DIRECTION.LEFT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActiveView());
        assertTrue(children.get(0).getContainsActiveView());
        assertFalse(children.get(1).getContainsActiveView());
        assertFalse(children.get(2).getContainsActiveView());

        ln.moveFocus(DIRECTION.RIGHT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActiveView());
        assertFalse(children.get(0).getContainsActiveView());
        assertTrue(children.get(1).getContainsActiveView());
        assertFalse(children.get(2).getContainsActiveView());

        ln.moveFocus(DIRECTION.RIGHT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActiveView());
        assertFalse(children.get(0).getContainsActiveView());
        assertFalse(children.get(1).getContainsActiveView());
        assertTrue(children.get(2).getContainsActiveView());

        ln.moveFocus(DIRECTION.RIGHT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActiveView());
        assertFalse(children.get(0).getContainsActiveView());
        assertFalse(children.get(1).getContainsActiveView());
        assertTrue(children.get(2).getContainsActiveView());

        assertTrue(l3.getContainsActiveView());
        l3.moveFocus(DIRECTION.RIGHT);
        assertTrue(l3.getContainsActiveView());
        l3.moveFocus(DIRECTION.LEFT);
        assertTrue(l3.getContainsActiveView());

        assertFalse(l4.getContainsActiveView());
        l4.moveFocus(DIRECTION.RIGHT);
        assertFalse(l4.getContainsActiveView());
        l4.moveFocus(DIRECTION.LEFT);
        assertFalse(l4.getContainsActiveView());
    }

    @Test
    void testEquals(){
        assertNotEquals(l1,l2);
        assertNotEquals(l1,l3);
        assertNotEquals(l1,l4);
        assertNotEquals(l2,l3);
        assertNotEquals(l2,l4);
        assertNotEquals(l3,l4);

        assertEquals(l1,l1a);
        assertNotEquals(l1,l1p);
        assertEquals(l2,l2p);
        assertNotEquals(l2,l2a);
    }
    @Test
    void testRender(){}
    @Test
    void testClone(){
        Layout l1_clone = l1.clone();

        assertEquals(l1,l1_clone);
        assertNotSame(l1,l1_clone);
    }
    @Test
    void testMakeLeftmostLeafActive(){
        assertTrue(l1.getContainsActiveView());
        l1.makeLeftmostLeafActive();
        assertTrue(l1.getContainsActiveView());

        assertFalse(l2.getContainsActiveView());
        l2.makeLeftmostLeafActive();
        assertTrue(l2.getContainsActiveView());
    }
    @Test
    void testMakeRightmostLeafActive(){
        assertTrue(l1.getContainsActiveView());
        l1.makeRightmostLeafActive();
        assertTrue(l1.getContainsActiveView());

        assertFalse(l2.getContainsActiveView());
        l2.makeRightmostLeafActive();
        assertTrue(l2.getContainsActiveView());
    }
    @Test
    void testGetLeftLeaf(){
        LayoutLeaf left_l1 = l1.getLeftLeaf();
        assertSame(left_l1, l1);
        assertEquals(left_l1,l1);
    }
    @Test
    void testSanitizeInputChild(){
    }
}


















