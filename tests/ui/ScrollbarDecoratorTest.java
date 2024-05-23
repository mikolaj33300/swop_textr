package ui;

import files.BufferCursorContext;
import ioadapter.RealTermiosTerminalAdapter;
import ioadapter.TermiosTerminalAdapter;
import ioadapter.VirtualTestingTermiosAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.Rectangle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ScrollbarDecoratorTest {

    FileBufferView filebufferview;
    ScrollbarDecorator decorator;

    BufferCursorContext bufferCursorContext;
    VirtualTestingTermiosAdapter adapter;

    @TempDir
    Path path1, path2;

    String content1 = "termios this termios that, you get no say in it\nand also termios is king in terminal applications\nand also, i love the maker of termios";
    String content2 = "my favourite library is not even fully released (termios)";

    @BeforeEach
    public void setUp() throws IOException {
        path1 = path1.resolve("test1.txt");
        Files.write(path1, content1.getBytes());
        path2 = path2.resolve("test2.txt");
        Files.write(path2, content2.getBytes());
        bufferCursorContext = new BufferCursorContext(path1.toString(), "\n".getBytes());
        adapter = new VirtualTestingTermiosAdapter(150, 10, new ArrayList<Integer>(0));
        filebufferview = new FileBufferView(bufferCursorContext, adapter);
        decorator = new ScrollbarDecorator(filebufferview);
    }

    @Test
    public void testRender() throws IOException {
        View toTestFBView = new FileBufferView(bufferCursorContext, adapter);
        toTestFBView = new ScrollbarDecorator(toTestFBView);
        toTestFBView.setRealCoords(new Rectangle(0,0,150,5));
        toTestFBView.render(toTestFBView.hashCode());

    }

    @Test
    public void testSetAdapter() {
        ScrollbarDecorator decorator = new ScrollbarDecorator(this.filebufferview);
        decorator.setTermiosTerminalAdapter(new RealTermiosTerminalAdapter());
        assertInstanceOf(RealTermiosTerminalAdapter.class, decorator.getTermiosTerminalAdapter());
        decorator.setTermiosTerminalAdapter(new VirtualTestingTermiosAdapter(20, 20, new ArrayList<>()));
        assertInstanceOf(VirtualTestingTermiosAdapter.class, decorator.getTermiosTerminalAdapter());
    }

    @Test
    public void testFocusedLine_NoMovement_Zero() {
        assertEquals(decorator.getFocusedLine(), 0);
    }

    @Test
    public void testFocusedLine_MovementRight_Zero() {
        bufferCursorContext.moveCursorRight();
        assertEquals(decorator.getFocusedLine(), 0);
    }

    @Test
    public void testFocusedLine_MovementLeft_Zero() {
        bufferCursorContext.moveCursorLeft();
        assertEquals(decorator.getFocusedLine(), 0);
    }

    @Test
    public void testFocusedLine_MovementUp_Zero_Minimum() {
        bufferCursorContext.moveCursorUp();
        assertEquals(decorator.getFocusedLine(), 0);
    }

    @Test
    public void testFocusedLine_MovementUp_NonZero_NonMinimum() {
        bufferCursorContext.moveCursorDown();
        bufferCursorContext.moveCursorDown();
        bufferCursorContext.moveCursorUp();
        assertEquals(decorator.getFocusedLine(), 1);
    }

    @Test
    public void testFocusedLine_MovementDown_NonZero() {
        bufferCursorContext.moveCursorDown();
        assertEquals(decorator.getFocusedLine(), 1);
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(decorator.equals(decorator));
    }

    @Test
    public void testEquals_NotSameObject() {
        assertFalse(decorator.equals(new String("hello")));
    }

    @Test
    public void testEquals_NewObject_NotSameView() throws IOException {
        BufferCursorContext bufferCursorContext2 = new BufferCursorContext(path2.toString(), "\n".getBytes());
        TermiosTerminalAdapter adapter = new VirtualTestingTermiosAdapter(150, 10, new ArrayList<Integer>(0));
        View filebufferview = new FileBufferView(bufferCursorContext2, adapter);
        ScrollbarDecorator decorator = new ScrollbarDecorator(filebufferview);
        assertFalse(this.decorator.equals(decorator));
    }

    @Test
    public void testLineLength() {
        assertEquals(this.decorator.getLineLength(0), "termios this termios that, you get no say in it".length());
    }

    @Test
    public void testFocusedCol_NoMovement_Zero() {
        assertEquals(this.decorator.getFocusedCol(), 0);
    }

    @Test
    public void testFocusedCol_LeftMovement_Zero() {
        bufferCursorContext.moveCursorLeft();
        assertEquals(this.decorator.getFocusedCol(), 0);
    }

    @Test
    public void testFocusedCol_LeftMovement_NonZero() {
        bufferCursorContext.moveCursorRight();
        bufferCursorContext.moveCursorRight();
        bufferCursorContext.moveCursorLeft();
        assertNotEquals(this.decorator.getFocusedCol(), 0);
    }

    @Test
    public void testFocusedCol_RightMovement_NonZero() {
        bufferCursorContext.moveCursorRight();
        assertNotEquals(this.decorator.getFocusedCol(), 0);
    }

    @Test
    public void testGetContentHeight() {
        assertEquals(this.decorator.getTotalContentHeight(), 3);
    }



}






















