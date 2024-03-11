package layouttree;

import files.FileBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class LayoutNodeTest {
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
        ln = new LayoutNode(Layout.Orientation.VERTICAL,children);
    }

}
