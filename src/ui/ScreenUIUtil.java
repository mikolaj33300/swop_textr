package ui;

import io.github.btj.termios.Terminal;

import java.io.IOException;

public class ScreenUIUtil {

        public static UICoords retrieveDimensionsTerminal() throws IOException {
            Terminal.reportTextAreaSize();
            for(int i = 0; i < 4; i++)
                Terminal.readByte();

            int c = Terminal.readByte();
            int height = c - '0';
            int tempByte = Terminal.readByte();

            for(;;) {
                if(tempByte < '0' || '9' < tempByte)
                    break;
                if (height > (Integer.MAX_VALUE - (c - '0')) / 10)
                    break;
                height = height * 10 + (tempByte - '0');
                tempByte = Terminal.readByte();
            }
            c = Terminal.readByte();
            int width = c - '0';
            tempByte = Terminal.readByte();

            for(;;) {
                if(tempByte < '0' || '9' < tempByte)
                    break;
                if (width > (Integer.MAX_VALUE - (c - '0')) / 10)
                    break;
                width = width * 10 + (tempByte - '0');
                tempByte = Terminal.readByte();
            }
            int terminalWidth = width;
            int terminalHeight = height;
            return new UICoords(0, 0, terminalWidth, terminalHeight);
        }

}
