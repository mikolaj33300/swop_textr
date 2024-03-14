package ui;

import core.Controller;
import io.github.btj.termios.Terminal;
import layouttree.LayoutLeaf;
import layouttree.LayoutNode;

import java.io.IOException;

public abstract class View {
    int startX;
    int startY;
    int height;
    int width;

    static int terminalWidth;
    static int terminalHeight;

    protected LayoutLeaf parent;

    //public View(LayoutLeaf parent) {
        //this.parent = parent;
    //}

    protected void setCorrectCoords() throws IOException {
        retrieveDimensions();
        startX = parent.getStartX(terminalWidth, terminalHeight);
        startY = parent.getStartY(terminalWidth, terminalHeight);
        width = parent.getWidth(terminalWidth, terminalHeight);
        height = parent.getHeight(terminalWidth, terminalHeight);
    }



    public void clearContent() throws IOException {
/*        retrieveDimensions();
        for(int i = startY; i<startY+height; i++){
            for(int j = startX; j<startX+width; j++){
                Terminal.printText(i+1, j+1, " ");
            }
        }*/
        Terminal.clearScreen();
    };

    public abstract void render() throws IOException;

    public abstract void renderCursor() throws IOException;

    /**
     * <p>Calculates the dimensions of the terminal
     * Credits to BTJ. This looks very clean and intuïtive.
     * Sets the fields {@link View#terminalWidth} and {@link View#terminalHeight}.</p>
     * <p>Method set to default for unit test access.</p>
     */
    private void retrieveDimensions() throws IOException {

        Terminal.reportTextAreaSize();
        int tempByte = 0;
        for(int i = 0; i < 4; i++)
            Terminal.readByte();

        int c = Terminal.readByte();
        int height = c - '0';
        tempByte = Terminal.readByte();

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

    public abstract boolean equals(Object o);
}
