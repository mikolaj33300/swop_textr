package layouttree;

import org.junit.jupiter.api.*;
import util.MoveDirection;
import util.RotationDirection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LayoutNodeTest {

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

        ArrayList<Layout> children1;
        ArrayList<Layout> children2;
        ArrayList<Layout> children3;
        LayoutNode hn1;
        LayoutNode hn2;
        LayoutNode vn1;
        LayoutNode vn2;

        ArrayList<Layout> mixed_children;
        ArrayList<Layout> two_leafs;
        Layout layout_two_leafs;
        Layout layout_mixed_children;

        @BeforeEach
        void setUp() throws IOException {
            l1 = new LayoutLeaf(1);
            l2 = new LayoutLeaf(2);
            l3 = new LayoutLeaf(3);
            l4 = new LayoutLeaf(4);
            l5 = new LayoutLeaf(5);
            l6 = new LayoutLeaf(6);
            l7 = new LayoutLeaf(7);
            l8 = new LayoutLeaf(8);
            l9 = new LayoutLeaf(9);
            l10 = new LayoutLeaf(10);

            children1 = new ArrayList<>(Arrays.asList(l1,l2,l3));
            children2 = new ArrayList<>(Arrays.asList(l4,l5,l6));
            children3 = new ArrayList<>(Arrays.asList(l7,l8,l9));
            hn1 = new HorizontalLayoutNode(children1);
            vn1 = new VerticalLayoutNode(children2);
            hn2 = new HorizontalLayoutNode(children3);
            vn2 = new VerticalLayoutNode(children3);

            mixed_children = new ArrayList<>(Arrays.asList(l1, new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2, l3))), l4));
            two_leafs = new ArrayList<>(Arrays.asList(l1,l2));
            layout_mixed_children = new VerticalLayoutNode(mixed_children);
            layout_two_leafs = new VerticalLayoutNode(two_leafs);
        }

    @Test
    void testGetDirectChildrenOnlyLeafs() {
        ArrayList<Layout> direct_children = ((LayoutNode)layout_two_leafs).getDirectChildren();
        assertNotSame(direct_children, two_leafs);
        assertEquals(direct_children, two_leafs);
    }

    @Test
    public void temporaryTestDelete(){
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l3)));
        vln.insertRightOfSpecified(1, 2);
        vln.delete(2);

    }

    @Test
    void TestGetDirectChildrenMixed() {
        LayoutNode mixed_layout = new VerticalLayoutNode(mixed_children);
        ArrayList<Layout> direct_children = mixed_layout.getDirectChildren();

        assertNotSame(direct_children, mixed_children);
        assertEquals(direct_children, mixed_children);
    }

    @Test
    void testGetNeighborsContainedHashLeftSameRootParentEnd(){
        int returnedHash = layout_two_leafs.getNeighborsContainedHash(MoveDirection.LEFT, 1);
        assertEquals(returnedHash, 1);
    }

    @Test
    void testGetNeighborsContainedHashLeftSameRootParentNext(){
        int returnedHash = layout_two_leafs.getNeighborsContainedHash(MoveDirection.LEFT, 2);
        assertEquals(returnedHash, 1);
    }

    @Test
    void testGetNeighborsContainedHashRightSameRootParentNext(){
        int returnedHash = layout_two_leafs.getNeighborsContainedHash(MoveDirection.RIGHT, 1);
        assertEquals(returnedHash, 2);
    }

    @Test
    void testGetNeighborsContainedHashRightSameRootParentEnd(){
        int returnedHash = layout_two_leafs.getNeighborsContainedHash(MoveDirection.RIGHT, 2);
        assertEquals(returnedHash, 2);
    }

    @Test
    void testGetNeighborsContainedHashLeftSameParentNext(){
        int returnedHash = layout_mixed_children.getNeighborsContainedHash(MoveDirection.LEFT, 3);
        assertEquals(returnedHash, 2);
    }

    @Test
    void testGetNeighborsContainedHashRightSameParentNext(){
        int returnedHash = layout_mixed_children.getNeighborsContainedHash(MoveDirection.RIGHT, 2);
        assertEquals(returnedHash, 3);
    }

    @Test
    void testGetNeighborsContainedHashRightToLower(){
        int returnedHash = layout_mixed_children.getNeighborsContainedHash(MoveDirection.RIGHT, 1);
        assertEquals(returnedHash, 2);
    }

    @Test
    void testGetNeighborsContainedHashRightToHigher(){
        int returnedHash = layout_mixed_children.getNeighborsContainedHash(MoveDirection.RIGHT, 3);
        assertEquals(returnedHash, 4);
    }

    @Test
    void testGetNeighborsContainedHashLeftToLower(){
        int returnedHash = layout_mixed_children.getNeighborsContainedHash(MoveDirection.LEFT, 4);
        assertEquals(returnedHash, 3);
    }

    @Test
    void testGetNeighborsContainedHashLeftToHigher(){
        int returnedHash = layout_mixed_children.getNeighborsContainedHash(MoveDirection.LEFT, 2);
        assertEquals(returnedHash, 1);
    }

    @Test
    void testInsertRightOfSpecified(){}

    @Test
    void testRotateRelationshipNeighborAbleCounterClockTowLeafs(){
        HorizontalLayoutNode correct_ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        layout_two_leafs = layout_two_leafs.rotateRelationshipNeighbor(RotationDirection.COUNTERCLOCKWISE,1);
        assertEquals(layout_two_leafs,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborUnableCounterClockTwoLeafs(){
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        layout_two_leafs = layout_two_leafs.rotateRelationshipNeighbor(RotationDirection.COUNTERCLOCKWISE,2);
        assertEquals(layout_two_leafs,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborAbleClockTwoLeafs(){
        HorizontalLayoutNode correct_ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1)));
        layout_two_leafs = layout_two_leafs.rotateRelationshipNeighbor(RotationDirection.CLOCKWISE,1);
        assertEquals(layout_two_leafs,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborUnableClockTwoLeafs(){
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        layout_two_leafs = layout_two_leafs.rotateRelationshipNeighbor(RotationDirection.CLOCKWISE,2);
        assertEquals(layout_two_leafs,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborAbleCounterClockWithLowerLeaf(){
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l4,l3))))));
        layout_mixed_children = layout_mixed_children.rotateRelationshipNeighbor(RotationDirection.COUNTERCLOCKWISE,3);
        assertEquals(layout_mixed_children,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborAbleClockWithLowerLeaf(){
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3,l4))))));
        layout_mixed_children = layout_mixed_children.rotateRelationshipNeighbor(RotationDirection.CLOCKWISE,3);
        assertEquals(layout_mixed_children,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborAbleCounterClockWithHigherLeaf(){
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3,l4)));
        layout_mixed_children = layout_mixed_children.rotateRelationshipNeighbor(RotationDirection.COUNTERCLOCKWISE,1);
        assertEquals(layout_mixed_children,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborAbleClockWithHigherLeaf(){
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3,l4)));
        layout_mixed_children = layout_mixed_children.rotateRelationshipNeighbor(RotationDirection.CLOCKWISE,1);
        assertEquals(layout_mixed_children,correct_ln);
    }

    @Test
    public void testChangeHashNoTarget(){
        VerticalLayoutNode og_vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));

        vln.changeHash(0,0);
        assertEquals(vln,og_vln);
    }

    @Test
    public void testChangeHashDirectChild(){
        VerticalLayoutNode og_vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l3,l2)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));

        vln.changeHash(1,3);
        assertEquals(vln,og_vln);
    }

    @Test
    public void testChangeHashInDirectChild(){
        VerticalLayoutNode og_vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l4))))));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3))))));

        vln.changeHash(3,4);
        assertEquals(vln,og_vln);
    }
}










