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

    public TerminalReader() {}

    /**
     * Returns the width and height in array form.
     * Warning: copied from tests from BTJ himself. This is not my code.
     *
     * Returns the dimensions in form of: [height, width]
     */
    public int[] getDimensions() throws IOException {
        Terminal.reportTextAreaSize();
        int[] dimensions = new int[2];

        // Reads: '\033'
        peekByte(); print("Byte0: " + buffer);
        eatByte();
        // Reads: '['
        peekByte(); print("Byte1: " + buffer);
        eatByte();
        // Reads: '8'
        peekByte(); print("Byte2: " + buffer);
        eatByte();
        // Reads: ';'
        peekByte(); print("Byte3: " + buffer);
        eatByte();
        dimensions[0] = getNumber();
        peekByte(); print("Junk in between " + buffer);
        eatByte();
        dimensions[1] = getNumber();

        System.out.println("Height: " + dimensions[0] + ", width: " + dimensions[1]);

        return dimensions;
    }

    /**
     * Returns height and width of the terminal in an int[] format: [height, width]
     */
    public int[] getD() throws IOException {

        Terminal.reportTextAreaSize();
        int tempByte = 0;
        // Read 4 junk bytes
        for(int i = 0; i < 4; i++) {
            tempByte = Terminal.readByte(); print("Byte" + i + ": " + tempByte);
        }

        int c = Terminal.readByte(); print("Start getNumber: " + c);
        int number1 = c - '0'; print("Number is " + c + " - " + '0' + " = " + number1);
        tempByte = Terminal.readByte(); print("Loop: " + tempByte);

        for(;;) {
            // 1st iteration) Read new byte (this is always the case)
            // 2nd iteration) We will always inspect a byte here, but not throw it away always
            if(tempByte < '0' || '9' < tempByte) {
                print("Breaking with number " + number1);
                break;
            }
            if (number1 > (Integer.MAX_VALUE - (c - '0')) / 10)
                break; // should never happen

            number1 = number1 * 10 + (tempByte - '0'); print("L Number is " + tempByte + " - " + '0' + " = " + number1);
            // If we are here, we will always read a new byte. We overwrite the previous
            tempByte = Terminal.readByte(); print("Loop: " + tempByte);

        }

        // Previous loop will hold a tempByte before breaking, this byte is a junk byte between the two numbers.
        // We don't read a byte here.
        print("Here should be the junk: " + tempByte);
        // Read second number. We can read here, because the previous character was flushed.
        c = Terminal.readByte(); print("Start getNumber: " + c);
        int number2 = c - '0'; print("Number is " + c + " - " + '0' + " = " + number2);
        tempByte = Terminal.readByte(); // this byte is also ALWAYS read. We read two bytes at the start.
        print("Loop: " + tempByte);

        for(;;) {
            // 1st iteration) Read new byte
            // 2nd iteration) Flushed previous byte, and check out new one
            //                Can possibly break here, so we don't throw byte away...
            if(tempByte < '0' || '9' < tempByte) {
                print("Breaking with number " + number2);
                break;
            }
            if (number2 > (Integer.MAX_VALUE - (c - '0')) / 10)
                break; // should never happen

            number2 = number2 * 10 + (tempByte - '0'); print("L Number is " + tempByte + " - " + '0' + " = " + number2);
            tempByte = Terminal.readByte();

        }

        System.out.println("2) Height: " + number1 + ", width: " + number2);

        return new int[] {number1, number2};


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
        // We ate previous so this will refill buffer
        int c = peekByte(); print("Start getNumber: " + c);
        int result = c - '0'; print("Number is " + c + " - " + '0' + " = " + result);
        eatByte();

        for ( ; ; ) {
            c = peekByte(); print("- Loop " + c);
            if (c < '0' || '9' < c)
                break;
            eatByte();
            if (result > (Integer.MAX_VALUE - (c - '0')) / 10)
                return -1;

            result = result * 10 + (c - '0'); print("L Number is " + c + " - " + '0' + " = " + result);
        }
        return result;
    }

    private void print(String s) {
        System.out.println(s);
    }


}
