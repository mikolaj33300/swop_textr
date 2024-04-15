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
    }
/*
    @Test
    void testRotateRelationshipNeighbor(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode correct_ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        ln.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE,1);
        assertEquals(ln,correct_ln);

    }


/*
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
        hn1.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        ArrayList<Layout> moved_children1 = hn1.getDirectChildren();
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), children1.get(i));
            assertNotSame(moved_children1.get(i), children1.get(i));
        }
        hn1.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        moved_children1 = hn1.getDirectChildren();
        ArrayList<Layout> correct_children1 = new ArrayList<>(Arrays.asList(l1p,l2a,l3));
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }
        hn1.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        moved_children1 = hn1.getDirectChildren();
        correct_children1 = new ArrayList<>(Arrays.asList(l1p,l2,l3a));
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }

        hn1.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        moved_children1 = hn1.getDirectChildren();
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }

        hn1.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
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
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2a,l3))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3a))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4a,hn10)));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5a, vn2,l10))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7a,l8,l9))),l10))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8a,l9))),l10))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8,l9a))),l10))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, vn2,l10a))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, vn2,l10a))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8,l9a))),l10))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8a,l9))),l10))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7a,l8,l9))),l10))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5a, vn2,l10))))));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4a,hn10)));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3a))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2a,l3))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);
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

    @Test
    void testRotateRelationshipNeighbourRoot() throws IOException {
        Layout l10 = new LayoutLeaf("testresources/test.txt", true);
        Layout l10_clone = l10.clone();
        l10 = l10.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, hash);
        assertEquals(l10,l10_clone);
        l10 = l10.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, hash);
        assertEquals(l10,l10_clone);
    }

    @Test
    void testRotateRelationshipNeighborLeafs() {
        Layout current_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));

        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, hash);
        Layout correct_layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1, l2)));
        assertEquals(current_layout, correct_layout);

        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, hash);
        correct_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2, l1)));
        assertEquals(current_layout, correct_layout);

        //Can't rotate any further, active leaf is mostright
        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, hash);
        correct_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2, l1)));
        assertEquals(current_layout, correct_layout
        );

        current_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1, l2)));

        //Can't rotate any further active leaf is mostright
        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, hash);
        correct_layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2, l1)));
        assertEquals(current_layout, correct_layout);

        //Test neighbours not affected
        current_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,l1,l2,l4)));

        current_layout = current_layout.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, hash);
        correct_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1, l2))),l4)));
        assertEquals(current_layout, correct_layout);
    }

    @Test
    void testRotateRelationShipNeighbourLeafsUnderNode(){
        //Leaf on node with leaf on same node
        Layout root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2))),l3)));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, hash);
        LayoutNode correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3)));
        assertEquals(root_clock,correct_clock);

        Layout root_counter = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2))),l3)));
        root_counter = root_counter.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, hash);
        VerticalLayoutNode correct_counter = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3)));
        assertEquals(root_counter,correct_counter);

        //Leaf off node with leaf on node
        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, hash);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3)));
        assertEquals(root_clock, correct_clock);

        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, hash);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3)));
        assertEquals(root_clock, correct_clock);

        //Leaf on node with leaf off node
        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1))),l3)));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, hash);
        correct_clock = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3)));
        assertEquals(root_clock, correct_clock);

        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, hash);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3)));
        assertEquals(root_clock, correct_clock);

        //Leaf on node with leaf on other node
        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1))),new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,l4))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE, hash);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1,l3))),l4)));
        assertEquals(root_clock, correct_clock);

        root_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l1))),new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,l4))))));
        root_clock = root_clock.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE, hash);
        correct_clock = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3,l1))),l4)));
        assertEquals(root_clock, correct_clock);
    }


    @Test
    void testEquals() {

    }

    @Test
    void testClone() {
        LayoutNode complex_tree = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(hn1, l4, new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, vn2, l10))))));
        LayoutNode complex_clone = complex_tree.clone();
        assertEquals(complex_clone, complex_tree);
        assertNotSame(complex_clone, complex_tree);
    }*/
}
