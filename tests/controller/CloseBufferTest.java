package controller;

import ioadapter.VirtualTestingTermiosAdapter;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.GlobalCloseStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CloseBufferTest {

    @TempDir
    Path path1, path2;
    private final VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    private final VirtualTestingTermiosAdapter adapter2 = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    TextR textr1, textr2;

    @BeforeEach
    public void setVariables() throws IOException {
        path1 = path1.resolve("test1.txt");
        Files.write(path1, "i am a mister\n ; but you can call me mister TEE".getBytes());
        path2 = path2.resolve("test2.txt");
        Files.write(path2, "i love eating kaas\n ; kaas is my favourite\n; also using termios on a daily basis".getBytes());
        textr1 = new TextR(new String[] {"--lf",path1.toString()}, adapter);
        textr2 = new TextR(new String[] {"--lf",path1.toString(), path2.toString()}, adapter2);
    }

    @Test
    public void closeNonDirty_OneFile() {
        // Assert non dirty
        assertFalse(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(
                        textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()
                ).getFileBufferContextTransparent().getDirty()
        );
        // Assert that closing gives no error, but since this is the last window, it will return 2
        assertEquals(GlobalCloseStatus.CLOSED_ALL_DISPLAYS, textr1.getActiveUseCaseController().getFacade().closeActive().b);
        // This will return 1, the window is not deleted, but the application is closed
        assertEquals(1, textr1.getActiveUseCaseController().getFacade().getWindows().size());
    }

    @Test
    public void closeNonDirty_TwoPlusFile() {
        // Assert non dirty
        assertFalse(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        );
        // Assert that closing gives no error, but since this is the last window, it will return 2
        assertEquals(GlobalCloseStatus.CLOSED_SUCCESFULLY, textr2.getActiveUseCaseController().getFacade().closeActive().b);
        assertEquals(1, textr2.getActiveUseCaseController().getFacade().getWindows().size());
    }

    @Test
    public void closeDirty_OneFile() throws IOException {
        enterCharacter('a');
        // Assert dirty
        assertTrue(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        );
        // Assert that closing gives no error -> not dirty
        assertEquals(GlobalCloseStatus.DIRTY_CLOSE_PROMPT, textr1.getActiveUseCaseController().getFacade().closeActive().b);
        // Windows stay the same, only textR will handle this popup
        assertEquals(1, textr1.getActiveUseCaseController().getFacade().getWindows().size());
    }

    @Test
    public void closeDirty_OneFile_HandlePopup() throws IOException {
        enterCharacter('a');
        triggerStdinEventFirstAdapter();
        adapter.putByte(27);
        adapter.putByte(10);
        adapter.putByte((int)'S');
        enterCharacter('n');
        triggerStdinEventFirstAdapter();
        // Assert dirty
        assertTrue(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        );
        // Assert that closing gives no error -> not dirty
        assertEquals(GlobalCloseStatus.DIRTY_CLOSE_PROMPT, textr1.getActiveUseCaseController().getFacade().closeActive().b);
        // handled Y
        assertEquals(1, textr1.getActiveUseCaseController().getFacade().getWindows().size());
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

    private void triggerStdinEventFirstAdapter(){
        adapter.runStdinListener();
    }

}
