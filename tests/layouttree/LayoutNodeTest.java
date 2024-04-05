package layouttree;

import core.TextR;
import files.FileBuffer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    void setUp() throws IOException {
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
        FileBuffer fb1 = new FileBuffer("testresources/test.txt");
        FileBuffer fb2 = new FileBuffer(path2);
        FileBuffer fb3 = new FileBuffer(path3);
        FileBuffer fb4 = new FileBuffer(path4);
        FileBuffer fb5 = new FileBuffer(path5);
        FileBuffer fb6 = new FileBuffer(path6);
        FileBuffer fb7 = new FileBuffer(path7);
        FileBuffer fb8 = new FileBuffer(path8);
        FileBuffer fb9 = new FileBuffer(path9);
        FileBuffer fb10 = new FileBuffer(path10);
        l1 = new LayoutLeaf(path1, true);
        l1p = new LayoutLeaf(path1, false);
        l2 = new LayoutLeaf(path2, false);
        l2a = new LayoutLeaf(path2, true);
        l3 = new LayoutLeaf(path3, false);
        l3a = new LayoutLeaf(path3, true);
        l4 = new LayoutLeaf(path4, false);
        l4a = new LayoutLeaf(path4, true);
        l5 = new LayoutLeaf(path5, false);
        l5a = new LayoutLeaf(path5, true);
        l6 = new LayoutLeaf(path6, false);
        l6a = new LayoutLeaf(path6, true);
        l7 = new LayoutLeaf(path7, false);
        l7a = new LayoutLeaf(path7, true);
        l8 = new LayoutLeaf(path8, false);
        l8a = new LayoutLeaf(path8, true);
        l9 = new LayoutLeaf(path9, false);
        l9a = new LayoutLeaf(path9, true);
        l10 = new LayoutLeaf(path10, false);
        l10a = new LayoutLeaf(path10, true);

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
        hn1.getNeighborsContainedHash(DIRECTION.LEFT);
        ArrayList<Layout> moved_children1 = hn1.getDirectChildren();
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), children1.get(i));
            assertNotSame(moved_children1.get(i), children1.get(i));
        }
        hn1.getNeighborsContainedHash(DIRECTION.RIGHT);
        moved_children1 = hn1.getDirectChildren();
        ArrayList<Layout> correct_children1 = new ArrayList<>(Arrays.asList(l1p,l2a,l3));
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }
        hn1.getNeighborsContainedHash(DIRECTION.RIGHT);
        moved_children1 = hn1.getDirectChildren();
        correct_children1 = new ArrayList<>(Arrays.asList(l1p,l2,l3a));
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }

        hn1.getNeighborsContainedHash(DIRECTION.RIGHT);
        moved_children1 = hn1.getDirectChildren();
        for(int i = 0;i < moved_children1.size();i++){
            assertEquals(moved_children1.get(i), correct_children1.get(i));
            assertNotSame(moved_children1.get(i), correct_children1.get(i));
        }

        hn1.getNeighborsContainedHash(DIRECTION.LEFT);
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
        currentlayout.getNeighborsContainedHash(DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2a,l3))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3a))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4a,hn10)));
        currentlayout.getNeighborsContainedHash(DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5a, vn2,l10))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7a,l8,l9))),l10))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8a,l9))),l10))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8,l9a))),l10))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, vn2,l10a))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, vn2,l10a))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.RIGHT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8,l9a))),l10))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7,l8a,l9))),l10))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5, new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l7a,l8,l9))),l10))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4,new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l5a, vn2,l10))))));
        currentlayout.getNeighborsContainedHash(DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3))),l4a,hn10)));
        currentlayout.getNeighborsContainedHash(DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2,l3a))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1p,l2a,l3))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(DIRECTION.LEFT);
        assertEquals(currentlayout,correctlayout);

        correctlayout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3))),l4,hn10)));
        currentlayout.getNeighborsContainedHash(DIRECTION.LEFT);
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
    }
}

















































