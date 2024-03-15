package core;

import files.FileHolder;
import layouttree.DIRECTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditBufferTest {


    TextR c;
    private final String root = "testresources/";
    private final String path1 = root + "test.txt";
    private final String path2 = root + "test2.txt";
    private final String path3 = root + "test3.txt";

    @BeforeEach
    public void setVariables() {
        write(path1, "ai lov yousing termios");
        write(path2, "btj is a mister");
        write(path3, "btj makes great libraries");
        c = new TextR(new String[] {path1, path2, path3});
    }

    @Test
    public void testEditBuffer() throws IOException {
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
                ,Files.readAllBytes(Path.of(path1))));
        assertTrue(FileHolder.areContentsEqual(
                "bbtj is a mister".getBytes()
                ,Files.readAllBytes(Path.of(path2))));
        assertTrue(FileHolder.areContentsEqual(
                "cbtj makes great libraries".getBytes()
                ,Files.readAllBytes(Path.of(path3))));


    }

    /**
     * Helper method that writes given text into the file at given path
     */
    private void write(String path, String text) {
        try {
            // Overwrite file test.txt
            FileWriter writer = new FileWriter(new File(path));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
