package controller;

import controller.adapter.SwingTerminalAdapter;
import controller.adapter.VirtualTestingTermiosAdapter;
import files.BufferCursorContext;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ui.FileBufferView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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
