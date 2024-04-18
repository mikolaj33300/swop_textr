package controller;

import files.BufferCursorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import ui.FileBufferView;
import ui.View;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertSame;

public class WindowTest {
    @TempDir
    Path path1;

    @BeforeEach
    public void setVariables() throws IOException {
        path1 = path1.resolve("test");
        Files.write(path1, "mister".getBytes());
    }
    @Test
    void testConstructor() throws IOException {
        String path = path1.toString();
        BufferCursorContext bufferCursorContext = new BufferCursorContext(path, new byte[]{0x0d, 0x0a});
        FileBufferInputHandler handler = new FileBufferInputHandler(bufferCursorContext);
        VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1200, 10, new ArrayList<>());

        View view = new FileBufferView(bufferCursorContext, adapter);
        Window window = new Window(view, handler);
        assertSame(window.view, view);
        assertSame(window.handler, handler);

    }
}
