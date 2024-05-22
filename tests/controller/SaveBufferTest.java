package controller;

import ioadapter.VirtualTestingTermiosAdapter;
import files.FileHolder;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveBufferTest {

    @TempDir
    Path path1, path2;
    private final VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    private final VirtualTestingTermiosAdapter adapter2 = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());

    private final VirtualTestingTermiosAdapter adapter3 = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());

    private TextR textr1, textr2, textr3;

    @BeforeEach
    public void setVariables() throws IOException {
        Path a = path1.resolve("test1.txt");
        Files.write(a, "i am a mister\n ; but you can call me mister TEE".getBytes());
        Path b = path2.resolve("test2.txt");
        Files.write(b, "i love eating kaas\n ; kaas is my favourite\n; also using termios on a daily basis".getBytes());
        textr1 = new TextR(new String[] {"--lf", a.toString()}, adapter);
        textr2 = new TextR(new String[] {"--lf", b.toString()}, adapter2);
        textr3 = new TextR(new String[] {"--lf", a.toString(), b.toString()}, adapter3);
    }

    /**
     * This test asserts that switching between views, entering text via controller and saving the buffer
     * works. The buffer contents are correctly saved to disk.
     */
    @Test
    public void testGetsDirty() throws IOException {
        enterCharacter('b');

        assertTrue(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        );
    }

    @Test
    public void testSaveContentsToFile() throws IOException {
        enterCharacter('b');
        adapter.putByte(19); // Ctrl + S
        triggerStdinEventFirstAdapter();
        assertFalse(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        );
        assertTrue(FileHolder.areContentsEqual(Files.readAllBytes(path1.resolve("test1.txt")), "bi am a mister\n ; but you can call me mister TEE".getBytes()));
    }


    private void enterCharacter(char character) {
        adapter.putByte((int) character);
        triggerStdinEventFirstAdapter();
    }

    private void focusNext() {
        adapter.putByte(14);
        triggerStdinEventFirstAdapter();
    }

    public void focusPrevious() {
        adapter.putByte(16);
        triggerStdinEventFirstAdapter();
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
        triggerStdinEventFirstAdapter();
    }

    private void triggerStdinEventFirstAdapter(){
        adapter.runStdinListener();
    }

}
