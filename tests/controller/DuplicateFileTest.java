package controller;

import ioadapter.VirtualTestingTermiosAdapter;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        textr1 = new TextR(new String[] {"--lf", a.toString()}, adapter);
        enterDuplicateCtrl();
        assertEquals( textr1.getActiveUseCaseController().getFacade().getWindows().size(), 2);
    }

    private void enterDuplicateCtrl() {
        adapter.putByte((int) 4);
        triggerStdinEventFirstAdapter();

    }

    private void triggerStdinEventFirstAdapter(){
        adapter.runStdinListener();
    }
}
