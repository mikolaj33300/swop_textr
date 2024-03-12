package core;

import files.FileBuffer;
import layouttree.HorizontalLayoutNode;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    /**
     * Tests the constructor. Will it create the layout correctly?
     */
    @Test
    public void testArguments() {
        String path1 = "testresources/test.txt";
        String path2 = "testresources/test.txt";
        String[] args = new String[]{"--lf", path1, path2};

        // Testing opening with --lf + paths
        Controller controller = new Controller(args);
        assertEquals(Controller.getLineSeparator(), "0a");

        // Testing opening with --crlf + paths
        args = new String[] {"--crlf", path1, path2};
        controller = new Controller(args);
        assertEquals(Controller.getLineSeparator(), "0d0a");

        // Testing only paths
        args = new String[]{path1, path2};
        controller = new Controller(args);
        assertNull(Controller.getLineSeparator());

        // Na constructor zou volgende root layout moeten bestaan:
        FileBuffer buffer1 = new FileBuffer(path1);
        FileBuffer buffer2 = new FileBuffer(path2);
        ArrayList<Layout> leaves = new ArrayList<>();
        leaves.add(new LayoutLeaf(buffer1, true));
        leaves.add(new LayoutLeaf(buffer2, false));

        HorizontalLayoutNode node = new HorizontalLayoutNode(leaves);

        assertTrue(controller.initRootLayout().equals(node));
    }

}
