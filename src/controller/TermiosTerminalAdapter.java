package controller;

import java.io.IOException;

public interface TermiosTerminalAdapter {
    public void clearScreen();

    public void enterRawInputMode();

    public void leaveRawInputMode();

    public void moveCursor(int row, int column);

    public void printText(int row, int column, String text);

    public int readByte() throws IOException;

    public int readByte(long deadline) throws IOException;

    public void reportTextAreaSize();

}
