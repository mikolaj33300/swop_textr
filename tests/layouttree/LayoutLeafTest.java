package layouttree;
import exception.HashNotMatchingException;
import org.junit.jupiter.api.*;
import util.MoveDirection;
import util.RotationDirection;

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
    void testConstructor(){
        LayoutLeaf ll = new LayoutLeaf(1);
        assertNull(ll.parent);
        assertEquals(ll.getContainedHashCode(),1);
    }

    @Test
    void testGetContainedHashCode(){
        assertEquals(l1.getContainedHashCode(),1);
    }

    @Test
   void testRotateRelationshipNeighborRootLeafCLock(){
        Layout rotated_ll = l1.rotateRelationshipNeighbor(RotationDirection.CLOCKWISE, 1);
        assertEquals(rotated_ll,l1);
    }

    @Test
    void testRotateRelationshipNeighborRootLeafCounter(){
        Layout rotated_ll = l1.rotateRelationshipNeighbor(RotationDirection.COUNTERCLOCKWISE, 1);
        assertEquals(rotated_ll,l1);
    }

    @Test
    void testGetNeighborsContainedHashLeftLeaf(){
        int returnedHash = l1.getNeighborsContainedHash(MoveDirection.LEFT, 1);
        assertEquals(returnedHash, 1);
    }

    @Test
    void testGetNeighborsContainedHashRightLeaf(){
        int returnedHash = l1.getNeighborsContainedHash(MoveDirection.RIGHT, 1);
        assertEquals(returnedHash, 1);
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
    void testDeleteRootLeaf(){
        assertNull(l1.delete(1));
    }

    @Test
    void testClone(){
        LayoutLeaf l1_copy = l1.clone();
        assertNotSame(l1,l1_copy);
        assertEquals(l1,l1_copy);
    }

    @Test
    void testInsertRightOfSpecifiedToRightSameHash(){
        LayoutLeaf l1 = new LayoutLeaf(1);
        Layout v1 = l1.insertRightOfSpecified(1, 2);

        ArrayList<Layout> toAdd = new ArrayList<>();
        toAdd.add(new LayoutLeaf(1));
        toAdd.add(new LayoutLeaf(2));
        assertEquals(v1, new VerticalLayoutNode(toAdd));
    }

    @Test
    void testInsertRightOfSpecifiedToLeftDifferentHash(){
        LayoutLeaf l1 = new LayoutLeaf(1);
        assertThrows(HashNotMatchingException.class, () -> l1.insertRightOfSpecified(2, 3));
    }
}
















