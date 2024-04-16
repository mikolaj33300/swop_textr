package layouttree;

import org.junit.jupiter.api.*;
import ui.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class HorizontalLayoutNodeTest {
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
        ArrayList<Layout> children = new ArrayList<>(Arrays.asList(l1,l2));
        HorizontalLayoutNode ln = new HorizontalLayoutNode(new ArrayList<>(children));

        assertEquals(children,ln.children);
        assertNotSame(children,ln.children);
        for(int i = 0; i<children.size();i++){
            assertEquals(children.get(i),ln.children.get(i));
            assertNotSame(children.get(i),ln.children.get(i));
        }
        assertNull(ln.parent);
    }

    @Test
    void testGetOrientation(){
        HorizontalLayoutNode ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(ln.getOrientation(), LayoutNode.Orientation.HORIZONTAL);
    }

    @Test
    void testIsAllowedToBeChildOfDifferentOrientation(){
        VerticalLayoutNode base_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode ext_ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,l4)));

        assertTrue(base_ln.isAllowedToBeChildOf(ext_ln));
    }

    @Test
    void testIsAllowedToBeChildOfSameOrientation(){
        VerticalLayoutNode base_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode ext_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l3,l4)));

        assertFalse(base_ln.isAllowedToBeChildOf(ext_ln));
    }

    @Test
    void testClone(){
        HorizontalLayoutNode og_ln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode clone_ln = og_ln.clone();

        assertEquals(og_ln,clone_ln);
        assertNotSame(og_ln,clone_ln);
        for(int i = 0; i <og_ln.children.size(); i++){
            assertEquals(og_ln.children.get(i),clone_ln.children.get(i));
            assertNotSame(og_ln.children.get(i),clone_ln.children.get(i));
        }
    }

    @Test
    void testEqualsSameOrientationSameChildren(){
        HorizontalLayoutNode ln1 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode ln2 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));

        assertEquals(ln1, ln2);
    }

    @Test
    void testEqualsSameOrientationSimilarChildren(){
        HorizontalLayoutNode ln1 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode ln2 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1.clone(),l2.clone())));

        assertEquals(ln1, ln2);
    }

    @Test
    void testEqualsDifferentOrientation(){
        HorizontalLayoutNode ln1 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode ln2 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));

        assertNotEquals(ln1, ln2);
    }
    @Test
    void testEqualsDifferentAmountChildren(){
        HorizontalLayoutNode ln1 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode ln2 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3)));

        assertNotEquals(ln1, ln2);
    }

    @Test
    void testEqualsDifferentChildren(){
        HorizontalLayoutNode ln1 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode ln2 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l3,l4)));

        assertNotEquals(ln1, ln2);
    }

    @Test
    public void testLayoutScaledCoords() throws IOException {
        LayoutLeaf l1 = new LayoutLeaf(1);
        LayoutLeaf l2 = new LayoutLeaf(2);
        ArrayList<Layout> toAdd = new ArrayList<>();
        toAdd.add(l1);
        toAdd.add(l2);
        HorizontalLayoutNode v1 = new HorizontalLayoutNode(toAdd);

        HashMap<Integer, Rectangle> coordsList = v1.getCoordsList(new Rectangle(0, 0, 1, 1));
        assertEquals(coordsList.get(1), new Rectangle(0, 0, 0.5, 1));

        assertEquals(coordsList.get(2), new Rectangle(0.5, 0, 0.5, 1));
    }

    @Test
    void testInsertToRight(){
        ArrayList<Layout> toAdd = new ArrayList<>();
        toAdd.add(new LayoutLeaf(1));
        toAdd.add(new LayoutLeaf(3));
        Layout v1 = new VerticalLayoutNode(toAdd);

        Layout v2 = v1.insertRightOfSpecified(1, 2);

        toAdd.add(1, new LayoutLeaf(2));
        assertEquals(v1, new VerticalLayoutNode(toAdd));
    }
}



























