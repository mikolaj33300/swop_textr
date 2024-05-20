package controller;

import controller.adapter.VirtualTestingTermiosAdapter;
import files.BufferCursorContext;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ui.FileBufferView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisplayFacadeTest {
    @TempDir
    Path path1;

    @BeforeEach
    public void setVariables() throws IOException {
        path1 = path1.resolve("test");
        Files.write(path1, "mister".getBytes());
    }

    @Test
    public void testDisplaysDuped() throws IOException {
        String path = path1.toString();
        BufferCursorContext bufferCursorContext = new BufferCursorContext(path, new byte[]{0x0d, 0x0a});
        FileBufferInputHandler handler = new FileBufferInputHandler(bufferCursorContext);
        VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1200, 10, new ArrayList<>());
        FileBufferView view = new FileBufferView(bufferCursorContext, adapter);
        FileBufferWindow window = new FileBufferWindow(view, handler);

        DisplayFacade newDisplay = new DisplayFacade(window, adapter, "--lf".getBytes());
        newDisplay.paintScreen();
        assertEquals(adapter.getVirtualScreen().getFirst()[0], 'm');
    }

}
