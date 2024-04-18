package controller;

import files.BufferCursorContext;
import ui.FileBufferView;
import ui.View;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertSame;

public class WindowTest {
    @TempDir
    Path path1;

    @BeforeEach
    public void setVariables() throws IOException {
        path1 = path1.resolve("test");
    }
    @Test
    void testConstructor() throws IOException {
        String path = path1.toString();
        BufferCursorContext bufferCursorContext = new BufferCursorContext(path, new byte[]{0x0d, 0x0a});
        FileBufferInputHandler handler = new FileBufferInputHandler(bufferCursorContext);
        VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(20, 10, new ArrayList<>());

        View view = new FileBufferView(bufferCursorContext, adapter);
        Window window = new Window(view, handler);
        assertSame(window.view, view);
        assertSame(window.handler, handler);

    }
}
