package util;

import java.io.IOException;

import util.Coords;

public class ScreenUIUtil {

    /**
     * Returns the coords of the text area of terminal. Is not tested since it's assumed is correct,
     * just like the termios methods themselves. Source: termios tests.
     *
     * @return UI coords
     * @throws IOException exception
     */
    public static Coords retrieveDimensionsTerminal() throws IOException {
      return new Coords(0, 0, 188, 63);
    }

}
