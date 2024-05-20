package ui;

import controller.adapter.TermiosTerminalAdapter;
import io.github.btj.termios.Terminal;
import util.Coords;
import util.Rectangle;

import java.io.IOException;

public abstract class View {

    public abstract int getFocusedCol();

    public abstract int getFocusedLine();
    public abstract int getTotalContentHeight();
    /**
     * The adapter used for interacting with termios
     */
    protected TermiosTerminalAdapter termiosTerminalAdapter;


    Coords uiCoordsReal;

    /**
     * The total width of the terminal
     */
    static int terminalWidth;

    /**
     * The total height of the terminal
     */
    static int terminalHeight;

    public TermiosTerminalAdapter getTermiosTerminalAdapter() {
        return termiosTerminalAdapter;
    }

    public View(TermiosTerminalAdapter termiosTerminalAdapter) {
        this.termiosTerminalAdapter = termiosTerminalAdapter;
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


    protected abstract int getLineLength(int focusedLine);

    public void setRealCoords(Rectangle rectangle) {
        this.uiCoordsReal = new Coords((int) Math.floor(rectangle.startX), (int) Math.floor(rectangle.startY), (int) Math.floor(rectangle.width), (int) Math.floor(rectangle.height));
    }

    public Coords getRealCoords() {
        return uiCoordsReal;
    }

    public void setTermiosTerminalAdapter(TermiosTerminalAdapter adapter){
        this.termiosTerminalAdapter = adapter;
    };
}
