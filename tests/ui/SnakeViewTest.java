package ui;

import ioadapter.VirtualTestingTermiosAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snake.SnakeGame;
import util.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SnakeViewTest {

    final int maxX = 25;
    final int maxY = 25;
    SnakeGame game;
    SnakeView view;
    VirtualTestingTermiosAdapter adapter;

    @BeforeEach
    public void init() {
        adapter = new VirtualTestingTermiosAdapter(50, 50, new ArrayList<>());
        game = new SnakeGame(5, maxX, maxY);
        this.view = new SnakeView(game, adapter);
        this.view.setRealCoords(new Rectangle(0, 0, maxX, maxY));
    }

    @Test
    public void TestSnakePrintedCorrectly() throws IOException {
        this.view.render(0);

        assertEquals(adapter.getVirtualScreen().get(maxY/2)[(maxX/2)-4], 'd');
        assertEquals(adapter.getVirtualScreen().get(maxY/2)[(maxX/2)-3], 'c');
        assertEquals(adapter.getVirtualScreen().get(maxY/2)[(maxX/2)-2], 'b');
        assertEquals(adapter.getVirtualScreen().get(maxY/2)[(maxX/2)-1], 'a');
        assertEquals(adapter.getVirtualScreen().get(maxY/2)[(maxX/2)], '>');
    }

    @Test
    public void testLineLength_Zero() {
        assertEquals(this.view.getLineLength(1), 0);
    }

    @Test
    public void testEquals_IrrelevantObject() {
        assertFalse(view.equals(new String("hello misters")));
    }

    @Test
    public void testRenderCursor_NoEffect_ButBetterForCoverage() throws IOException {
        view.renderCursor();
    }

    @Test
    public void testContentHeight_Zero() {
        assertEquals(view.getTotalContentHeight(), 0);
    }

    @Test
    public void testFocusedLine_Zero() {
        assertEquals(view.getFocusedLine(), 0);
    }

    @Test
    public void testFocusedCol_Zero() {
        assertEquals(view.getFocusedCol(), 0);
    }

    @Test
    public void testPrintBox_CornerCorrect() {
        String content = "mister";
        this.view.printBox(2, content);
        int offset = maxX % 2 == 0 ? 0 : 1;

        assertEquals(
                String.valueOf(this.adapter.getVirtualScreen().get(2)[(maxX/2) - ((content.length()+2)/2) - offset]),
                "+"
        );

    }

    @Test
    public void testPrintBox_PipeCorrrect() {
        String content = "mister";
        this.view.printBox(2, content);
        int offset = maxX % 2 == 0 ? 0 : 1;

        assertEquals(
                String.valueOf(this.adapter.getVirtualScreen().get(2+1)[(maxX/2) - ((content.length()+2)/2) - offset]),
                "I"
        );

    }

    private ArrayList<Character> charArray(char[] arr) {
        ArrayList<Character> list = new ArrayList<>();
        for(char a : arr)
            list.add(a);
        return list;
    }


    private void logScreen() {
        System.out.println(adapter.getVirtualScreen().stream()
                .map(this::charArray)
                .map(entryArr -> entryArr.stream()
                        .map(a -> String.valueOf(a).trim().isEmpty() ? "." : "x")
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining("\n ; "))
        );
    }



}
