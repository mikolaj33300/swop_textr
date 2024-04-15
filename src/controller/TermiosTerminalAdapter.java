package controller;

public interface TermiosTerminalAdapter {
    public void clearScreen();

    public void enterRawInputMode();

    public void leaveRawInputMode();

    public void moveCursor(int row, int column);

    public void printText(int row, int column, String text);

    public int readByte();

    public int readByte(long deadline);

    public void reportTextAreaSize();

}
