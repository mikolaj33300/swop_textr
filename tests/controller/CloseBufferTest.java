package controller;

import controller.adapter.VirtualTestingTermiosAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ui.FileBufferView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CloseBufferTest {

    @TempDir
    Path path1, path2;
    private final VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    TextR textr1, textr2;

    @BeforeEach
    public void setVariables() throws IOException {
        path1 = path1.resolve("test1.txt");
        Files.write(path1, "i am a mister\n ; but you can call me mister TEE".getBytes());
        path2 = path2.resolve("test2.txt");
        Files.write(path2, "i love eating kaas\n ; kaas is my favourite\n; also using termios on a daily basis".getBytes());
        textr1 = new TextR(new String[] {"--lf",path1.toString()}, adapter);
        textr2 = new TextR(new String[] {"--lf",path1.toString(), path2.toString()}, adapter);
    }

    @Test
    public void closeNonDirty_OneFile() {
        // Assert non dirty
        assertFalse(
                ((FileBufferView) textr1.facade.getWindows().get(0).getView()).getBufferCursorContext().getDirty()
        );
        // Assert that closing gives no error, but since this is the last window, it will return 2
        assertEquals(2, textr1.facade.closeActive());
        // This will return 1, the window is not deleted, but the application is closed
        assertEquals(1, textr1.facade.getWindows().size());
    }

    @Test
    public void closeNonDirty_TwoPlusFile() {
        // Assert non dirty
        assertFalse(
                ((FileBufferView) textr2.facade.getWindows().get(0).getView()).getBufferCursorContext().getDirty()
        );
        // Assert that closing gives no error, but since this is the last window, it will return 2
        assertEquals(0, textr2.facade.closeActive());
        assertEquals(1, textr2.facade.getWindows().size());
    }

    @Test
    public void closeDirty_OneFile() throws IOException {
        enterCharacter('a');
        haltLoop();
        textr1.loop();
        // Assert dirty
        assertTrue(
                ((FileBufferView) textr1.facade.getWindows().get(0).getView()).getBufferCursorContext().getDirty()
        );
        // Assert that closing gives no error -> not dirty
        assertEquals(1, textr1.facade.closeActive());
        // Windows stay the same, only textR will handle this popup
        assertEquals(1, textr1.facade.getWindows().size());
    }

    @Test
    public void closeDirty_OneFile_HandlePopup() throws IOException {
        enterCharacter('a');
        adapter.putByte(27);
        adapter.putByte(10);
        adapter.putByte((int)'S');
        enterCharacter('n');
        haltLoop();
        textr1.loop();
        // Assert dirty
        assertTrue(
                ((FileBufferView) textr1.facade.getWindows().get(0).getView()).getBufferCursorContext().getDirty()
        );
        // Assert that closing gives no error -> not dirty
        assertEquals(1, textr1.facade.closeActive());
        // handled Y
        assertEquals(1, textr1.facade.getWindows().size());
    }

    private void enterCharacter(char character) {
        adapter.putByte((int) character);
    }

    private void focusNext() {
        adapter.putByte(14);
    }

    public void focusPrevious() {
        adapter.putByte(16);
    }

    private void haltLoop() {
        adapter.putByte(-2);
    }

}
