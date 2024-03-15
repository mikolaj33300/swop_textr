package core;

import files.FileHolder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class LaunchTextrTest {

    private final String root = "testresources/";
    private final String path1 = root + "test.txt";
    private final String path2 = root + "test2.txt";
    private final String path3 = root + "test3.txt";

    @Test
    public void testLaunch() throws IOException, NoSuchFieldException, IllegalAccessException {

        // Test empty input arguments
        String[] args = new String[] {};
        String[] finalArgs = args;
        assertThrows(RuntimeException.class, () -> TextR.main(finalArgs));

        // Test no path but --lf input
        args = new String[] {"--lf"};
        String[] finalArgs1 = args;
        assertThrows(RuntimeException.class, () -> TextR.main(finalArgs1));

        // Test no path but -crlf input
        args = new String[] {"--crlf"};
        String[] finalArgs2 = args;
        assertThrows(RuntimeException.class, () -> TextR.main(finalArgs2));

        // Testing -crlf recognition
        args = new String[] {"--crlf", path2, path3, "noterminal"};
        String[] finalArgs3 = args;
        assertDoesNotThrow(() -> TextR.main(finalArgs3));

        // Testing -crlf result
        // It's reflection time.
        Field field = TextR.class.getDeclaredField("lineSeparatorArg");
        field.setAccessible(true);
        byte[] lineSeparator = (byte[]) field.get(null); // pass null for static fields
        assertTrue(FileHolder.areContentsEqual(new byte[]{0x0d, 0x0a}, lineSeparator));

        // Testing lf recognition
        args = new String[] {"--lf", path2, path3, "noterminal"};
        String[] finalArgs4 = args;
        assertDoesNotThrow(() -> TextR.main(finalArgs4));

        // Testing result
        // It's reflection time.
        field = TextR.class.getDeclaredField("lineSeparatorArg");
        field.setAccessible(true);
        lineSeparator = (byte[]) field.get(null); // pass null for static fields
        assertTrue(FileHolder.areContentsEqual(new byte[]{0x0a}, lineSeparator));

        //Controller c = new Controller(new String[] {"test.txt"});
        //c.enterText(Integer.valueOf(170).byteValue());
        //assertThrows(IOException.class, () -> c.getRootLayout().saveActiveBuffer());
    }

}
