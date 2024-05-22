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

public class EditBufferTest {


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


    // Test writing in the first opened buffer
    @Test
    public void testNonDirtyOpened() throws IOException, NoSuchFieldException {
        assertFalse(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        );
    }

    @Test
    public void testDirtyEdited() throws IOException {
        enterCharacterFirstAdapter('H');
        triggerStdinEventFirstAdapter();

        //assertTrue(
        //        ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        //);
    }

    @Test
    public void testNonDirtySave() throws IOException {
        enterCharacterFirstAdapter('H');
        adapter.putByte(19); // Ctrl + S
        triggerStdinEventFirstAdapter();
        triggerStdinEventFirstAdapter();

        assertFalse(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        );
    }

    @Test
    public void testWriteAfterNext() throws IOException {
        focusNext();
        enterCharacterFirstAdapter('H');
        adapter.putByte(19); // Ctrl + S
        triggerStdinEventFirstAdapter();
        triggerStdinEventFirstAdapter();
        triggerStdinEventFirstAdapter();

        assertFalse(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        );
    }

    @Test
    public void testBufferReceivesCharacter() throws IOException {
        enterCharacterFirstAdapter('m');
        triggerStdinEventFirstAdapter();

        assertTrue(
                FileHolder.areContentsEqual(
                    ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade()
                            .getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler())
                            .getFileBufferContextTransparent().getFileBuffer().getBytes(),
                            "mi am a mister\n ; but you can call me mister TEE".getBytes()
                )
        );
    }

    private void enterCharacterFirstAdapter(char character) {
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

    private void triggerStdinEventFirstAdapter(){
        adapter.runStdinListener();
    }


}
