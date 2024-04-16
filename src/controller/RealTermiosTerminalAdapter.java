package controller;

import io.github.btj.termios.Terminal;

import java.io.IOException;

public class RealTermiosTerminalAdapter implements TermiosTerminalAdapter{
    @Override
    public void clearScreen() {
        Terminal.clearScreen();
    }

    @Override
    public void enterRawInputMode() {
        Terminal.enterRawInputMode();
    }

    @Override
    public void leaveRawInputMode() {
        Terminal.leaveRawInputMode();
    }

    @Override
    public void moveCursor(int row, int column) {
        Terminal.moveCursor(row, column);
    }

    @Override
    public void printText(int row, int column, String text) {
        Terminal.printText(row, column, text);
    }

    @Override
    public int readByte() throws IOException {
        return Terminal.readByte();
    }

    @Override
    public int readByte(long deadline) throws IOException {
        //TODO Update to newest termios
        return Terminal.readByte();
    }

    @Override
    public void reportTextAreaSize() {
        Terminal.reportTextAreaSize();
    }
}
