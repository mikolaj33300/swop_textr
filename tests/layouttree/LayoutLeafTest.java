package layouttree;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LayoutLeafTest {
    LayoutLeaf l1;
    LayoutLeaf l2;
    LayoutLeaf l3;
    LayoutLeaf l4;

    @BeforeEach
    void setUp() throws IOException {
        l1 = new LayoutLeaf(1);
        l2 = new LayoutLeaf(2);
        l3 = new LayoutLeaf(3);
        l4 = new LayoutLeaf(4);
    }

    @Test
    void constructorTest() throws IOException {
        LayoutLeaf ll = new LayoutLeaf(1);
        assertNull(ll.parent);
        assertEquals(ll.containedHashCode,1);
    }

    @Test
    void testGetNeighborsContainedHashRightSolo(){
        assertEquals(l1.getNeighborsContainedHash(DIRECTION.RIGHT,1),1);
    }

    @Test
    void testGetNeighborsContainedHashLeftSolo(){
        assertEquals(l1.getNeighborsContainedHash(DIRECTION.LEFT,1),1);
    }

    @Test
    void testGetNeighborsContainedHashRightSameParentNext(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(l1.getNeighborsContainedHash(DIRECTION.RIGHT,1),2);
    }

    @Test
    void testGetNeighborsContainedHashRightSameParentEnd(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(l2.getNeighborsContainedHash(DIRECTION.RIGHT,2),2);
    }

    @Test
    void testGetNeighborsContainedHashLeftSameParentNext(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(l2.getNeighborsContainedHash(DIRECTION.LEFT,2),1);
    }

    @Test
    void testGetNeighborsContainedHashLeftSameParentEnd(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(l1.getNeighborsContainedHash(DIRECTION.LEFT,1),1);
    }

    @Test
    void testGetNeighborsContainedHashRightToLower(){
        HorizontalLayoutNode hln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,hln)));
        assertEquals(l1.getNeighborsContainedHash(DIRECTION.RIGHT,1),2);
    }

    @Test
    void testGetNeighborsContainedHashRightToHigher(){
        HorizontalLayoutNode hln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(hln,l3)));
        assertEquals(l2.getNeighborsContainedHash(DIRECTION.RIGHT,2),3);
    }

    @Test
    void testGetNeighborsContainedHashLeftToLower(){
        HorizontalLayoutNode hln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(hln,l3)));
        assertEquals(l3.getNeighborsContainedHash(DIRECTION.LEFT,3),2);
    }

    @Test
    void testGetNeighborsContainedHashLeftToHigher(){
        HorizontalLayoutNode hln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,hln)));
        assertEquals(l2.getNeighborsContainedHash(DIRECTION.LEFT,2),1);
    }

    @Test
   void testRotateRelationshipNeighbor(){
    }

    @Test
    void testEqualsSame() throws IOException {
        LayoutLeaf l1_copy = new LayoutLeaf(1);
        assertEquals(l1_copy,l1);
    }

    @Test
    void testEqualsDifHash(){
        assertNotEquals(l1,l2);
    }

    @Test
    void testEqualsDifObj(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3)));
        assertNotEquals(l1,ln);
    }

    @Test
    void testClone(){
        LayoutLeaf l1_copy = l1.clone();
        assertNotSame(l1,l1_copy);
        assertEquals(l1,l1_copy);
    }
}
















