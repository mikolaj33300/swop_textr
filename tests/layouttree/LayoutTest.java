package layouttree;

import files.FileBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LayoutTest {

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
    void testGetRootLayoutLeafRoot(){
        Layout root = l1.getRootLayout();
        assertEquals(l1,root);
        assertNotSame(l1,root);
    }

    @Test
    void testGetRootLayoutUnclonedLeafRoot(){
        Layout root = l1.getRootLayoutUncloned();
        assertSame(l1,root);
    }

    @Test
    void testGetRootLayoutFromDirectChild(){
        LayoutNode layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        Layout root = layout.children.get(0).getRootLayout();
        assertEquals(layout,root);
        assertNotSame(layout,root);
    }

    @Test
    void testGetRootLayoutUnclonedFromDirectChild(){
        LayoutNode layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        Layout root = layout.children.get(0).getRootLayoutUncloned();
        assertSame(layout,root);
    }

    @Test
    void testGetRootLayoutFromIndirectDirectChild(){
        LayoutNode sub_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        LayoutNode layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(sub_layout,l3)));
        LayoutNode sub_layout_uncloned = (LayoutNode) layout.children.get(0);
        Layout root = sub_layout_uncloned.children.get(0).getRootLayout();
        assertEquals(layout,root);
        assertNotSame(layout,root);
    }

    @Test
    void testGetRootLayoutUnclonedFromIndirectDirectChild(){
        LayoutNode sub_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        LayoutNode layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(sub_layout,l3)));
        LayoutNode sub_layout_uncloned = (LayoutNode) layout.children.get(0);
        Layout root = sub_layout_uncloned.children.get(0).getRootLayoutUncloned();
        assertSame(layout,root);
    }

    @Test
    void testGetParentRoot(){
        assertNull(l1.getParent());
    }

     @Test
     void testGetParentChild(){
        LayoutNode layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        Layout parent = layout.children.get(0).getParent();
        assertEquals(parent,layout);
        assertNotSame(parent,layout);
     }



    /**
     * Testen hieronder file springen tussen Nodes en Leafs
     * Het is dus passend om ze als layout-geheel te testen
     */
    @Test
    void testGetNeighborsContainedHashRightSolo(){
        assertEquals(l1.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT,1),1);
    }

    @Test
    void testGetNeighborsContainedHashLeftSolo(){
        assertEquals(l1.getNeighborsContainedHash(MOVE_DIRECTION.LEFT,1),1);
    }

    @Test
    void testGetNeighborsContainedHashRightSameParentNext(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(ln.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT,1),2);
    }

    @Test
    void testGetNeighborsContainedHashRightSameParentEnd(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(l2.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT,2),2);
    }

    @Test
    void testGetNeighborsContainedHashLeftSameParentNext(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(ln.getNeighborsContainedHash(MOVE_DIRECTION.LEFT,2),1);
    }

    @Test
    void testGetNeighborsContainedHashLeftSameParentEnd(){
        LayoutNode ln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        assertEquals(ln.getNeighborsContainedHash(MOVE_DIRECTION.LEFT,1),1);
    }

    @Test
    void testGetNeighborsContainedHashRightToLower(){
        HorizontalLayoutNode hln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,hln)));
        assertEquals(vln.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT,1),2);
    }

    @Test
    void testGetNeighborsContainedHashRightToHigher(){
        HorizontalLayoutNode hln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(hln,l3)));
        assertEquals(vln.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT,2),3);
    }

    @Test
    void testGetNeighborsContainedHashLeftToLower(){
        HorizontalLayoutNode hln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(hln,l3)));
        assertEquals(vln.getNeighborsContainedHash(MOVE_DIRECTION.LEFT,3),2);
    }

    @Test
    void testGetNeighborsContainedHashLeftToHigher(){
        HorizontalLayoutNode hln = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(l2,l3)));
        VerticalLayoutNode vln = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,hln)));
        assertEquals(vln.getNeighborsContainedHash(MOVE_DIRECTION.LEFT,2),1);
    }

    @Test
    void testDeleteLeafNoCompression(){
        Layout og_layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2,l3)));
        Layout deleted_Layout = og_layout.delete(2);
        Layout correct_layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l3)));
        assertEquals(deleted_Layout,correct_layout);
    }

    @Test
    void testDeleteLeafNewLeafRoot(){
        Layout og_layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        Layout deleted_Layout = og_layout.delete(2);
        assertEquals(deleted_Layout,l1);
    }

    @Test
    void testDeleteLeafNewNodeRoot(){
        Layout sub_layout = new VerticalLayoutNode(new ArrayList<>(Arrays.asList(l1,l2)));
        Layout og_layout = new HorizontalLayoutNode(new ArrayList<>(Arrays.asList(sub_layout,l3)));
        Layout deleted_Layout = og_layout.delete(3);
        assertEquals(deleted_Layout,sub_layout);
    }
}


















