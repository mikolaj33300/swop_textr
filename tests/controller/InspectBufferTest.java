package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ui.FileBufferView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class InspectBufferTest {

    @TempDir
    Path path1, path2;
    private final VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    private TextR textr1, textr2, textr3;

    @BeforeEach
    public void setVariables() throws IOException {
        Path a = path1.resolve("test1.txt");
        Files.write(a, "mister".getBytes());
        Path b = path2.resolve("test2.txt");
        Files.write(b, "mister2\nhello".getBytes());
        textr1 = new TextR(new String[] {"--lf", a.toString()}, adapter);
        textr2 = new TextR(new String[] {"--lf", b.toString()}, adapter);
        textr3 = new TextR(new String[] {"--lf", a.toString(), b.toString()}, adapter);
    }

    /// Line separator \n gebruikt. Dus de test zijn enkel relevant op mac.

    @Test
    public void testMoveCursorUp_OneLine() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('A');
        haltLoop();

        // Loop the program: will read the move cursor command & stop the loop after
        textr1.loop();

        // Assert that the active view is a FileBufferView so we can retrieve the context
        assertInstanceOf(
                FileBufferView.class,
                textr1.facade.getWindows().get(textr1.facade.getActive()).view
        );
        FileBufferView view = (FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view;

        // Test if the move cursor worked logically
        assertEquals(view.cursorContext().getInsertionPointLine(), 0);
        assertEquals(view.cursorContext().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorUp_TwoLine() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('B');
        moveCursor('A');
        haltLoop();

        // Loop the program: will read the move cursor command & stop the loop after
        textr1.loop();

        // Assert that the active view is a FileBufferView so we can retrieve the context
        assertInstanceOf(
                FileBufferView.class,
                textr1.facade.getWindows().get(textr1.facade.getActive()).view
        );
        FileBufferView view = (FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view;

        // Test if the move cursor worked logically
        assertEquals(view.cursorContext().getInsertionPointLine(), 0);
        assertEquals(view.cursorContext().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorDown_OneLine() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('B');
        haltLoop();

        // Loop the program: will read the move cursor command & stop the loop after
        textr1.loop();

        // Assert that the active view is a FileBufferView so we can retrieve the context
        assertInstanceOf(
                FileBufferView.class,
                textr1.facade.getWindows().get(textr1.facade.getActive()).view
        );
        FileBufferView view = (FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view;

        // Test if the move cursor worked logically
        assertEquals(view.cursorContext().getInsertionPointLine(), 0);
        assertEquals(view.cursorContext().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorDown_TwoLine() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('B');
        haltLoop();

        // Loop the program: will read the move cursor command & stop the loop after
        textr2.loop();

        // Assert that the active view is a FileBufferView so we can retrieve the context
        assertInstanceOf(
                FileBufferView.class,
                textr2.facade.getWindows().get(textr2.facade.getActive()).view
        );
        FileBufferView view = (FileBufferView) textr2.facade.getWindows().get(textr2.facade.getActive()).view;

        // Test if the move cursor worked logically
        assertEquals(view.cursorContext().getInsertionPointLine(), 1);
        assertEquals(view.cursorContext().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorRight() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('C');
        haltLoop();

        // Loop the program: will read the move cursor command & stop the loop after
        textr1.loop();

        // Assert that the active view is a FileBufferView so we can retrieve the context
        assertInstanceOf(
                FileBufferView.class,
                textr1.facade.getWindows().get(textr1.facade.getActive()).view
        );
        FileBufferView view = (FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view;

        // Test if the move cursor worked logically
        assertEquals(view.cursorContext().getInsertionPointLine(), 0);
        assertEquals(view.cursorContext().getInsertionPointCol(), 1);

    }

    @Test
    public void testMoveCursorLeft_Start() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('B');
        haltLoop();

        // Loop the program: will read the move cursor command & stop the loop after
        textr1.loop();
        // Assert that the active view is a FileBufferView so we can retrieve the context
        assertInstanceOf(
                FileBufferView.class,
                textr1.facade.getWindows().get(textr1.facade.getActive()).view
        );
        FileBufferView view = (FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view;

        // Test if the move cursor worked logically
        assertEquals(view.cursorContext().getInsertionPointLine(), 0);
        assertEquals(view.cursorContext().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorLeft_NotStart() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('C');
        moveCursor('C');
        moveCursor('C');
        moveCursor('D');
        haltLoop();

        // Loop the program: will read the move cursor command & stop the loop after
        textr1.loop();
        // Assert that the active view is a FileBufferView so we can retrieve the context
        assertInstanceOf(
                FileBufferView.class,
                textr1.facade.getWindows().get(textr1.facade.getActive()).view
        );
        FileBufferView view = (FileBufferView) textr1.facade.getWindows().get(textr1.facade.getActive()).view;

        // Test if the move cursor worked logically
        assertEquals(view.cursorContext().getInsertionPointLine(), 0);
        assertEquals(view.cursorContext().getInsertionPointCol(), 2);

    }

    @Test
    public void testPreviousNext_OneBuffer() throws IOException {
        assertEquals(textr1.facade.getActive(), 0);
        focusNext();
        haltLoop();
        textr1.loop();
        assertEquals(textr1.facade.getActive(), 0);
    }


    @Test
    public void testPreviousNext_TwoBuffer() throws IOException {
        assertEquals(textr3.facade.getActive(), 0);
        focusNext();
        haltLoop();
        textr3.loop();
        assertEquals(textr3.facade.getActive(), 1);
    }

    @Test
    public void testPreviousNext_DoubleNext() throws IOException {
        assertEquals(textr3.facade.getActive(), 0);
        focusNext();
        focusNext();
        haltLoop();
        textr3.loop();
        assertEquals(textr3.facade.getActive(), 1);
    }

    @Test
    public void testPreviousNext_NextPevious() throws IOException {
        assertEquals(textr3.facade.getActive(), 0);
        focusNext();
        focusPrevious();
        haltLoop();
        textr3.loop();
        assertEquals(textr3.facade.getActive(), 0);
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
