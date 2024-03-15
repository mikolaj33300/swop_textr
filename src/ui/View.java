package ui;

import io.github.btj.termios.Terminal;
import layouttree.LayoutLeaf;

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
     * The leaf to which this view belongs
     */
    protected LayoutLeaf parent;

    public LayoutLeaf getParent(){
        return parent.clone();
    }

    /**
     * Initializes information for a view depending on {@link View#terminalHeight} and {@link View#terminalWidth}
     */
    protected void setCorrectCoords() throws IOException {
        retrieveDimensions();
        startX = parent.getStartX(terminalWidth, terminalHeight);
        startY = parent.getStartY(terminalWidth, terminalHeight);
        width = parent.getWidth(terminalWidth, terminalHeight);
        height = parent.getHeight(terminalWidth, terminalHeight);
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
    public abstract void render() throws IOException;

    /**
     * Renders the cursor on the current view
     */
    public abstract void renderCursor() throws IOException;

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

    /**
     * Checks whether this View and the given object are the same type and have the same contents
     */
    public abstract boolean equals(Object o);
}
