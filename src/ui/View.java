package ui;

import controller.adapter.TermiosTerminalAdapter;
import io.github.btj.termios.Terminal;
import util.Coords;
import util.Rectangle;

import java.io.IOException;

public abstract class View {

    /**
     * The rectangle which describes the bounds of this view
     */
    Rectangle uiCoordsScaled;

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
     * @param uiCoordsScaled the new coordinates
     */
    public void setScaledCoords(Rectangle uiCoordsScaled) {
        this.uiCoordsScaled = uiCoordsScaled;
    }

    /**
     * @param termiosTerminalAdapter the object that renders the terminal
     * @return coordinates 
     */
    public Coords getRealUICoordsFromScaled(TermiosTerminalAdapter termiosTerminalAdapter) throws IOException {
        Coords screenDimensions = termiosTerminalAdapter.getTextAreaSize();
        return new Coords(
                (int) Math.floor(((double) screenDimensions.width)* uiCoordsScaled.startX),
                (int) Math.floor(((double) screenDimensions.height)* uiCoordsScaled.startY),
                (int) Math.floor(((double) screenDimensions.width)* uiCoordsScaled.width),
                (int) Math.floor(((double) screenDimensions.height)* uiCoordsScaled.height));
    }


    /**
     * Clears the content on the terminal window
     * Used to prevent text ghosting on the screen
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
     * @param activeHash the hash of the active window
     */
    public abstract void render(int activeHash) throws IOException;

    /**
     * Renders the cursor on the current view
     */
    public abstract void renderCursor() throws IOException;

    /**
     * Checks whether this View and the given object are the same type and have the same contents
     * @param o the object to compare this to
     */
    public abstract boolean equals(Object o);


}
