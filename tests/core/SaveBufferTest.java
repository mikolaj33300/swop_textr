package core;

import files.FileHolder;
import layouttree.DIRECTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Debug;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveBufferTest {

    TextR c;
    private final String root = "testresources/";
    private final String path1 = root + "test.txt";
    private final String path2 = root + "test2.txt";
    private final String path3 = root + "test3.txt";

    @BeforeEach
    public void setVariables() {
        Debug.write(path1, "ai lov yousing termios");
        Debug.write(path2, "kaas is a mister");
        Debug.write(path3, "kaas makes great libraries");
        c = new TextR(new String[] {path1, path2, path3, "noterminal"});
    }

    /**
     * This test asserts that switching between views, entering text via controller and saving the buffer
     * works. The buffer contents are correctly saved to disk.
     */
    @Test
    public void testMoveFocus() throws IOException {

        // We schrijven a op plaats 0 in path1.
        TextR.setLineSeparatorFromArgs(new String[] {path1, path2, path3});
        byte b = (Integer.valueOf(97)).byteValue();

        c.enterText(b);
        c.rootLayout.saveActiveBuffer();

        b = (Integer.valueOf(98)).byteValue();
        c.moveFocus(DIRECTION.RIGHT);
        c.enterText(b);
        c.rootLayout.saveActiveBuffer();

        b = (Integer.valueOf(99)).byteValue();
        c.moveFocus(DIRECTION.RIGHT);
        c.enterText(b);
        c.rootLayout.saveActiveBuffer();

        assertTrue(FileHolder.areContentsEqual(
                "aai lov yousing termios".getBytes()
                , Files.readAllBytes(Path.of(path1))));
        assertTrue(FileHolder.areContentsEqual(
                "bkaas is a mister".getBytes()
                ,Files.readAllBytes(Path.of(path2))));
        assertTrue(FileHolder.areContentsEqual(
                "ckaas makes great libraries".getBytes()
                ,Files.readAllBytes(Path.of(path3))));

    }

}
