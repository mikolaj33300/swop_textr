package layouttree;

import org.junit.jupiter.api.*;
import ui.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class VerticalLayoutNodeTest {
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

    //TODO: als nieuwe functies in deze klasse, dan ook nog testen toe te voegen --> zie in laatste week of nodig

    @Test
    void testConstructor(){
        ArrayList<Layout> children = new ArrayList<>(Arrays.asList(l1,l2));
        VerticalLayoutNode ln = new VerticalLayoutNode(new ArrayList<>(children));

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
        VerticalLayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(ln.getOrientation(), LayoutNode.Orientation.VERTICAL);
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
        VerticalLayoutNode og_ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode clone_ln = og_ln.clone();

        assertEquals(og_ln,clone_ln);
        assertNotSame(og_ln,clone_ln);
        assertNotSame(og_ln.children, clone_ln.children);
    }

    @Test
    void testEqualsSameOrientationSameChildren(){
        VerticalLayoutNode ln1 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode ln2 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));

        assertEquals(ln1, ln2);
    }

    @Test
    void testEqualsDifferentOrientation(){
        VerticalLayoutNode ln1 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        HorizontalLayoutNode ln2 = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));

        assertNotEquals(ln1, ln2);
    }
    @Test
    void testEqualsDifferentAmountChildren(){
        VerticalLayoutNode ln1 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode ln2 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3)));

        assertNotEquals(ln1, ln2);
    }

    @Test
    void testEqualsDifferentChildren(){
        VerticalLayoutNode ln1 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode ln2 = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l3,l4)));

        assertNotEquals(ln1, ln2);
    }




    @Test
    public void testLayoutScaledCoords() throws IOException {
        LayoutLeaf l1 = new LayoutLeaf(1);
        LayoutLeaf l2 = new LayoutLeaf(2);
        ArrayList<Layout> toAdd = new ArrayList<>();
        toAdd.add(l1);
        toAdd.add(l2);
        VerticalLayoutNode v1 = new VerticalLayoutNode(toAdd);

        HashMap<Integer, Rectangle> coordsList = v1.getCoordsList(new Rectangle(0, 0, 1, 1));
        assertTrue(coordsList.get(1).equals(new Rectangle(0,0,1, 0.5)));

        assertTrue(coordsList.get(2).equals(new Rectangle(0,0.5,1, 0.5)));
    }
}





