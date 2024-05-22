package controller;

import controller.adapter.VirtualTestingTermiosAdapter;
import files.FileHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LaunchTextrTest {

    @TempDir
    Path path1, path2;

    private final VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    private TextR textr1, textr2, textr3;

    @BeforeEach
    public void setVariables() throws IOException {
        path1 = path1.resolve("test1.txt");
        Files.write(path1, "mister".getBytes());
        path2 = path2.resolve("test2.txt");
        Files.write(path2, "mister2\nhello".getBytes());
        textr1 = new TextR(new String[] {"--lf", path1.toString()}, adapter);
        textr2 = new TextR(new String[] {"--lf", path1.toString()}, adapter);
        textr3 = new TextR(new String[] {"--lf", path1.toString(), path2.toString()}, adapter);

    }

    @Test
    public void testNoArguments() {
        assertThrows(Exception.class, () -> new TextR(new String[] {}, adapter));
    }

    @Test
    public void testNoPaths() {
        assertThrows(Exception.class, () -> new TextR(new String[] {"--lf"}, adapter));
        assertThrows(Exception.class, () -> new TextR(new String[] {"--crlf"}, adapter));
    }

    @Test
    public void testInvalidPath() {
        assertThrows(Exception.class, () -> new TextR(new String[]{"invalidpath.btj"}, adapter));
    }

    @Test
    public void testPathsAndLineSeparator() {
        assertDoesNotThrow(() -> new TextR(new String[] {"--lf", path1.toString()}, adapter));
        assertDoesNotThrow(() -> new TextR(new String[] {"--crlf", path1.toString()}, adapter));
    }

    @Test
    public void testLineSeparatorAssignment() {
        assertTrue(FileHolder.areContentsEqual(textr1.getActiveUseCaseController().getFacade().getLineSeparatorArg(), new byte[] {0x0a}));
        assertTrue(FileHolder.areContentsEqual(textr2.getActiveUseCaseController().getFacade().getLineSeparatorArg(), new byte[] {0x0a}));
    }

    @Test
    public void testWindowsOpened() {
        assertEquals(textr1.getActiveUseCaseController().getFacade().getWindows().size(), 1);
        assertEquals(textr3.getActiveUseCaseController().getFacade().getWindows().size(), 2);
    }

}
