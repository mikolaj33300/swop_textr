package controller;

import ioadapter.SwingTerminalAdapter;
import ioadapter.VirtualTestingTermiosAdapter;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class InspectBufferTest {

    @TempDir
    Path path1, path2, path3;
    private final VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());

    private final VirtualTestingTermiosAdapter adapter2 = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    private final VirtualTestingTermiosAdapter adapter3 = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());
    private final VirtualTestingTermiosAdapter adapter4 = new VirtualTestingTermiosAdapter(1000, 10, new ArrayList<>());

    private TextR textr1, textr2, textr3, textr4;

    @BeforeEach
    public void setVariables() throws IOException {
        Path a = path1.resolve("test1.txt");
        Files.write(a, "mister".getBytes());
        Path b = path2.resolve("test2.txt");
        Files.write(b, "mister2\nhello".getBytes());

        Path c = path3.resolve("test3.txt");
        Files.write(c, """
				{
				  "Documents": {
				    "SWOP": {
				      "assignment_it2.txt": "This is the assignment for iteration 2.",
				      "assignment_it3.txt": "This is the assignment for iteration 3."
				    },
				    "json_in_string.json": "{\\r\\n  \\"foo\\": \\"bar\\"\\r\\n}"
				  }
				}""".getBytes());

        textr1 = new TextR(new String[] {"--lf", a.toString()}, adapter);
        textr2 = new TextR(new String[] {"--lf", b.toString()}, adapter2);
        textr3 = new TextR(new String[] {"--lf", a.toString(), b.toString()}, adapter3);
        //textr4 = new TextR(new String[] {"--lf", c.toString()}, new SwingTerminalAdapter());
    }

    /// Line separator \n gebruikt. Dus de test zijn enkel relevant op mac.

    @Test
    public void testMoveCursorUp_OneLine() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('A');


        // Assert that the active view is a FileBufferView so we can retrieve the context

        FileBufferInputHandler view = (FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler();

        // Test if the move cursor worked logically
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointLine(), 0);
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorUp_TwoLine() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('B');
        moveCursor('A');


        // Assert that the active view is a FileBufferView so we can retrieve the context
        FileBufferInputHandler view = (FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler();

        // Test if the move cursor worked logically
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointLine(), 0);
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorDown_OneLine() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('B');

        FileBufferInputHandler view = (FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler();

        // Test if the move cursor worked logically
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointLine(), 0);
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorDown_TwoLine() throws IOException {
        // Move cursor & let loop stop itself
        moveCursorSecond('B');


        FileBufferInputHandler view = (FileBufferInputHandler) textr2.getActiveUseCaseController().getFacade().getWindows().get(textr2.getActiveUseCaseController().getFacade().getActive()).getHandler();

        // Test if the move cursor worked logically
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointLine(), 1);
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorRight() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('C');

        // Assert that the active view is a FileBufferView so we can retrieve the context
        FileBufferInputHandler view = (FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler();

        // Test if the move cursor worked logically
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointLine(), 0);
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointCol(), 1);

    }

    @Test
    public void testMoveCursorLeft_Start() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('B');

        FileBufferInputHandler view = (FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler();

        // Test if the move cursor worked logically
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointLine(), 0);
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointCol(), 0);

    }

    @Test
    public void testMoveCursorLeft_NotStart() throws IOException {
        // Move cursor & let loop stop itself
        moveCursor('C');
        moveCursor('C');
        moveCursor('C');
        moveCursor('D');

        FileBufferInputHandler view = (FileBufferInputHandler) textr1.getActiveUseCaseController().getFacade().getWindows().get(textr1.getActiveUseCaseController().getFacade().getActive()).getHandler();

        // Test if the move cursor worked logically
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointLine(), 0);
        assertEquals(view.getFileBufferContextTransparent().getInsertionPointCol(), 2);

    }

    @Test
    public void testPreviousNext_OneBuffer() throws IOException {
        assertEquals(textr1.getActiveUseCaseController().getFacade().getActive(), 0);
        focusNext();
        assertEquals(textr1.getActiveUseCaseController().getFacade().getActive(), 0);
    }


    @Test
    public void testPreviousNext_TwoBuffer() throws IOException {
        assertEquals(textr3.getActiveUseCaseController().getFacade().getActiveDisplay().getActive(), 0);
        focusNextThird();
        assertEquals(textr3.getActiveUseCaseController().getFacade().getActiveDisplay().getActive(), 1);
    }

    @Test
    public void testPreviousNext_DoubleNext() throws IOException {
        assertEquals(textr3.getActiveUseCaseController().getFacade().getActiveDisplay().getActive(), 0);
        focusNextThird();
        focusNextThird();
        assertEquals(textr3.getActiveUseCaseController().getFacade().getActiveDisplay().getActive(), 1);
    }

    @Test
    public void testPreviousNext_NextPevious() throws IOException {
        assertEquals(textr3.getActiveUseCaseController().getFacade().getActive(), 0);
        focusNextThird();
        focusPreviousThird();
        assertEquals(textr3.getActiveUseCaseController().getFacade().getActive(), 0);
    }

    @Test
    public void SECRET_TEST_FOR_DEBUGGING() throws IOException {
    }

    private void focusNext() {
        adapter.putByte(14);
        triggerStdinEventFirstAdapter();
    }

    private void focusNextThird() {
        adapter3.putByte(14);
        triggerStdinEventThirdAdapter();
    }

    private void focusPreviousThird() {
        adapter3.putByte(16);
        triggerStdinEventThirdAdapter();
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

    private void moveCursorSecond(char dir) {
        adapter2.putByte(27);
        adapter2.putByte(10);
        adapter2.putByte((int) dir);
        triggerStdinEventSecondAdapter();
    }
    private void triggerStdinEventFirstAdapter(){
        adapter.runStdinListener();
    }

    private void triggerStdinEventSecondAdapter(){
        adapter2.runStdinListener();
    }

    private void triggerStdinEventThirdAdapter(){
        adapter3.runStdinListener();
    }

}
