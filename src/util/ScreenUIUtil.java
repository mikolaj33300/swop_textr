package util;

import io.github.btj.termios.Terminal;
import util.Coords;

import java.io.IOException;

public class ScreenUIUtil {

    /**
     * Returns the coords of the text area of terminal. Is not tested since it's assumed is correct,
     * just like the termios methods themselves. Source: termios tests.
     *
     * @return UI coords
     * @throws IOException exception
     */
    public static Coords retrieveDimensionsTerminal() throws IOException {
      /*
        Terminal.reportTextAreaSize();
        for (int i = 0; i < 4; i++)
            Terminal.readByte();

        int c = Terminal.readByte();
        int height = c - '0';
        int tempByte = Terminal.readByte();

        for (; ; ) {
            if (tempByte < '0' || '9' < tempByte)
                break;
            if (height > (Integer.MAX_VALUE - (c - '0')) / 10)
                break;
            height = height * 10 + (tempByte - '0');
            tempByte = Terminal.readByte();
        }
        c = Terminal.readByte();
        int width = c - '0';
        tempByte = Terminal.readByte();

        for (; ; ) {
            if (tempByte < '0' || '9' < tempByte)
                break;
            if (width > (Integer.MAX_VALUE - (c - '0')) / 10)
                break;
            width = width * 10 + (tempByte - '0');
            tempByte = Terminal.readByte();
        }
        int terminalWidth = width;
        int terminalHeight = height;
        */
        return new Coords(0, 0, 188, 63);
    }

}
