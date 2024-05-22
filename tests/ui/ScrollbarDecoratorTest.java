package ui;

import ioadapter.VirtualTestingTermiosAdapter;
import files.BufferCursorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.Rectangle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScrollbarDecoratorTest {

    FileBufferView filebufferview;

    BufferCursorContext bufferCursorContext;
    VirtualTestingTermiosAdapter adapter;

    @TempDir
    Path path1;

    String content1 = "termios this termios that, you get no say in it";

    @BeforeEach
    public void setUp() throws IOException {
        path1 = path1.resolve("test1.txt");
        Files.write(path1, content1.getBytes());
        bufferCursorContext = new BufferCursorContext(path1.toString(), System.lineSeparator().getBytes());
        adapter = new VirtualTestingTermiosAdapter(150, 10, new ArrayList<Integer>(0));
        filebufferview = new FileBufferView(bufferCursorContext, adapter);
    }

    @Test
    public void testRender() throws IOException {
        View toTestFBView = new FileBufferView(bufferCursorContext, adapter);
        toTestFBView = new ScrollbarDecorator(toTestFBView);
        toTestFBView.setRealCoords(new Rectangle(0,0,150,5));
        toTestFBView.render(toTestFBView.hashCode());

    }

}






















