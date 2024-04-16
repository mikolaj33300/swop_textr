package layouttree;

import controller.TextR;
import org.junit.jupiter.api.*;

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
        }



    @Test
    void testRotateRelationshipNeighborAbleCounterClockLeaf(){
        Layout ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode correct_ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        ln = ln.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE,1);
        assertEquals(ln,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborUnableClockLeaf(){
        Layout ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        ln = ln.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE,2);
        assertEquals(ln,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborAbleClockLeaf(){
        Layout ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode correct_ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1)));
        ln = ln.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE,1);
        assertEquals(ln,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborUnableCounterClockLeaf(){
        Layout ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        ln = ln.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE,2);
        assertEquals(ln,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborAbleCounterClockNode(){
        Layout ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode correct_ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        ln = ln.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE,1);
        assertEquals(ln,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborUnableCounterClockNode(){
        Layout ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        ln = ln.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE,2);
        assertEquals(ln,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborAbleClockNode(){
        Layout ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode correct_ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1)));
        ln = ln.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE,1);
        assertEquals(ln,correct_ln);
    }

    @Test
    void testRotateRelationshipNeighborUnableClockNode(){
        Layout ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode correct_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        ln = ln.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE,2);
        assertEquals(ln,correct_ln);
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
    void moveFocusBasic(){
        //Test Node with only leafs
        int returnedHash = hn1.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 1);
        assertEquals(returnedHash, 1);

        returnedHash = hn1.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 1);
        assertEquals(returnedHash, 2);

        returnedHash = hn1.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 2);
        assertEquals(returnedHash, 3);

        returnedHash = hn1.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 3);
        assertEquals(returnedHash, 3);

        returnedHash = hn1.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 3);
        assertEquals(returnedHash, 2);
    }
    @Test
    void moveFocusComplex(){
        //Test with a complex tree
        HorizontalLayoutNode hn10 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, vn2,l10)));
        VerticalLayoutNode currentlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(hn1,l4,hn10)));

        int returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 1);
        assertEquals(returnedHash,1);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 1);
        assertEquals(returnedHash,2);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 2);
        assertEquals(3,returnedHash);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 3);
        assertEquals(returnedHash, 4);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 4);
        assertEquals(returnedHash,5);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 5);
        assertEquals(returnedHash,7);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 7);
        assertEquals(returnedHash,8);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 8);
        assertEquals(returnedHash,9);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 9);
        assertEquals(returnedHash,10);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, 10);
        assertEquals(returnedHash,10);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 10);
        assertEquals(returnedHash,9);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 9);
        assertEquals(returnedHash,8);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 8);
        assertEquals(returnedHash,7);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 7);
        assertEquals(returnedHash,5);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 5);
        assertEquals(returnedHash,4);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 4);
        assertEquals(returnedHash,3);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 3);
        assertEquals(returnedHash,2);

        returnedHash = currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, 2);
        assertEquals(returnedHash,1);
    }

    @Test
    void testInsertDirectChild(){
        ArrayList<Layout> old_children = vn1.getDirectChildren();
        vn1.insertDirectChild(l4);
        ArrayList<Layout> new_children = vn1.getDirectChildren();
        old_children.add(l4);
        assertEquals(old_children,new_children);

        vn1.insertDirectChild(hn2);
        new_children = vn1.getDirectChildren();
        old_children.add(hn2);
        assertEquals(old_children,new_children);
    }

/*
    @Test
    void testRotateRelationshipNeighborLeafs() {
        Layout current_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));

        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, 1);
        Layout correct_layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1, l2)));
        assertEquals(current_layout, correct_layout);

        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, 1);
        correct_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2, l1)));
        assertEquals(current_layout, correct_layout);

        //Can't rotate any further, active leaf is mostright
        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, 1);
        correct_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2, l1)));
        assertEquals(current_layout, correct_layout
        );

        current_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1, l2)));

        //Can't rotate any further active leaf is mostright
        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, 1);
        correct_layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2, l1)));
        assertEquals(current_layout, correct_layout);

        //Test neighbours not affected
        current_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,l1,l2,l4)));

        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, 1);
        correct_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1, l2))),l4)));
        assertEquals(current_layout, correct_layout);
    }

    @Test
    void testRotateRelationShipNeighbourLeafsUnderNode(){
        //Leaf on node with leaf on same node
        Layout root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2))),l3)));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, 1);
        LayoutNode correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3)));
        assertEquals(root_clock,correct_clock);

        Layout root_counter = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2))),l3)));
        root_counter = root_counter.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, 1);
        VerticalLayoutNode correct_counter = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3)));
        assertEquals(root_counter,correct_counter);

        //Leaf off node with leaf on node
        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, 1);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3)));
        assertEquals(root_clock, correct_clock);

        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, 1);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3)));
        assertEquals(root_clock, correct_clock);

        //Leaf on node with leaf off node
        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1))),l3)));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, 1);
        correct_clock = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3)));
        assertEquals(root_clock, correct_clock);

        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, 1);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3)));
        assertEquals(root_clock, correct_clock);

        //Leaf on node with leaf on other node
        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1))),new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,l4))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, 1);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3))),l4)));
        assertEquals(root_clock, correct_clock);

        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1))),new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,l4))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, 1);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3,l1))),l4)));
        assertEquals(root_clock, correct_clock);
    }*/
}
