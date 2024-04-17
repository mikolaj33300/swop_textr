package controller;

import files.FileHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ui.FileBufferView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditBufferTest {


    @TempDir
    Path path1, path2;
    private final VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    private TextR textr1, textr2, textr3;

    @BeforeEach
    public void setVariables() throws IOException {
        Path a = path1.resolve("test1.txt");
        Files.write(a, "i am a mister\n ; but you can call me mister TEE".getBytes());
        Path b = path2.resolve("test2.txt");
        Files.write(b, "i love eating kaas\n ; kaas is my favourite\n; also using termios on a daily basis".getBytes());
        textr1 = new TextR(new String[] {"--crlf", a.toString()}, adapter);
        textr2 = new TextR(new String[] {"--lf", b.toString()}, adapter);
        textr3 = new TextR(new String[] {"--lf", a.toString(), b.toString()}, adapter);
    }


    // Test writing in the first opened buffer
    @Test
    public void testNonDirtyOpened() throws IOException, NoSuchFieldException {
        haltLoop();
        textr1.loop();
        assertFalse(
                ((FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view).cursorContext().getDirty()
        );
    }

    @Test
    public void testDirtyEdited() throws IOException {
        enterCharacter('H');
        haltLoop();
        textr1.loop();

        assertTrue(
                ((FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view).cursorContext().getDirty()
        );
    }

    @Test
    public void testNonDirtySave() throws IOException {
        enterCharacter('H');
        adapter.putByte(19); // Ctrl + S
        haltLoop();
        textr1.loop();

        assertFalse(
                ((FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view).cursorContext().getDirty()
        );
    }

    @Test
    public void testWriteAfterNext() throws IOException {
        focusNext();
        enterCharacter('H');
        adapter.putByte(19); // Ctrl + S
        haltLoop();
        textr1.loop();
        assertFalse(
                ((FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view).cursorContext().getDirty()
        );
    }

    @Test
    public void testBufferReceivesCharacter() throws IOException {
        enterCharacter('m');
        haltLoop();
        textr1.loop();
        assertTrue(
                FileHolder.areContentsEqual(
                    ((FileBufferView) textr1.facade
                            .getWindows().get(textr1.facade.getActive()).view)
                            .cursorContext().getFileBuffer().getBytes(),
                            "mi am a mister\n ; but you can call me mister TEE".getBytes()
                )
        );
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

    /**
     * A: up
     * B: down
     * C: right
     * D: left
     * @param dir the direction
     */
    private void moveCursor(char dir) {
        adapter.putByte(27);
        adapter.putByte(10);
        adapter.putByte((int) dir);
    }

    private void haltLoop() {
        adapter.putByte(-2);
    }


}
