package controller.adapter;

import controller.ASCIIKeyEventListener;
import util.Coords;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface TermiosTerminalAdapter {
    public void clearScreen();

    public void enterRawInputMode();

    public void leaveRawInputMode();

    public void moveCursor(int row, int column);

    public void printText(int row, int column, String text);

    public int readByte() throws IOException;

    public int readByte(long deadline) throws IOException, TimeoutException;

    public Coords getTextAreaSize() throws IOException;

    public void subscribeToResizeTextArea(ResizeListener l);

    public void setInputListener(Runnable runnable);

    void clearInputListener();
}

