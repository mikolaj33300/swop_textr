package ui;

import ioadapter.TermiosTerminalAdapter;
import ioadapter.VirtualTestingTermiosAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snake.SnakeGame;

import java.util.ArrayList;

public class SnakeViewTest {

    SnakeGame game;
    SnakeView view;
    TermiosTerminalAdapter adapter;

    private SnakeView getAdapter(ArrayList<Integer> input, SnakeGame game) {
        return new SnakeView(game, new VirtualTestingTermiosAdapter(50, 50, input));
    }

    @BeforeEach
    public void init() {
        game = new SnakeGame(5, 50, 50);
    }

    @Test
    public void TestSnakePrintedCorrectly() {


    }



}
