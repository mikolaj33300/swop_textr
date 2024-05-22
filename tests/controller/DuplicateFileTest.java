package controller;

import controller.adapter.VirtualTestingTermiosAdapter;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DuplicateFileTest {
    @TempDir
    Path path1, path2;
    private VirtualTestingTermiosAdapter adapter;
    private TextR textr1;

    Path a;

    @BeforeEach
    public void setVariables() throws IOException {
        a = path1.resolve("test1.txt");
        Files.write(a, "i am a mister\n ; but you can call me mister TEE".getBytes());
        adapter = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    }

    @Test
    public void testDisplaysDuped() throws IOException {
        adapter = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
        enterDuplicateCtrl();
        haltLoop();
        textr1 = new TextR(new String[] {"--lf", a.toString()}, adapter);
        textr1.startListenersAndHandlers();
        assertTrue(
                ((FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler()).getFileBufferContextTransparent().getDirty()
        );
    }

    private void enterDuplicateCtrl() {
        adapter.putByte((int) 4);
    }
    private void haltLoop() {
        adapter.putByte(-2);
    }
}
