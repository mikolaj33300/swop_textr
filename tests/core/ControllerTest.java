package core;

import files.FileBuffer;
import files.FileHolder;
import layouttree.HorizontalLayoutNode;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import org.junit.jupiter.api.Test;
import controller.TextR;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    //@Test
    /**
     * Tests the constructor. Will it create the layout correctly?
     */
    public void testArguments() throws IOException {
        String path1 = "testresources/test.txt";
        String path2 = "testresources/test.txt";


        String[] args = new String[]{"--lf", path1, path2};

        // Testing opening with --lf + paths
        TextR.setLineSeparatorFromArgs(args);
        assertArrayEquals(FileHolder.lineSeparator, new byte[]{0x0a});

        // Testing opening with --crlf + paths
        String[] sargs = new String[] {"--crlf", path1, path2};
        TextR.setLineSeparatorFromArgs(sargs);
        assertArrayEquals(TextR.getLineSeparatorArg(), new byte[]{0x0d, 0x0a});

        // Testing only paths
        args = new String[]{path1, path2};
        TextR.setLineSeparatorFromArgs(args);
        assertArrayEquals(TextR.getLineSeparatorArg(), System.lineSeparator().getBytes());

        // Na constructor zou volgende root layout moeten bestaan:
        TextR controller = new TextR(args);
        FileBuffer buffer1 = new FileBuffer(path1);
        FileBuffer buffer2 = new FileBuffer(path2);
        ArrayList<Layout> leaves = new ArrayList<>();
        leaves.add(new LayoutLeaf(path1, true));
        leaves.add(new LayoutLeaf(path2, false));

        HorizontalLayoutNode node = new HorizontalLayoutNode(leaves);

        //assertTrue(controller.getRootLayout().equals(node));

    }

}
