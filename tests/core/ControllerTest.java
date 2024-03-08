package core;

import core.Controller;
import files.FileBuffer;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.LayoutNode;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ControllerTest {

    /**
     * Tests the constructor. Will it create the layout correctly?
     */
    @Test
    public void testArguments() {
        String path1 = "testresources/test.txt";
        String path2 = "testresources/test.txt";

        Controller controller = new Controller(new String[]{path1, path2});

        // Na constructor zou volgende root layout moeten bestaan:
        FileBuffer buffer1 = new FileBuffer(path1);
        FileBuffer buffer2 = new FileBuffer(path2);
        ArrayList<Layout> leaves = new ArrayList<>();
        leaves.add(new LayoutLeaf(buffer1, false));
        leaves.add(new LayoutLeaf(buffer2, false));
        LayoutNode node = new LayoutNode(Layout.Orientation.HORIZONTAL, leaves);

        assert controller.getRootLayout().equals(node);
    }

}
