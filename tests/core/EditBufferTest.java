package core;

import files.FileHolder;
import layouttree.DIRECTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Debug;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditBufferTest {


    Controller c;
    private final String root = "testresources/";
    private final String path1 = root + "test.txt";
    private final String path2 = root + "test2.txt";
    private final String path3 = root + "test3.txt";

    @BeforeEach
    public void setVariables() {
        Debug.write(path1, "ai lov yousing termios");
        Debug.write(path2, "btj is a mister");
        Debug.write(path3, "btj makes great libraries");
        c = new Controller(new String[] {path1, path2, path3});
    }

    /**
     * This test asserts that:
     * 1. Moving between views works
     * 2. The controller can insert text into the active file buffer
     * 3. Implicitly asserting that contents writting to the buffer are saved correctly,
     *    because saveActiveBuffer() writes the edited contents to disk.
     */
    @Test
    public void testEditBuffer() throws IOException, NoSuchFieldException {
        // We schrijven a op plaats 0 in path1.
        Controller.setLineSeparatorFromArgs(new String[] {path1, path2, path3});
        byte b = (Integer.valueOf(97)).byteValue();

        // Enter text at column 0.
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
                ,Files.readAllBytes(Path.of(path1))));
        assertTrue(FileHolder.areContentsEqual(
                "bbtj is a mister".getBytes()
                ,Files.readAllBytes(Path.of(path2))));
        assertTrue(FileHolder.areContentsEqual(
                "cbtj makes great libraries".getBytes()
                ,Files.readAllBytes(Path.of(path3))));


    }


}
