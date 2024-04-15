package ui;

import io.github.btj.termios.Terminal;
import layouttree.LayoutLeaf;
import util.Debug;

import java.io.IOException;

public abstract class View {

    /*
     * the hashcode of the rendered object
     */
    private final int hashCode;

    Rectangle uiCoordsScaled;

    /**
     * The total width of the terminal
     */
    static int terminalWidth;

    /**
     * The total height of the terminal
     */
    static int terminalHeight;

    public View(int hashCode){
        this.hashCode = hashCode;
    }

    public int getHashCode(){
        return this.hashCode;
    }

    /**
     * Initializes information for a view depending on {@link View#terminalHeight} and {@link View#terminalWidth}
     */
    public void setScaledCoords(Rectangle uiCoordsScaled) {
        this.uiCoordsScaled = uiCoordsScaled;
    }

    public UICoords getRealUICoordsFromScaled() throws IOException {
        UICoords screenDimensions = ScreenUIUtil.retrieveDimensionsTerminal();
        return new UICoords(
                (int) Math.floor(((double) screenDimensions.width)* uiCoordsScaled.startX),
                (int) Math.floor(((double) screenDimensions.height)* uiCoordsScaled.startY),
                (int) Math.floor(((double) screenDimensions.width)* uiCoordsScaled.width),
                (int) Math.floor(((double) screenDimensions.height)* uiCoordsScaled.height));
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
     * Render all the elements on the this view
     */
    public abstract void render(int activeHash) throws IOException;

    /**
     * Renders the cursor on the current view
     */
    public abstract void renderCursor() throws IOException;

    /**
     * Checks whether this View and the given object are the same type and have the same contents
     */
    public abstract boolean equals(Object o);
}
