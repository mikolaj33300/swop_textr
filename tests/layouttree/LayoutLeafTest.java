package layouttree;

import files.FileBuffer;

import org.junit.jupiter.api.*;

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
    void setUp() {
        String path1 = "testresources/test.txt";
        String path2 = "testresources/test2.txt";
        String path3 = "testresources/test3.txt";
        String path4 = "testresources/test4.txt";
        FileBuffer fb1 = new FileBuffer(path1, null);
        FileBuffer fb2 = new FileBuffer(path2, null);
        FileBuffer fb3 = new FileBuffer(path3, null);
        FileBuffer fb4 = new FileBuffer(path4, null);
        l1 = new LayoutLeaf(fb1, true);
        l1a = new LayoutLeaf(fb1, true);
        l1p = new LayoutLeaf(fb1, false);
        l2 = new LayoutLeaf(fb2, false);
        l2a = new LayoutLeaf(fb2, true);
        l2p = new LayoutLeaf(fb2, false);
        l3 = new LayoutLeaf(fb3, true);
        l4 = new LayoutLeaf(fb4, false);
        ArrayList<Layout> children = new ArrayList<Layout>();
        children.add(l1);
        children.add(l2);
        children.add(l4);
        ln = new VerticalLayoutNode(children);
    }
    @Test
    void testDeleteLeftLeaf(){
        /*assertEquals(ln.getDirectChildren().size(),3);
        ln.deleteLeftLeaf();
        assertEquals(ln.getDirectChildren().size(),2);
        ln.deleteLeftLeaf();
        assertEquals(ln.getDirectChildren().size(),1);
        ln.deleteLeftLeaf();*/
    }
    //Dit zijn eigenlijk testen op ln -> testen op een object is niet mogelijk zonder representation exposure
    @Test
    void testMoveFocus(){
        ArrayList<Layout> children = ln.getDirectChildren();
        assertTrue(ln.getContainsActive());
        assertTrue(children.get(0).getContainsActive());
        assertFalse(children.get(1).getContainsActive());
        assertFalse(children.get(2).getContainsActive());

        ln.moveFocus(Layout.DIRECTION.RIGHT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActive());
        assertFalse(children.get(0).getContainsActive());
        assertTrue(children.get(1).getContainsActive());
        assertFalse(children.get(2).getContainsActive());

        ln.moveFocus(Layout.DIRECTION.LEFT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActive());
        assertTrue(children.get(0).getContainsActive());
        assertFalse(children.get(1).getContainsActive());
        assertFalse(children.get(2).getContainsActive());

        ln.moveFocus(Layout.DIRECTION.LEFT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActive());
        assertTrue(children.get(0).getContainsActive());
        assertFalse(children.get(1).getContainsActive());
        assertFalse(children.get(2).getContainsActive());

        ln.moveFocus(Layout.DIRECTION.RIGHT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActive());
        assertFalse(children.get(0).getContainsActive());
        assertTrue(children.get(1).getContainsActive());
        assertFalse(children.get(2).getContainsActive());

        ln.moveFocus(Layout.DIRECTION.RIGHT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActive());
        assertFalse(children.get(0).getContainsActive());
        assertFalse(children.get(1).getContainsActive());
        assertTrue(children.get(2).getContainsActive());

        ln.moveFocus(Layout.DIRECTION.RIGHT);
        children = ln.getDirectChildren();
        assertTrue(ln.getContainsActive());
        assertFalse(children.get(0).getContainsActive());
        assertFalse(children.get(1).getContainsActive());
        assertTrue(children.get(2).getContainsActive());

        assertTrue(l3.getContainsActive());
        l3.moveFocus(Layout.DIRECTION.RIGHT);
        assertTrue(l3.getContainsActive());
        l3.moveFocus(Layout.DIRECTION.LEFT);
        assertTrue(l3.getContainsActive());

        assertFalse(l4.getContainsActive());
        l4.moveFocus(Layout.DIRECTION.RIGHT);
        assertFalse(l4.getContainsActive());
        l4.moveFocus(Layout.DIRECTION.LEFT);
        assertFalse(l4.getContainsActive());
    }
    @Test
    void testRotateRelationshipNeighbor(){
        //Layout.ROT_DIRECTION.CLOCKWISE
        //Layout.ROT_DIRECTION.COUNTERCLOCKWISE
    }
    @Test
    void testEquals(){
        assertEquals(l1, l1);
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
    void render(){}
    @Test
    void testClone(){
        Layout l1_clone = l1.clone();

        assertEquals(l1,l1_clone);
        assertNotSame(l1,l1_clone);
    }
    @Test
    void testMakeLeftmostLeafActive(){
        assertTrue(l1.getContainsActive());
        l1.makeLeftmostLeafActive();
        assertTrue(l1.getContainsActive());

        assertFalse(l2.getContainsActive());
        l2.makeLeftmostLeafActive();
        assertTrue(l2.getContainsActive());
    }
    @Test
    void testMakeRightmostLeafActive(){
        assertTrue(l1.getContainsActive());
        l1.makeRightmostLeafActive();
        assertTrue(l1.getContainsActive());

        assertFalse(l2.getContainsActive());
        l2.makeRightmostLeafActive();
        assertTrue(l2.getContainsActive());
    }
    @Test
    void testGetLeftLeaf(){
        LayoutLeaf left_l1 = (LayoutLeaf) l1.getLeftLeaf();
        assertNotSame(left_l1, l1);
        assertEquals(left_l1,l1);
    }
    @Test
    void testSanitizeInputChild(){
    }
}


















