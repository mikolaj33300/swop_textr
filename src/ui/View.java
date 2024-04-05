package ui;

import io.github.btj.termios.Terminal;
import layouttree.LayoutLeaf;
import files.FileBuffer;

import java.io.IOException;

public abstract class View {
    /**
     * The lefmost point where this view starts
     */
    int startX;

    /**
     * The topmost point where this view starts
     */
    int startY;

    /**
     * The height of this window
     */
    int height;

    /**
     * The width of this window
     */
    int width;

    /**
     * The total width of the terminal
     */
    static int terminalWidth;

    /**
     * The total height of the terminal
     */
    static int terminalHeight;

    /**
     * Initializes information for a view depending on {@link View#terminalHeight} and {@link View#terminalWidth}
     * this will be set out of the render function which can get it out of the tree
     * TODO
    protected void setCorrectCoords(int hashCode) throws IOException {
        retrieveDimensions();
        startX = parent.getStartX(terminalWidth, terminalHeight, hashCode);
        startY = parent.getStartY(terminalWidth, terminalHeight, hashCode);
        width = parent.getWidth(terminalWidth, terminalHeight, hashCode);
        height = parent.getHeight(terminalWidth, terminalHeight, hashCode);
    }
     */

    /**
     * return this objects hashcode for our keys
     * @return hash
     */
    public int getHash() {
	    return this.hashCode();
    }


    /**
     * Clears the content on the terminal window
     * Used when to prevent text ghosting on the screen
     */
    public void clearContent() throws IOException {
/*        retrieveDimensions();
        for(int i = startY; i<startY+height; i++){
            for(int j = startX; j<startX+width; j++){
                Terminal.printText(i+1, j+1, " ");
            }
        }*/
        Terminal.clearScreen();
    }

    /**
     * Render all the elements on the thid view
     */
    public abstract void render(FileBuffer containedFileBuffer, int hashCode, boolean active) throws IOException;

    /**
     * <p>Calculates the dimensions of the terminal
     * Credits to BTJ. This looks very clean and intuïtive.
     * Sets the fields {@link View#terminalWidth} and {@link View#terminalHeight}.</p>
     * <p>Method set to default for unit test access.</p>
     */
    private void retrieveDimensions() throws IOException {

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
        View.terminalWidth = width;
        View.terminalHeight = height;
    }
}
