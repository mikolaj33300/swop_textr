package controller;

import ioadapter.SwingTerminalAdapter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwingTermiosAdapterTest {
    //Not unit test, this is for visual testing of whether we handle swing correctly

    @Test
    public void testDisplayedSwing() throws IOException {
        SwingTerminalAdapter adapterToTest = new SwingTerminalAdapter();
        adapterToTest.printText( 1, 1, "aaaaaaaaaaa");
        /*while(true){
            //this is so the window doesn't immediately close but the program doesn't have other stuff to do
        }*/
    }
}
