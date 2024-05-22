package ui;

import ioadapter.TermiosTerminalAdapter;
import ioadapter.VirtualTestingTermiosAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snake.SnakeGame;

import java.util.ArrayList;

public class SwingEditableTerminalAppTest {


    @Test
    public void TestSwingEditableTerminalPrintsText() {
        SwingEditableTerminalApp newApp = new SwingEditableTerminalApp();
        newApp.setVisible(true);
        newApp.updateBuffer("Pain", 0, 0);
        while(true);

    }
}
