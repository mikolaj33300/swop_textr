package ioadapter;

import io.github.btj.termios.Terminal;
import util.ScreenUIUtil;
import util.Coords;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

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
    public int readByte(long deadline) throws IOException, TimeoutException {
        //TODO Update to newest termios
        return Terminal.readByte(deadline);
    }

    @Override
    public Coords getTextAreaSize() throws IOException {
        //return new Coords(0, 0, 50, 20);
        return ScreenUIUtil.retrieveDimensionsTerminal();
    }

    @Override
    public void subscribeToResizeTextArea(ResizeListener l) {
        //In assignment it is assumed this will never happen
    }

    @Override
    public void setInputListenerOnAWTEventQueue(Runnable runnable) {
        Terminal.setInputListener(new Runnable() {
            @Override
            public void run() {
                java.awt.EventQueue.invokeLater(() -> {
                    runnable.run();
                });
            }
        });
    }

    @Override
    public void clearInputListener() {
        Terminal.clearInputListener();
    }

    @Override
    public void subscribeToKeyPresses(ASCIIKeyEventListener newAsciiListener) {
        //TODO: We can use this API to subscribe to keypresses on real termios instead of setInputListenerOnAWTEventQueue.
        // Not sure if it's worth the risks associated with refactoring since this wouldn't provide much benefits other than
        // that this wouldn't have to be done in main
    }

    @Override
    public void unsubscribeFromKeyPresses(ASCIIKeyEventListener listenerToRemove) {

    }

    @Override
    public void unsubscribeFromResizeTextArea(ResizeListener l) {

    }

    @Override
    public void addAndStartTimerListener(int delay, ActionListener actionListener) {
        javax.swing.Timer timer = new javax.swing.Timer(delay, actionListener);
        timer.start();
    }
}
