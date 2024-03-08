package util;

import io.github.btj.termios.Terminal;

import java.io.IOException;

/**
 * Because of the smart implementation of BTJ, we have to create a
 * seperate class to read the terminal size. weLL aCtUaLLy wItH gRaSp yOu cAn ..SDFLJSDMFLKJEMFKJ
 * no. I do not agree.
 *
 * Anyway...
 *
 * Inspiration from BTJ himself: https://github.com/btj/termios/blob/main/src/test/java/TerminalTest.java
 */
public class TerminalReader {

    private boolean bufferFull = false;
    private int buffer;
    private int width, height;

    public TerminalReader() {
        try {
            this.recalculateDimensions();
        } catch(IOException e) {
            System.out.println("[TerminalReader] Error occured whilst calculating dimensions of terminal.");
            e.printStackTrace();
        }
    }

    /**
     * Returns the width of the terminal
     * Note: rerun for {@link TerminalReader#recalculateDimensions()} for accurate size
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the terminal
     * Note: rerun for {@link TerminalReader#recalculateDimensions()} for accurate size
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width and height in array form.
     */
    public void recalculateDimensions() throws IOException {
        Terminal.reportTextAreaSize();
        //Terminal.clearScreen();

        // \033
        peekByte();
        eatByte();
        // [
        peekByte();
        eatByte();
        // 8
        peekByte();
        eatByte();
        // ;
        peekByte();
        eatByte();
        this.height = getNumber();
        peekByte();
        eatByte();
        this.width = getNumber();
    }

    private int peekByte() throws IOException {
        if(!bufferFull) {
            buffer = Terminal.readByte();
            bufferFull = true;
        }
        return buffer;
    }

    private void eatByte() throws IOException {
        peekByte();
        bufferFull = false;
    }

    private int getNumber() throws IOException {
        int c = peekByte();
        int result = c - '0';
        eatByte();

        for ( ; ; ) {
            c = peekByte();
            if (c < '0' || '9' < c)
                break;
            eatByte();
            if (result > (Integer.MAX_VALUE - (c - '0')) / 10)
                return -1;

            result = result * 10 + (c - '0');
        }
        return result;
    }


}
